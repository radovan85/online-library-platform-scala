FROM tomcat:10.1-jdk17

WORKDIR /app

COPY target/online-library.war /app/app.war

RUN mv /app/app.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]