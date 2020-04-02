FROM java:8

MAINTAINER yuchencd93<yuchencd93@gmail.com>

WORKDIR /usr/lib
ADD /so/libDPSDK_Core.so libDPSDK_Core.so
ADD /so/libDPSDK_Java.so libDPSDK_Java.so
ADD /so/libdsl.so libdsl.so
ADD /so/libdslalien.so libdslalien.so
ADD /so/libPicSdk.so libPicSdk.so
ADD /so/libPlatformSDK.so libPlatformSDK.so
ADD /so/libStreamConvertor.so libStreamConvertor.so
RUN cd /root/home
ADD /target/aiot-backstage-0.0.1-SNAPSHOT.jar aiot-app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/aiot-app.jar" ]