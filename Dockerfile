FROM tomcat:9-jdk11
# L'image ne contient que Tomcat, le fichier WAR serait normalement copié ici
EXPOSE 8080
CMD ["catalina.sh", "run"]