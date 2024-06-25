FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} yukgaejang.jar
ENTRYPOINT ["java","-jar","/yukgaejang.jar","--spring.profiles.active=prod"]

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime