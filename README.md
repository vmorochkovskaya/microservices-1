### 1. Start resources
   ##### 1. ```cd intro-task-1```
   ##### 2. ```docker compose up -d```
   ##### 3. create a topic:
- ```docker exec -it kafka bash```
- ```kafka-topics.sh --create --topic resource-event --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1```
 ##### 4. ```mvn spring-boot:run```
### 2. Start songs
   ```cd intro-tasl-1-song```
   ```mvn spring-boot:run```
### 3. Start processor
   ```cd resource-processor```
   ```mvn spring-boot:run```
### 4. Postman
POST ```http://127.0.0.1:8083/resources``` with binary body with mp3 file. Use audio/mpeg header