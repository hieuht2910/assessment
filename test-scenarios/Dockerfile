FROM maven:3.8.4-openjdk-17

WORKDIR /app
COPY . .

# We don't run the tests here because they will be run via the command in docker-compose
CMD ["mvn", "clean", "test"] 