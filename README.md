product-crud-postgres/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/productcrud/
│   │   │       ├── ProductCrudApplication.java  
│   │   │       ├── entity/
│   │   │       │   └── Product.java             
│   │   │       ├── repository/
│   │   │       │   └── ProductRepository.java   
│   │   │       ├── service/
│   │   │       │   └── ProductService.java      
│   │   │       └── controller/
│   │   │           └── ProductController.java   
│   │   └── resources/
│   │       └── application.properties           
├── pom.xml

- CREATE DATABASE productdb;
- Create folder included below dependencies and confirm those included in the pom.xml
  - Spring Web
  - Spring Data JPA
  - PostgreSQL Driver
-  Project：Maven, Language：Java
-  Setup the "src/main/resources/application.properties" and added below code
     spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
     spring.datasource.username=postgres
     spring.datasource.password=yourpassword
     spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
-  Create entity.jave eg. Product.java, Marked as a JPA entity, corresponding to the product table in PostgreSQL
-  Create Repository.jave in ".../productcrud/repository/ProductRepository.java" Provides standard CRUD methods, Long is the primary key type
-  Create Service.jave in ".../productcrud/service/ProductService.java" Encapsulate business logic and call Repository methods.
-  Create Controller.java in ".../productcrud/controller/ProductController.java" REST API endpoints, handling HTTP requests.
-  Main application in ".../productcrud/ProductCrudApplication.java"

Test API Method

###
POST http://localhost:8080/api/products
Content-Type: application/json

{"name":"MacBook Air","price":999.99,"description":"M2 chip, 13-inch Retina display"}

###
GET http://localhost:8080/api/products

###
GET http://localhost:8080/api/products/5

###
GET http://localhost:8080/api/products/name/MacBook%20Air

###
PUT http://localhost:8080/api/products/2
Content-Type: application/json

{"name":"iPhone 1","price":899.99,"description":"Apple iPhone 1 Pro with A16 Bionic chip"}

###
DELETE http://localhost:8080/api/products/3

###
GET http://localhost:8080/api/products/page?page=0&size=3

###
GET http://localhost:8080/api/products/price-range?minPrice=200.0&maxPrice=1000.0
