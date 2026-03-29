# Scheduling Tests

Data: 2026-03-29

## Obiettivo

Validare che la nuova semantica `effort` non sia solo una colonna UI, ma un alias coerente di `work` nel motore di scheduling.

## Approccio

Prima di scrivere i test Maven sono state eseguite prove interattive con `jshell` contro il build reale:

- inizializzazione standalone con `Environment.setStandAlone(true)`;
- bootstrap configurazione con `Init.initialize()`;
- creazione progetto locale con `ProjectFactory` e `CreateOptions`;
- creazione task reale con `project.newNormalTaskInstance(false)`.

Questo ha confermato che:

- il task ha un default assignment valido;
- `setEffort()` aggiorna davvero il work sottostante;
- `getEffort()` restituisce correttamente giorni/uomo;
- con scheduling `FIXED_WORK`, l'aumento di effort espande anche la durata.

## Test aggiunti

File:

- `projectlibre-app/src/test/java/com/projity/pm/task/NormalTaskEffortTest.java`

Copertura introdotta:

1. `setEffortStoresUnderlyingWorkInPersonDays`
   - verifica che `setEffort(5 giorni)` porti il work a 5 giorni;
   - verifica che `getEffort()` ritorni 5 giorni;
   - verifica che il campo non sia read-only nel caso normale.

2. `effortAliasAlwaysReturnsDaysEvenWhenWorkDisplayUsesHours`
   - forza il display del work in ore;
   - verifica che `effort` continui a essere restituito come `TimeUnit.DAYS`.

3. `fixedWorkSchedulingStillRecalculatesDurationFromEffort`
   - imposta il task in `SchedulingType.FIXED_WORK`;
   - aumenta l'effort a 3 giorni;
   - verifica work e duration coerenti a 3 giorni.

## Esito

Comando:

- `mvn test`

Esito:

- 3 test eseguiti
- 0 failure
- 0 error
- 0 skipped

## Conclusione

La gestione `effort` in giorno/uomo e tecnicamente sostenibile e gia verificata sul motore.

Il limite attuale non e concettuale ma di prodotto:

- serve estendere il coverage a task con assegnazioni multiple;
- serve coprire effort-driven, fixed-duration e casi con actual work;
- serve aggiungere test di regressione su import/export quando `effort` viene esposto in UI e serializer.
