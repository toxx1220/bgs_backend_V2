package de.bgs.secondary.git

import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.lib.ProgressMonitor
import java.time.Instant

class LoggingProgressMonitor : ProgressMonitor {
    private val logger = KotlinLogging.logger {}
    private var taskName: String = ""
    private var totalWork: Int = 0
    private var lastProgress: Int = 0
    private var hasBegun = false
    private var startTime: Instant = Instant.now()
    private var showDuration = false

    override fun start(totalTasks: Int) {
        this.startTime = Instant.now()
        logger.info { "Git clone started (total tasks: $totalTasks)" }
    }

    override fun beginTask(title: String?, totalWork: Int) {
        this.taskName = title ?: "Unknown task"
        this.totalWork = totalWork
        this.lastProgress = 0
        this.hasBegun = true

        logger.debug { "Started: $taskName (total work units: $totalWork)" }
    }

    override fun update(completed: Int) {
        if (!hasBegun) return

        val currentProgress = lastProgress + completed
        this.lastProgress = currentProgress

        if (totalWork > 0) {
            val progressPercentage = (currentProgress * 100) / totalWork
            // Log at 25%, 50%, 75% and 100% to keep it concise
            if (progressPercentage % 25 == 0 && progressPercentage > 0) {
                logger.info { "$taskName: $progressPercentage% complete ($currentProgress/$totalWork). Took ${getFormattedDurationString()}" }
            }
        } else {
            // For indeterminate tasks, log every 1000 units
            if (currentProgress % 1000 == 0 && currentProgress > 0) {
                logger.info { "$taskName: $currentProgress work units completed. Took ${getFormattedDurationString()}" }
            }
        }
    }

    override fun endTask() {
        if (hasBegun) {
            logger.info { "Completed: $taskName. Took ${getFormattedDurationString()}" }
            hasBegun = false
        }
    }

    override fun isCancelled(): Boolean {
        return false
    }

    override fun showDuration(enabled: Boolean) {
        this.showDuration = enabled
    }

    private fun getFormattedDurationString(): String {
        val duration = Instant.now().toEpochMilli() - startTime.toEpochMilli()
        val seconds = duration / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m ${seconds % 60}s"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
}
