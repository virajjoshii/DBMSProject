Overview
This project is part of the CS623 course and demonstrates the implementation of a robust database system using Java and PostgreSQL. The primary focus is on managing products, depots, and stock while ensuring that all database transactions adhere to ACID (Atomicity, Consistency, Isolation, Durability) principles.

Features
Product Management: Allows for adding, deleting, and updating product records.
Depot Management: Facilitates the addition, deletion, and modification of depot records.
Stock Management: Manages the stock levels of products at various depots.
ACID Compliance: Ensures that all transactions maintain atomicity, consistency, isolation, and durability.

Tables Structure
Prod: Stores product details with fields for product ID (prodId), name (pname), and price.
Depot: Contains depot information including depot ID (depId), address (addr), and volume.
Stock: Tracks stock quantities with fields for product code (pcode), depot code (dcode), and quantity.

Technologies Used
Java: Used for backend logic and database interaction.
PostgreSQL: Database system used for storing all data.
JDBC: Java Database Connectivity used for connecting to and manipulating the database.
