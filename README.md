# ilicache-web-service

```
java -jar /Users/stefan/apps/ili2h2gis-4.6.0/ili2h2gis-4.6.0.jar --dbfile ilicachedb --defaultSrsCode 2056 --createFk --createFkIdx --createGeomIdx --strokeArcs --models SO_AGI_Ilicache_20211109 --modeldir "." --createUnique --createEnumTabs --beautifyEnumDispName --createMetaInfo --createNumChecks --schemaimport
```

```
./mvnw cayenne:cdbimport --settings .mvn/custom-settings.xml
```

## TODO
- PK creation strategy with maven plugin?
- data source in project.xml (wird das dann überschrieben?)
- Tests:
 * Wie konfigurieren wegen DB? -> eigenes application-test.yml (falls nötig). Dann muss es annotiert werden.
 * Können die Repo in die DB geschrieben werden?
 * cloneRepositories() testen
- ilisite.xml für geklonte Repos
- lastRun etc in DB schreiben
- static files exponieren
- dstDir für cgen
- yml separieren (user only yml)
- CloneRepoService und Scheduler trennen, damit clonen ganz ohne Scheduler möglich wird (auch einzeln Repos).

## Run

### JVM

```
java -jar target/ilicache-web-service-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/application.yml,optional:file:/path/to/config.yml
```