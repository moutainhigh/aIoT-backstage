#使用java环境
FROM java
#缓存目录
VOLUME tmp
#将当前项目的jar添加到容器中
ADD  "target/aiot-backstage-0.0.1-SNAPSHOT.jar" "aiot-app.jar"
#当容器启动时执行启动命令
ENTRYPOINT ["java","-jar","aiot-app.jar"]