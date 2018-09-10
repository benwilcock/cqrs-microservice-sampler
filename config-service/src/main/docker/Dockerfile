FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
EXPOSE 8888
ADD config-service.jar app.jar
ADD *.yml config/
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]