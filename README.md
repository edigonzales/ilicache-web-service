# ilicache-web-service

```
java -jar /Users/stefan/apps/ili2h2gis-4.6.0/ili2h2gis-4.6.0.jar --dbfile ilicachedb --defaultSrsCode 2056 --createFk --createFkIdx --createGeomIdx --strokeArcs --models SO_AGI_Ilicache_20211109 --modeldir "." --createUnique --createEnumTabs --beautifyEnumDispName --createMetaInfo --createNumChecks --schemaimport
```

```
./mvnw cayenne:cdbimport (--settings .mvn/custom-settings.xml)
```


```
./gradlew clean deleteCayenneConfig cdbimport cgen
./gradlew clean cdbimport cgen
```

 isGenerated="true" 

## TODO
- PK creation strategy with maven plugin?
- data source in project.xml (wird das dann überschrieben?)
- Tests:
 * Wie konfigurieren wegen DB? -> eigenes application-test.yml (falls nötig). Dann muss es annotiert werden.
 * Können die Repo in die DB geschrieben werden?
 * cloneRepositories() testen
- (static files exponieren). Mal schauen, ob noch alles so funktioniert wie es sollte. Insbesondere, wenn die Anwendung selber auch static resources hat.
- dstDir für cgen
- CloneRepoService und Scheduler trennen, damit clonen ganz ohne Scheduler möglich wird (auch einzeln Repos).
- In "Prod"-Umgebung testen, d.h. mit Domain etc.x
- Pipeline
- Docker

## Run

### JVM

```
java -jar target/ilicache-web-service-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/application.yml,optional:file:/path/to/config.yml
```