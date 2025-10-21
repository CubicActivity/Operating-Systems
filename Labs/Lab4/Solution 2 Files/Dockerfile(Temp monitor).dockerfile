FROM openjdk:11-jdk-slim
WORKDIR /app
COPY TemperatureMonitor.java .
RUN javac TemperatureMonitor.java
CMD ["java", "TemperatureMonitor"]