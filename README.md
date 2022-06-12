# ilicache-web-service

## Features

## Non-Features
- Ein ilisite.xml pro geklontes Repository: Zur Zeit werden sämtliche Repositories in einem Rutsch heruntergeladen. D.h. es entsteht ein lokales, gespiegeltes Repository.
- Administration via GUI
- Einstellungen pro Repository: Z.B. unterschiedliche Klon-Häufigkeit für unterschiedliche Repos

## TODO
- Rename: ilimirror
- Tests
- Pipeline
- Docker

## Build
```
fixme ./gradlew aotTest generateAot nativeCompile
```

## Run

### JVM

```
java -jar target/ilicache-web-service-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/application.yml,optional:file:/path/to/config.yml
```

./gradlew bootJar
java -DspringAot=true -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar build/libs/ilicache-web-service-0.0.LOCALBUILD.jar
./gradlew nativeCompile