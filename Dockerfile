FROM openjdk:17-slim

LABEL authors="wellingtoncasarin.pl089@academico.ifsul.edu.br"

WORKDIR /app

COPY /build/libs/CarHired-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 80

CMD ["java", "-jar", "/app/app.jar"]