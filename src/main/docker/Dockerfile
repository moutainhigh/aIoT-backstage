FROM java:8

VOLUME /tmp

ADD aiot-backstage-0.0.1-SNAPSHOT.jar aiot-app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/aiot-app.jar" ]