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
- Defaultlogo in Dockerfile kopieren

## Build
FIXME (aotTest?)

```
./gradlew nativeCompile -i
```

## Run

### JVM

FIXME

```
java -jar target/ilicache-web-service-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/application.yml,optional:file:/path/to/config.yml
```

## XSLT

### Develop

```
java -jar /Users/stefan/apps/SaxonHE10-6J/saxon-he-10.6.jar -s:ilimodels_mirror.xml -xsl:/Users/stefan/sources/ilicache-web-service/src/main/resources/ilimodels2html.xsl -o:out.html
```
