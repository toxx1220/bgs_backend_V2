FROM openjdk:21-jdk-slim
RUN addgroup --system spring && adduser --system --ingroup spring spring
WORKDIR /bgs

# Add volume directory setup
VOLUME /data
RUN mkdir -p /data && \
    mkdir -p /home/spring/.config/jgit && \
    chown -R spring:spring /data && \
    chown -R spring:spring /home/spring

RUN chown -R spring:spring /bgs
USER spring:spring
ARG JAR_FILE=app.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx1536M", "-jar", "app.jar"]