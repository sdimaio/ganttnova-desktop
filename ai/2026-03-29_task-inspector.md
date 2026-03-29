# Task Inspector MVP

## Obiettivo

Portare in ProjectLibre una prima funzione semplice ma utile che manca rispetto a Microsoft Project: un `Task Inspector` capace di spiegare perche una task finisce in una certa data.

L'idea nasce dal confronto con le funzioni ufficiali Microsoft su fattori di schedulazione e dipendenze:

- https://support.microsoft.com/en-us/office/view-and-track-scheduling-factors-647becca-89da-4f58-a790-ba0ee8765683
- https://support.microsoft.com/en-us/office/highlight-how-tasks-link-to-other-tasks-afad334a-6051-4b18-acdb-73b63a651b4d

Nota: la scelta di implementare prima questa funzione e una mia inferenza tecnica basata sulle fonti ufficiali Microsoft e sulla struttura attuale del codice ProjectLibre. Il motivo e che i dati necessari esistono gia nel motore; mancava soprattutto il layer di spiegazione in UI.

## Decisione Di Prodotto

Invece di introdurre subito un pannello dockable o una vista laterale nuova, ho scelto un MVP a rischio basso:

- nuova action `TaskInspector` nel ribbon Task;
- dialogo testuale scrollabile;
- contenuto costruito sui dati reali del task selezionato;
- nessun ricalcolo custom del piano: l'Inspector legge e spiega il modello esistente.

Questo approccio ha tre vantaggi:

- costo di integrazione basso nel bootstrap Swing legacy;
- zero impatto sul motore di scheduling;
- utilita immediata per capire vincoli, predecessori, effort, allocazioni e slack.

## Contenuto Del Report

Il report mostra quattro sezioni:

- `Task`: nome e WBS.
- `Schedule`: start, finish, duration, effort, work, resource names, task type, effort driven, calendar, constraint, deadline, leveling delay, total/free slack.
- `Dependencies`: predecessori, successori e dettaglio leggibile dei link con tipo relazione e lag.
- `Scheduling factors`: fattori descrittivi e driver esterni della schedulazione.

Distinzione importante:

- fattori descrittivi: milestone, summary roll-up, durata stimata, risorse assegnate;
- driver esterni: predecessori, vincoli espliciti, deadline, task calendar override, leveling delay.

Se non ci sono driver esterni, il report lo dice esplicitamente invece di sembrare vuoto.

## Implementazione

File principali:

- `openproj_ui/src/com/projity/pm/graphic/frames/TaskInspector.java`
- `openproj_ui/src/com/projity/pm/graphic/frames/GraphicManager.java`
- `openproj_ui/src/com/projity/menu/MenuActionConstants.java`
- `openproj_ui/src/com/projity/menu/menuInternal.properties`
- `openproj_ui/src/com/projity/menu/menu.properties`
- `openproj_core/src/com/projity/strings/client.properties`
- `openproj_core/src/com/projity/strings/client_it.properties`
- `projectlibre-app/src/test/java/com/projity/pm/graphic/frames/TaskInspectorTest.java`

Scelte tecniche rilevanti:

- riuso dei `Field` esistenti per formattazione coerente e localizzata;
- nessuna duplicazione di logica di schedulazione;
- dettaglio dipendenze costruito con `DependencyType.toLongString(...)` e `DurationFormat.format(...)`;
- supporto pieno al nuovo comportamento `effort + allocazione default risorsa`, quindi nel report compare anche `me[50%]`.

## Stato Attuale

Funziona cosi:

- selezioni una task o una assignment;
- premi `Inspector` nel ribbon Task;
- ottieni una spiegazione compatta dello stato schedulato corrente.

Limitazioni volute di questa prima iterazione:

- non e ancora un pannello persistente;
- non evidenzia graficamente il task path nel diagramma;
- la localizzazione del label ribbon in `menu_it.properties` non e stata toccata in questa iterazione perche il file e ancora in encoding legacy non UTF-8.

## Verifica

Comandi eseguiti:

- `mvn -pl projectlibre-app -am -Dtest=TaskInspectorTest,ResourceDefaultAllocationTest,NormalTaskEffortTest -Dsurefire.failIfNoSpecifiedTests=false test`
- `mvn package -DskipTests=false`
- `timeout 12s ./run-projectlibre.sh`

Esito:

- test verdi;
- packaging verde;
- launcher vivo fino al timeout, senza crash di startup.

## Prossimo Passo Sensato

La prosecuzione naturale e trasformare questo MVP in un inspector persistente:

- pannello laterale agganciato alla selezione;
- highlight visuale di predecessori/successori nel Gantt;
- spiegazione dedicata di `effort`, `allocazione`, `durata` e `finish` quando la task e `FIXED_WORK`.
