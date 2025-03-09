TABLE OF CONTENTS
1 Background 2
1.1 Approach 2
1.2 Assessment 2
1.3 Standards to follow 2
2 Solution Requirements 3
2.1 Background 3
2.2 The Coffee Shop App 3
2.3 The Customer App 3
3 Deliverables 5
3.1 Part 1 5
3.2 Part 2 5
Version: 1.1 All Copyrights 101 Digital PTE LTD 2019 - 2024 Page 1
1 Background
This document outlines a mini project that is used to assess the technical knowledge and
competency of platform engineers at 101 Digital PTE LTD.
1.1 Approach
Candidates taking this assessment should read and understand the following items clearly:
1. 2. 3. The mini project requirements outlined in this document
The assessment criteria outlined below
The deliverables to be sent to 101 Digital.
1.2 Assessment
You will be assessed on the following assessment criteria:
1. 2. 3. 4. 5. 6. Packaging - how well you package your deliverables to make it easy for us to access,
build, deploy, test, review
Design - clear design of the APIs and any associated items such as databases
Working - a working API / APIs demonstrated through an app / web page or test scripts
Documentation - clear documentation of design, deployment and testing
Deployment - ease of deployment and testing the code / APIs
Value Add - additional items that you have considered and contributed to the project
(that we have not requested in the requirements)
1.3 Standards to follow
The following section outlines the standards you should follow:
1. 2. 3. 4. Use Java Spring Boot framework version 17 to develop the APIs
Use Postgres as database where a database is required
We will be deploying and testing your services on AWS using Docker containers
We prefer completely automated database setup (e.g. Liquibase).
Version: 1.1 All Copyrights 101 Digital PTE LTD 2019 - 2024 Page 2
2 Solution Requirements
The requirements for the overall solution are outlined below.
2.1 Background
A global coffee shop chain / franchise intends to launch an app to allow their regular
customers to pre-order coffee to pick up (say, on their way to work).
They have identified the following needs:
1. The coffee shop chain is a global network. So, they need to service shop locations
across multiple geographies
2. 3. 4. 5. The space is quite limited. So, they want everything to work easily on an App.
They need two Apps (a) One for the shop owner and (b) One for the customer
They have decided to build (a) on Android and (b) on iOS and Android
Not all coffee shops have the same menu. So, they need to be able to handle a menu
based on the shop
6. Most of their shops have only one queue, but some shops are able to support up to 3
queues
7. They would like their service to be API enabled, so that others (3rd parties) can build
apps using their APIs.
2.2 The Coffee Shop App
The requirements for the Coffee Shop App are as follows:
1. Allows the shop owner to login as an admin user
2. Allows the shop owner to setup / configure the app to support their shop
3. Allows the shop owner to configure the shop / app as follows:
a. Location and Contact details
b. The coffee menu & pricing
c. Number of queues and the maximum size of the queue
d. opening / closing times.
4. Allows the shop operator to login and manage the queue
5. To view / see the size of the queue and the number of waiting customers
6. To easily view the orders placed by the customers in the queue
7. The name of the persons in the queue
8. A score indicating the number of times that customers has been served by the coffee
shop chain
9. Take a customer off the queue and service them.
2.3 The Customer App
The requirements for the Customer App are as follows:
1. Allows the customer to register with their mobile number, name and regular address
(home or work)
2. Allows the customer to view and find the coffee shops closest to them
3. Place an online order for a coffee from the menu
Version: 1.1 All Copyrights 101 Digital PTE LTD 2019 - 2024 Page 3
4. See their position in the queue (and expected waiting time before collecting the coffee)
5. Exit the queue at any time (and notify the shop to cancel the order)
6. Any other function that you may consider useful.
Version: 1.1 All Copyrights 101 Digital PTE LTD 2019 - 2024 Page 4
3 Deliverables
The following section outlines what you should deliver to 101 Digital.
3.1 Part 1
This section outlines the requirements for the Customer App.
1. Create a solution design to address the requirements described for the Customer App.
Make use of Use Cases, Concept Diagrams, Sequence Diagrams, Data Designs, Data
Flows or any other forms of diagrams needed to describe your design
2. You do NOT need to design and build any of the Apps
3. Specify what coding, naming, security, technology standards you will follow when
building the solution
4. Outline the security solution that you intend to use
5. Outline the API endpoints that will be developed as part of the solution
6. Outline how you will test the solution without an App/front-end.
3.2 Part 2
1. Build the Process Order Service (The processing of an order placed by a customer) for
the solution outlined in Part 1 (only APIs and test / demo scripts are required) to
demonstrate your capability
2. Use Java Spring Boot framework to build the APIs
3. Setup sample data needed to demonstrate the APIs
4. Use Liquibase for setting up and managing database tables
5. Using test scripts demonstrate the APIs
6. Demonstrate how errors are detected and handled by the solution