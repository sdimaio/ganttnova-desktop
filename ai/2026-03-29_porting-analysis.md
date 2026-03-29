# Porting Analysis

Data: 2026-03-29

## Obiettivo

Portare ProjectLibre verso una base tecnologica compatibile con toolchain Java moderne, migliorando al tempo stesso il prodotto con una funzione richiesta dagli utenti: una colonna `effort` espressa in giorno/uomo.

## Decisioni prese

1. Non ho tentato una riscrittura totale.
   Un "vero porting tecnologico" credibile, in questo repository, parte dal rendere compilabile e impacchettabile l'applicazione esistente con una toolchain moderna. La riscrittura integrale del motore di scheduling e della UI Swing in un solo passaggio avrebbe alzato enormemente il rischio senza produrre un rilascio utile.

2. Ho trattato `openproj_*` come runtime primario.
   Il motore realmente usato dall'applicazione resta centrato sui package `com.projity.*`. I moduli `projectlibre_*` esistono, ma non sostituiscono ancora il core storico e convivono con esso.

3. Ho sbloccato la build moderna prima di estendere le funzioni.
   La build era bloccata da `source/target 1.5`, encoding legacy e dipendenze JAXB non più presenti nel JDK. Questi erano i veri blocker iniziali del porting.

4. Ho implementato `effort` come alias operativo di `work`.
   La pianificazione interna continua a ragionare in `work`, ma l'utente vede e modifica una colonna in giorno/uomo. Questo minimizza il rischio di regressioni perché non duplica il dato di scheduling.

## Debolezze strutturali rilevate

- Build legacy basata su Ant/Eclipse, con proprietà hardcoded e packaging non modulare.
- Repository misto `openproj_*` / `projectlibre_*`, con sovrapposizione di responsabilità.
- Forte accoppiamento fra UI Swing, field dictionary, scheduling engine ed exchange layer.
- Presenza di sorgenti in encoding Latin-1.
- Dipendenza da API rimosse dal JDK moderno (`javax.xml.bind`).
- Assenza di una suite di test automatizzati che protegga il comportamento del scheduling.
- Numerose librerie binarie vendorizzate localmente e non governate da un dependency manager.
- Architettura centrata su riflessione e XML di configurazione, con costo alto di manutenzione.

## Stato del porting dopo questo intervento

- Build resa compatibile con toolchain moderna almeno a livello di compilazione/pacchettizzazione Ant.
- Encoding di compilazione esplicitato.
- Dipendenza API JAXB reintrodotta nel classpath bundled.
- Nuovo campo `Field.effort` aggiunto al task model e alla vista Gantt di default.
- Packaging `dist` completato con successo.

## Roadmap raccomandata per un vero Java 25 target

### Fase 1

- Consolidare la build su Maven o Gradle.
- Introdurre CI con build ripetibile e smoke tests.
- Separare chiaramente core scheduling, UI, exchange e configuration.

### Fase 2

- Eliminare JAXB legacy oppure migrare a Jakarta XML Binding con dipendenze gestite.
- Normalizzare encoding e risorse in UTF-8.
- Ridurre la duplicazione fra `openproj_*` e `projectlibre_*`.

### Fase 3

- Aggiungere test deterministici per:
  - task type,
  - effort driven,
  - fixed work,
  - baselines,
  - critical path,
  - resource leveling.

### Fase 4

- Rinnovare la UI.
  La base Swing può ancora funzionare, ma oggi è un collo di bottiglia per produttività, accessibilità, theming e prestazioni percepite. La scelta giusta è isolare prima il dominio, poi decidere se restare su Swing modernizzato o migrare a JavaFX/web desktop shell.
