# Spring Boot + Cloud Foundry Heap Dump Examples

## References
https://community.pivotal.io/s/article/how-to-generate-and-download-java-application-heap-dump-from-cloud-foundry-container?language=en_US

## Methods

### OOM Heapdumps with the Java Kill Agent

To begin, ensure your app will be bound to a volume service with the string `heap-dump` in its name or tag. Terminal heap dumps will be written to it with the pattern:

```
<CONTAINER_DIR>/<SPACE_NAME>-<SPACE_ID[0,8]>/<APPLICATION_NAME>-<APPLICATION_ID[0,8]>/<INSTANCE_INDEX>-<TIMESTAMP>-<INSTANCE_ID[0,8]>.hprof
```

Service binding example (this is also taken care of in `manifest.yml`):

```
cf bind-service jbp-oom-example heap-dump -c '{"mount":"/var/heap-dump"}'
```

You should see the following logged on startup:

```
Write terminal heap dumps to /var/heap-dump/playground-9d67e169/jbp-oom-example-7728eab2/$CF_INSTANCE_INDEX-%FT%T%z-${CF_INSTANCE_GUID:0:8}.hprof
```

Next, trigger an OOM conditon by POSTing to the `/oom` endpoint:
```
curl -X POST https://jbp-oom-example.yourdomain.com/oom
```

You should see the following in the logs indicating a heapdump was captured:
```
2020-06-16T14:16:21.11-0500 [APP/PROC/WEB/0] OUT Heapdump written to "/var/heap-dump/playground-9d67e169/jbp-oom-example-7728eab2/0-2020-06-16T19:16:20-0000-ecf6826b.hprof"
...
...
2020-06-16T14:16:21.12-0500 [APP/PROC/WEB/0] ERR jvmkill killing current process
```

### Ad-Hoc Heapdumps with Spring Boot Actuator

The easiest option is to use Spring Boot Actuators. For Spring Boot apps with actuators enabled, a heap dump is available at the `/actuator/heapdump` endpoint in Spring Boot 2.x. Please refer details at [Spring Boot v2.1.x.RELEASE - Part V. Spring Boot Actuator: Production-ready features](https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/production-ready-endpoints.html).

#### Enable Spring Boot Actuator
In `build.gradle` add the `spring-boot-starter-actuator` dependency:
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    ...
}
```

#### Enable the `/actuator/heapdump` Endpoint
In `application.yml`, add the following:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,heapdump
  endpoint:
    heapdump:
      enabled: true
```

#### Obtain the Heap Dump
You can either navigate to the `/actuator/heapdump` endpoint in a browser to download or output it via curl. For example:
```sh
curl 'http://localhost:8080/actuator/heapdump' -O
```

## Building

```
./gradlew assemble
```
