FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
EXPOSE 9000 9001
ADD product-command-side.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]