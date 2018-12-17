FROM pwittchen/alpine-java11
# FROM fiadliel/java8-jre:8u131
VOLUME /tmp
ADD build/libs/ServiceLayer-1.0-SNAPSHOT.jar ServiceLayer.jar
#Copy the configuration file with the Library path!
COPY ld-musl-x86_64.path /etc/ld-musl-x86_64.path
# RUN bash -c 'touch /ServiceLayer.jar'
RUN touch ServiceLayer.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ServiceLayer.jar"]
