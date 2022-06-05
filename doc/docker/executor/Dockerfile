FROM frolvlad/alpine-oraclejre8

ENV TZ=Asia/Shanghai

ARG JAR_FILE
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY myjob-executor-sample-springboot-1.0.0-SNAPSHOT.jar /myjob/app.jar
WORKDIR /myjob

ENTRYPOINT ["java","-server", "-Xms512m", "-Xmx512m","-jar","app.jar"]