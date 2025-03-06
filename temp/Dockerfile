FROM tomcat:9.0-jdk17
COPY webapps/student-parser-api.war /usr/local/tomcat/webapps/
COPY excel.xlsx /usr/local/tomcat/
EXPOSE 8080
CMD ["catalina.sh", "run"]
