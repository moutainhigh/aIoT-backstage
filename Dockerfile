FROM java:8

MAINTAINER yuchencd93<yuchencd93@gmail.com>

ADD libDPSDK_Core.so /usr/lib/libDPSDK_Core.so
ADD libDPSDK_Java.so /usr/lib/libDPSDK_Java.so
ADD libdsl.so /usr/lib/libdsl.so
ADD libdslalien.so /usr/lib/libdslalien.so
ADD libPicSdk.so /usr/lib/libPicSdk.so
ADD libPlatformSDK.so /usr/lib/libPlatformSDK.so
ADD libStreamConvertor.so /usr/lib/libStreamConvertor.so
ADD dahuasdk.conf /etc/ld.so.conf.d/dahuasdk.conf
RUN /sbin/ldconfig
ADD aiot-backstage-0.0.1-SNAPSHOT.jar /home/aiot-app.jar

EXPOSE 8080 18080

ENTRYPOINT [ "java", "-jar", "/home/aiot-app.jar", "--spring.config.location=/home/config/application.yml"]