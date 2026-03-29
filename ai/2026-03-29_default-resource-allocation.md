# Percentuale Di Allocazione Di Default

## Obiettivo

Aggiungere nella tabella Risorse una colonna distinta da `Unita massime` che rappresenti la percentuale di allocazione di default della risorsa sul task.

Semantica scelta:

- `Unita massime`: capacita teorica della risorsa.
- `Percentuale di allocazione`: default di assegnazione quando nel Gantt si inserisce solo il nome della risorsa.

Esempio:

- Risorsa `me`
- `Percentuale di allocazione = 50%`
- Nel Gantt scrivo `me`
- Il parser crea l'assegnazione come `me[50%]`

Se il PM scrive `me[75%]`, il valore esplicito prevale sul default.

## Decisioni Tecniche

- Ho aggiunto il dato persistente `defaultAssignmentUnits` sulla risorsa.
- Ho mantenuto il default a `100%`.
- Ho normalizzato i vecchi file serializzati: se il campo non esiste o risulta `0`, viene riportato a `100%`.
- Ho lasciato il comportamento delle risorse materiali invariato.
- Ho nascosto la colonna per le risorse non lavoro.

## Impatto Sul Motore Di Scheduling

Il motore non e stato riscritto. Ho fatto in modo che la nuova informazione arrivi al motore nel punto corretto:

- il parser della colonna `Nome risorsa` usa ora il default della risorsa quando mancano le parentesi quadre;
- l'assegnazione nasce gia con le `units` corrette;
- il calcolo esistente `work/effort + units -> duration` continua a fare il suo lavoro.

Con questa scelta:

- `effort = 3 gg/uomo`
- allocazione di default = `50%`

produce una durata di `6 giorni`, e il task non resta piu stimato con `?`.

## File Coinvolti

- `openproj_core/src/com/projity/pm/resource/EnterpriseResource.java`
- `openproj_core/src/com/projity/pm/resource/ResourceImpl.java`
- `openproj_core/src/com/projity/pm/resource/ResourceSpecificFields.java`
- `openproj_core/src/com/projity/pm/assignment/AssignmentFormat.java`
- `openproj_core/src/com/projity/configuration/configuration.xml`
- `openproj_core/src/com/projity/configuration/view.xml`
- `projectlibre-app/src/test/java/com/projity/pm/task/ResourceDefaultAllocationTest.java`

## Verifica

Comandi eseguiti:

- `mvn -pl projectlibre-app -am -Dtest=ResourceDefaultAllocationTest,NormalTaskEffortTest -Dsurefire.failIfNoSpecifiedTests=false test`
- `mvn package -DskipTests=false`
- `timeout 12s ./run-projectlibre.sh`

Esito:

- test verdi;
- packaging verde;
- launcher vivo fino al timeout, senza crash di startup.
