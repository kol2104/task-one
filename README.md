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

### Feedback

- Was it easy to complete the task using AI? 

Yes

- How long did task take you to complete? (Please be honest, we need it to gather anonymized statistics)

2 hours 30 minutes

- Was the code ready to run after generation? What did you have to change to make it usable?

Yes, it was ready to run

- Which challenges did you face during completion of the task?

Integrate JaCoCo and SonarQube

- Which specific prompts you learned as a good practice to complete the task?

Ask ChatGPT to generate prompts to ask it about task
