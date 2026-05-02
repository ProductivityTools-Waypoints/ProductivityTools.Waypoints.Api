

## Project initialization

![spring-intialize](./images/spring-intialize.png)

Edit the ```pom.xml``` and add:

```
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>spring-cloud-gcp-starter-data-firestore</artifactId>
</dependency>
```

## Run project
```
$env:JAVA_HOME="c:\Program Files\Java\jdk-24\"
.\gradlew.bat build 
.\gradlew.bat bootrun
```

## Graphql
Add depdencies in build.gradle
```
implementation 'org.springframework.boot:spring-boot-starter-graphql'
```
enable UI for graph ql ```application.properties```

```
spring.graphql.graphiql.enabled=true
```

## Basics
### Add home controller
```
package top.productivitytools.PdfTools.api.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @QueryMapping
    public String helloQuery() {
        return "Hello World";
    }

    @MutationMapping
    public String Hello(@Argument("name") String name) {
        var response = "Hello " + name;
        return response;
    }
}

```

Add graphql

```
type Mutation{
    Hello(name: String!):String
}

type Query {
    helloQuery: String
}
```

### Testing

```
http://localhost:8080/graphiql
```
```
query {
  helloQuery
}
```