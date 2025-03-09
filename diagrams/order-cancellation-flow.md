```mermaid
sequenceDiagram
    participant Customer
    participant Order Service
    participant WebSocket
    participant Shop
    
    Note over Customer,Shop: Order already created and in IN_QUEUE or PROCESSING state
    
    Customer->>Order Service: PUT /api/orders/{orderId}/cancel
    Order Service->>Order Service: Update status to CANCELLED
    
    Order Service->>WebSocket: Broadcast status update
    WebSocket-->>Customer: Order status update (CANCELLED)
    WebSocket-->>Shop: Order status update (CANCELLED)
    
    Order Service-->>Customer: Order cancelled successfully 
```

Step-by-step breakdown of the order cancellation flow:
1. Customer sends a PUT request to /api/orders/{orderId}/cancel endpoint of the Order Service
2. Order Service updates the order status to "CANCELLED"
3. Order Service broadcasts the status update to WebSocket
4. WebSocket sends the order status update ("CANCELLED") to the Customer
5. WebSocket sends the order status update ("CANCELLED") to the Shop
6. Order Service returns confirmation to the Customer with "Order cancelled successfully" 