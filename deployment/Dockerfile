FROM openjdk:21-jdk-slim
RUN addgroup --system spring && adduser --system --ingroup spring spring
WORKDIR /bgs

ENV HOME=/home/spring

# Add volume directory setup
VOLUME /data
RUN mkdir -p /data && \
    mkdir -p $HOME/.config/jgit && \
    chown -R spring:spring /data && \
    chown -R spring:spring $HOME

RUN chown -R spring:spring /bgs
USER spring:spring
ARG JAR_FILE=app.jar
COPY --chown=spring:spring ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:+UseZGC", "-Xmx1000M", "-Xms500M", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]