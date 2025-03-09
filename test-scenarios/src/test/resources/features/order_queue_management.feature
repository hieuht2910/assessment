Feature: Coffee Shop Order Queue Management

  Scenario: ORDER-001 Create and track order in queue
    Given the coffee shop system is running
    And a queue "Main Queue" is created for shop 1 with max size 50
    And a customer "John" wants to order "1 Latte" from shop 1
    When the order is created
    Then the order should be created successfully
    And the order should be assigned to a queue
    And the queue size should be 1
    And the customer should see their position as 1
    And the estimated waiting time should be 5 minutes

  Scenario: ORDER-002 Multiple orders queue management
    Given the coffee shop system is running
    And a queue "Main Queue" is created for shop 1 with max size 50
    And the following orders are placed:
      | customerName | orderDetails     | shopId |
      | Alice       | 1 Cappuccino     | 1      |
      | Bob         | 2 Espresso       | 1      |
      | Charlie     | 1 Latte, 1 Mocha | 1      |
    When I check the queue status
    Then the queue size should be 3
    And the queue should have the following order:
      | position | customerName | status  |
      | 1        | Alice       | IN_QUEUE |
      | 2        | Bob         | IN_QUEUE |
      | 3        | Charlie     | IN_QUEUE |

  Scenario: ORDER-003 Order status updates
    Given the coffee shop system is running
    And a queue "Main Queue" is created for shop 1 with max size 50
    And a customer "David" has placed an order for "1 Americano"
    When the shop updates the order status to "PROCESSING"
    Then the customer should receive a status update
    And the order status should be "PROCESSING"
    When the shop updates the order status to "READY"
    Then the customer should receive a status update
    And the order status should be "READY"