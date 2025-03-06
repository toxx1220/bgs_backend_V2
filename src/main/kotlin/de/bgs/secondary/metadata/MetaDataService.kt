package de.bgs.secondary.metadata

import de.bgs.secondary.database.BoardGameItem
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jsoup.Jsoup
import org.springframework.http.client.ReactorClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import org.yaml.snakeyaml.util.Tuple
import java.io.StringReader
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory

private const val BGG_URI = "https://boardgamegeek.com/xmlapi/boardgame"

private const val XML_DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl"

private const val XML_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities"

private const val XML_BOARDGAME_TAG = "boardgame"

private const val XML_OBJECT_ID_TAG = "objectid"

private const val XML_IMAGE_TAG = "image"

private const val XML_DESCRIPTION_TAG = "description"

@Service
class MetaDataService {

    private val logger = KotlinLogging.logger { }

    fun retrieveMetaData(boardGameItemList: List<BoardGameItem>): List<BoardGameItem> {
        val client = RestClient.builder()
            .requestFactory(ReactorClientHttpRequestFactory())
            .build()

        val boardGameIds = boardGameItemList.joinToString(",") { it.bggId.toString() }

        val xmlResponseBody = client.get()
            .uri(URI("$BGG_URI/$boardGameIds"))
            .header("Accept-Encoding", "gzip")
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful }) { _, response ->
                logger.error { "Failed to retrieve image URIs from BGG API: ${response.statusCode} for $boardGameIds" }
            }
            .body(String::class.java)

        if (xmlResponseBody.isNullOrEmpty()) {
            logger.error { "Response Body from BGG API is null for $boardGameIds, for IDs: $boardGameIds" }
            return boardGameItemList
        }


        val imageUris: Map<Long, Tuple<String?, String?>> = parseImageUriFrom(xmlResponseBody)
        return boardGameItemList.map {
            it.imageUri = imageUris[it.bggId]?._1()
            it.description = imageUris[it.bggId]?._2()
            it
        }
    }

    private fun parseImageUriFrom(xmlString: String): Map<Long, Tuple<String?, String?>> {

        return try {
            val cleanedXml = xmlString.trim().replace("\uFEFF", "") // Remove UTF-8 BOM if present
            val factory = DocumentBuilderFactory.newInstance().apply {
                setFeature(XML_DISALLOW_DOCTYPE_DECL, true)
                setFeature(XML_EXTERNAL_GENERAL_ENTITIES, false)
            }

            val builder = factory.newDocumentBuilder()
            val document = builder.parse(InputSource(StringReader(cleanedXml)))
            val boardGameImageUris: MutableMap<Long, Tuple<String?, String?>> = mutableMapOf()

            val nodeList: NodeList = document.documentElement.getElementsByTagName(XML_BOARDGAME_TAG)
            for (i in 0 until nodeList.length) {
                val node = nodeList.item(i) as Element
                val objectId = node.getAttribute(XML_OBJECT_ID_TAG).toLongOrNull() ?: continue
                val imageUriItem = node.getElementsByTagName(XML_IMAGE_TAG)
                    .item(0)?.textContent?.trim()?.let { Jsoup.parse(it).text() }
                val description = node.getElementsByTagName(XML_DESCRIPTION_TAG)
                    .item(0)?.textContent?.trim()?.let { Jsoup.parse(it).text() }

                boardGameImageUris[objectId] = Tuple(imageUriItem, description)
            }
            logger.info { "Successfully parsed URIs $boardGameImageUris" }

            boardGameImageUris
        } catch (e: SAXParseException) {
            logger.error { "XML parsing failed at line ${e.lineNumber}, column ${e.columnNumber}: ${e.message}" }
            logger.debug { "Problematic XML snippet:\n${xmlString.take(500)}" }
            emptyMap()
        } catch (e: Exception) {
            logger.error { "XML parsing failed: ${e.message}" }
            emptyMap()
        }
    }
}
