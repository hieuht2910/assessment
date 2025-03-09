```mermaid
flowchart TB
    %% Client Applications
    subgraph ClientApps["Client Applications"]
        direction LR
        CustomerApp["Customer App"]
        ShopOwnerApp["Shop Owner App"]
        ThirdPartyApps["3rd Party Apps"]
    end

    %% API Gateway Layer
    subgraph Gateway["API Gateway Layer"]
        direction LR
        APIGateway["REST API Gateway"]
        WebSocketGateway["WebSocket Gateway"]
    end

    %% Microservices Layer
    subgraph Microservices["Microservices Layer"]
        direction LR
        AuthService["Auth Service"]
        UserService["User Service"]
        ShopService["Shop Service"]
        OrderService["Order Service"]
        NotificationService["Notification Service"]
    end

    %% Messaging and Caching
    subgraph Messaging["Messaging & Caching Layer"]
        direction LR
        RabbitMQ["RabbitMQ"]
        RedisCache["Redis Cache"]
    end

    %% Database Layer
    subgraph Database["Database Layer"]
        PostgreSQL["PostgreSQL Database"]
    end

    %% Connections - Client to Gateway
    CustomerApp --> APIGateway
    CustomerApp -.-> WebSocketGateway
    ShopOwnerApp --> APIGateway
    ShopOwnerApp -.-> WebSocketGateway
    ThirdPartyApps --> APIGateway

    %% Connections - Gateway to Services
    APIGateway --> AuthService
    APIGateway --> UserService
    APIGateway --> ShopService
    APIGateway --> OrderService
    WebSocketGateway -.-> OrderService

    %% Connections - Services to Database
    AuthService --> PostgreSQL
    UserService --> PostgreSQL
    ShopService --> PostgreSQL
    OrderService --> PostgreSQL

    %% Connections - Services to Messaging/Caching
    OrderService --> RabbitMQ
    OrderService --> RedisCache
    
    %% WebSocket specific flows
    OrderService -.-> WebSocketGateway
    
    %% Style
    classDef highlight fill:#4a9ff5,stroke:#333,stroke-width:2px;
    classDef messaging fill:#ffb366,stroke:#333,stroke-width:1px;
    classDef caching fill:#7be37b,stroke:#333,stroke-width:1px;
    classDef database fill:#f5f5f5,stroke:#333,stroke-width:1px;
    classDef websocket stroke-dasharray: 5 5;
    
    class OrderService,WebSocketGateway highlight;
    class RabbitMQ messaging;
    class RedisCache caching;
    class PostgreSQL database;
    
    %% WebSocket connections style
    linkStyle 4,5,9,15 stroke:#0066cc,stroke-width:2px,stroke-dasharray: 5 5;
```

## Architecture Overview with WebSocket for Order Tracking

This diagram represents a microservices architecture for a food ordering system with real-time order tracking capabilities via WebSockets.

### Key Components:

1. **Client Applications**
   - Customer App: Used by customers to place and track orders in real-time
   - Shop Owner App: Used by restaurant/shop owners to manage orders and inventory
   - 3rd Party Apps: External applications that integrate with the system

2. **API Gateway Layer**
   - REST API Gateway: Handles traditional HTTP requests
   - WebSocket Gateway: Manages persistent WebSocket connections for real-time updates

3. **Microservices Layer**
   - Auth Service: Handles authentication and authorization
   - User Service: Manages user profiles and preferences
   - Shop Service: Manages shop/restaurant information
   - Order Service: Processes and tracks orders (highlighted as a key service)

4. **Messaging & Caching Layer**
   - RabbitMQ: Message broker for asynchronous communication between services
   - Redis Cache: In-memory data store for caching and pub/sub capabilities

5. **Database Layer**
   - PostgreSQL: Persistent storage for all services

### WebSocket Flow for Order Tracking:

1. When an order status changes, the Order Service directly updates connected clients
2. The Order Service communicates with the WebSocket Gateway to push updates
3. The WebSocket Gateway pushes real-time updates to connected clients (Customer App and Shop Owner App)
4. The Order Service can use Redis for temporary state storage and RabbitMQ for processing order events

### Visual Representation:
- **Solid lines**: Regular HTTP/API communication
- **Dashed lines**: WebSocket/real-time communication
- **Blue highlighted components**: Key services in the WebSocket flow
- **Orange component**: Message broker (RabbitMQ)
- **Green component**: Caching layer (Redis)

This architecture enables real-time order tracking, allowing customers to see live updates of their order status and shop owners to receive instant notifications about new orders. By having the Order Service directly handle WebSocket communications, the architecture is simplified while maintaining the ability to provide real-time updates. 