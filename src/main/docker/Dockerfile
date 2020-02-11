FROM java:8

# 挂载test-docker目录
VOLUME /tmp

# COPY or ADD to image
COPY aiot-backstage-0.0.1-SNAPSHOT.jar app.jar

RUN bash -c "touch /app.jar"

EXPOSE 8080 18080
ENTRYPOINT ["java", "-jar", "app.jar"]