# Coffee Shop Order System Test Scenarios

This project contains automated test scenarios for the Coffee Shop Order System using Cucumber and RestAssured.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- The Coffee Shop Order System application running on localhost:8080

## Running the Tests

### Using Maven

To run all tests:

```bash
mvn clean test
```

To generate a Cucumber report:

```bash
mvn clean verify
```

The Cucumber reports will be generated in the following locations:
- HTML Report: `target/cucumber-reports.html`
- JSON Report: `target/cucumber-reports.json`
- XML Report: `target/cucumber-reports.xml`
- Detailed HTML Reports: `target/cucumber-reports/`

### Using IDE

You can also run the tests directly from your IDE by running the `CucumberTestRunner` class or individual feature files.

## Test Scenarios

The project includes the following test scenarios:

1. **ORDER-001**: Create and track order in queue
2. **ORDER-002**: Multiple orders queue management
3. **ORDER-003**: Order status updates
4. **ORDER-004**: Order processing flow with real-time status updates
5. **ORDER-005**: Customer cancels an order with real-time notifications

## Project Structure

- `src/test/java/com/digital/CucumberTestRunner.java`: The main entry point for running Cucumber tests
- `src/test/java/com/digital/steps/`: Contains the step definitions
- `src/test/java/com/digital/model/`: Contains the data models
- `src/test/resources/features/`: Contains the feature files with Gherkin scenarios
- `src/test/resources/classifications.properties`: Contains metadata for the Cucumber reports

## Sequence Diagrams

The project includes sequence diagrams that illustrate the flows being tested:

- `diagrams/order-processing-flow.mermaid`: Illustrates the order processing flow
- `diagrams/order-cancellation-flow.mermaid`: Illustrates the order cancellation flow 