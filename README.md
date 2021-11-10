# ilicache-web-service

```
java -jar /Users/stefan/apps/ili2h2gis-4.6.0/ili2h2gis-4.6.0.jar --dbfile ilicache --defaultSrsCode 2056 --createFk --createFkIdx --createGeomIdx --strokeArcs --models SO_AGI_Ilicache_20211109 --modeldir "." --createUnique --createEnumTabs --beautifyEnumDispName --createMetaInfo --createNumChecks --schemaimport
```

```
./mvnw cayenne:cdbimport --settings .mvn/custom-settings.xml
```

## TODO
- Tests:
 * Wie konfigurieren wegen DB?
- ilisite.xml für geklonte Repos
- lastRun etc in DB schreiben
- static files exponieren