FROM openjdk:11-jdk-slim
WORKDIR /app
COPY TemperatureSensor.java .
RUN javac TemperatureSensor.java
CMD ["java", "TemperatureSensor"]