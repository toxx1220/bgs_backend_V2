FROM openjdk:21-jdk-slim
ARG HOME_DIR
ARG DATA_DIR
ARG CONTAINER_USER
ARG CONTAINER_GROUP
RUN addgroup --system $CONTAINER_GROUP && adduser --system --ingroup $CONTAINER_GROUP --home $HOME_DIR $CONTAINER_USER
WORKDIR /bgs

# Create directories with explicit ownership
RUN mkdir -p $DATA_DIR && \
    mkdir -p $HOME_DIR/.config/jgit && \
    chown -R $CONTAINER_USER:$CONTAINER_GROUP $DATA_DIR && \
    chown -R $CONTAINER_USER:$CONTAINER_GROUP $HOME_DIR

RUN chown -R $CONTAINER_USER:$CONTAINER_GROUP /bgs
USER $CONTAINER_USER:$CONTAINER_GROUP
ARG JAR_FILE=app.jar
COPY --chown=$CONTAINER_USER:$CONTAINER_GROUP ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx5.5G", "-Duser.home=${HOME_DIR}", "-jar", "app.jar"]
