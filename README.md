# Spring Boot Batch Project

## Description
This project is a Spring Boot developed batch processing application, using Spring Batch for the management and execution of batch jobs. It is designed to be a basic and extensible example of a batch job, capable of processing both CSV and Excel files.

## Requirements
- Java 17
- Maven
- Oracle Database (or any other database of your choice)

## Configuration
Configure the database connection in `src/main/resources/application.properties` before running the application.

## Main Dependencies
- **Spring Boot Starter Batch**: Integration of Spring Batch.
- **Spring Boot Starter Data JPA**: Database access.
- **Spring Boot Starter Web**: RESTful web services.
- **Lombok**: Reducing boilerplate in models and logs.
- **Apache POI**: Handling Excel files.
- **Ehcache**: Caching.
- **Hibernate JCache**: Integration of JPA and Ehcache.
- **OpenCSV**: Reading and writing CSV files.

## Execution
Run the project with:
```bash
mvn spring-boot:run

```

## Project Structure
- **src/main/java**: Source code.
- **src/main/resources**: Configuration files and resources.
- **src/test/java**: Project tests.

## Testing
- **Run tests with:**:
```bash
mvn test
```

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/#build-image)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#howto.batch)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#using.devtools)

## Guides
The following guides illustrate how to use some features concretely:

* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

## Autor
Enrique Valdivia

## Licencia
This project is under the MIT License. You are free to use and modify it, as long as you acknowledge Enrique Valdivia as the original creator.

## Notas Adicionales
Customize and expand this project according to the needs of your application.
