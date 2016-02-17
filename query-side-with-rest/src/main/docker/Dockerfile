FROM java:8
VOLUME /tmp
EXPOSE 9000
ADD product-query-side.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]