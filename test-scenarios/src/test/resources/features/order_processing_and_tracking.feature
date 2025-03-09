Feature: Order Processing and Real-time Tracking

  Scenario: ORDER-004 Order processing flow with real-time status updates
    Given the coffee shop system is running
    And a queue "Main Queue" is created for shop 1 with max size 50
    And a customer "Emma" wants to order "1 Cappuccino" from shop 1
    When the order is created via REST API
    Then the order should be created with status "IN_QUEUE"
    And a message should be published to the order queue
    
    When the order listener processes the message
    Then the order status should be updated to "IN_QUEUE"
    And the order should be assigned to shop 1
    
    When the customer subscribes to order updates via WebSocket
    And the shop updates the order status to "PROCESSING"
    Then the customer should receive a WebSocket notification with status "PROCESSING"
    
    When the shop updates the order status to "READY"
    Then the customer should receive a WebSocket notification with status "READY"

  Scenario: ORDER-005 Customer cancels an order with real-time notifications
    Given the coffee shop system is running
    And a queue "Express Queue" is created for shop 2 with max size 30
    And a customer "Frank" has placed an order for "1 Mocha" with status "IN_QUEUE"
    And the shop 2 has received the order
    And the customer is subscribed to order updates via WebSocket
    
    When the customer cancels the order via REST API
    Then the order status should be updated to "CANCELLED"
    And the customer should receive a WebSocket notification with status "CANCELLED"
    And the shop should receive a WebSocket notification with status "CANCELLED"
    And the cancellation should be confirmed via REST API response 