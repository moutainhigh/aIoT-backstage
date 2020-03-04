FROM         java:8
#FROM        tomcat:8
# 环境变量
ENV APP     aiot-backstage-0.0.1-SNAPSHOT.jar

WORKDIR     /ysw/app
VOLUME         /ysw/app
ADD            $APP /ysw/app/app.jar
ENTRYPOINT     ["java", "-jar", "/ysw/app/app.jar"]
EXPOSE         8081