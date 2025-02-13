FROM openjdk:21-jdk-slim
RUN addgroup --system spring && adduser --system --ingroup spring spring
WORKDIR /bgs
RUN chown spring:spring /bgs
USER spring:spring
ARG JAR_FILE=app.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx1536M", "-jar", "app.jar"]