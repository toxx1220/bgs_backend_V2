FROM openjdk:21-jdk-slim
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
WORKDIR /bgs
ARG JAR_FILE=app.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/bgs/app.jar"]