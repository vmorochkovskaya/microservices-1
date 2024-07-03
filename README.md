1. ```cd intro-task-1```
```mvn clean install```
2. ```cd intro-tasl-1-song```
   ```mvn clean install```
3. Provide your aws secrets in .env
4. ```docker compose up -d```
5. POST ```http://127.0.0.1:8082/resources``` with binary body with mp3 file. Use audio/mpeg header