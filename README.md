## Task one

### Getting start 

1. Use ```docker-compose.yml``` file to up MySql database. 
You can use command:

        docker compose up

2. Build and run the application:
```
mvn clean package
```
```
java -jar ./target/task-one-0.0.1-SNAPSHOT.jar
```

### JaCoCo coverage report

Make JaCoCo tests coverage report with:

      mvn clean test jacoco:report

### SonarQube quality check

Check quality of code with:

      mvn clean verify sonar:sonar -Dsonar.token=<your_token>