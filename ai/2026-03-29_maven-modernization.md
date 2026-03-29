# Maven Modernization

Data: 2026-03-29

## Decisione architetturale

Invece di tradurre brutalmente il layout Ant esistente in un unico `pom.xml` monolitico, e stata scelta una struttura Maven a due moduli:

1. `projectlibre-thirdparty`
2. `projectlibre-app`

Questa scelta riduce il rischio:

- il modulo `thirdparty` incapsula il mondo legacy di `openproj_contrib`;
- il modulo `app` vede un solo artefatto interno invece di decine di jar storici sparsi;
- la build Maven resta leggibile e progressivamente migliorabile senza rompere la pipeline Ant gia sbloccata.

## Cosa fa ogni modulo

### `projectlibre-thirdparty`

- invoca `openproj_contrib/build.xml` via `maven-antrun-plugin`;
- costruisce `openproj-contrib.jar`, `openproj-script.jar`, `openproj-reports.jar`;
- riespone il contenuto di questi jar come unico artefatto Maven interno.

Questa non e ancora dependency management "perfetto", ma e molto piu pulito del layout precedente:

- la complessita legacy e confinata;
- l'applicazione non dipende piu direttamente da `lib/**/*.jar`;
- il passo successivo naturale e sostituire progressivamente i jar vendorizzati con coordinate Maven esplicite.

### `projectlibre-app`

- aggiunge come source roots i moduli legacy reali:
  - `projectlibre_core/src`
  - `openproj_core/src`
  - `openproj_ui/src`
  - `openproj_reports/src`
  - `projectlibre_exchange/src`
  - `openproj_exchange/src`
- copia tutte le risorse non Java;
- genera `com/projity/version/version.properties` via filtering;
- produce un jar avviabile con manifest `Main-Class`;
- copia le dipendenze runtime in `target/lib`.

## Dipendenze modernizzate

Le dipendenze introdotte esplicitamente in Maven sono:

- `org.glassfish.jaxb:jaxb-runtime`
- `com.formdev:flatlaf`
- `junit:junit`

Questa scelta risolve due problemi reali del porting:

- JAXB non deve piu essere solo "API presente per compilare", ma anche runtime disponibile per JDK moderni;
- la UI puo agganciarsi a un look-and-feel moderno senza introdurre dipendenze hard-coded nel codice legacy.

## Verifiche riuscite

Comandi eseguiti con successo:

- `mvn test`
- `mvn package -DskipTests`

Artefatti principali prodotti:

- `projectlibre-thirdparty/target/projectlibre-thirdparty-1.6.2-modernized-SNAPSHOT.jar`
- `projectlibre-app/target/projectlibre-app-1.6.2-modernized-SNAPSHOT.jar`
- `projectlibre-app/target/lib/*`

## Debiti residui

Il porting Maven e reale, ma non definitivo. Restano aperti:

- molte API deprecated o marked-for-removal nel codice storico;
- uso diffuso di reflection e pattern pre-generics;
- dipendenze legacy ancora inglobate attraverso il bridge `thirdparty`;
- assenza di una distribuzione desktop completa con installer o launcher dedicati.

La prossima iterazione sensata e trasformare il modulo `thirdparty` da bridge Ant a modulo Maven con dipendenze esplicite, una per volta.
