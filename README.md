# talk-tracing-in-microservices
Codebase used in talk "Tracing in Microservices"

Follow steps below to run your own copy of the applications

## 1. Start Docker images
### Jaeger
    docker run -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5788:5788 -p 16686:16686 -p 14268:14268 -p 9411:9411 --env COLLECTOR_ZIPKIN_HTTP_PORT=9411 --name tracingdemo-jaeger --rm jaegertracing/all-in-one 

### Redis
    docker run -p 6379:6379 --name tracingdemo-redis --rm redis 

## 2. Start Applications
### Weather Service and Location Service
    mvn spring-boot:run
### Location Service (Quarkus)
    mvn quarkus:dev -Djvm.args=-DJAEGER_PROPAGATION=b3,jaeger
    
## 3. Making Requests
    curl -i http://localhost:8080/weather?ip=8.8.8.8
    curl -i http://localhost:8080/weather?city=kyiv
     
## Jaeger URL
http://localhost:16686/search

# Links
https://opentracing.io/

https://opencensus.io/

https://www.cncf.io/

https://www.jaegertracing.io/

https://zipkin.io/

https://github.com/openzipkin/b3-propagation

https://www.instana.com/
