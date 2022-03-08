FROM openjdk:11
VOLUME /tmp
ADD build/libs/billscan-0.0.1-SNAPSHOT.war dockerimage.war
EXPOSE 8080
RUN bash -c 'touch /dockerimage.war'
ENTRYPOINT ["java","-jar","dockerimage.war"]