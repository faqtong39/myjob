FROM frolvlad/alpine-oraclejre8:full

ENV TZ=Asia/Shanghai

ARG JAR_FILE
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ${JAR_FILE} /myjob/app.jar
WORKDIR /myjob

ENTRYPOINT ["java","-jar", "-Xms1024m -Xmx1024m","/app.jar"]