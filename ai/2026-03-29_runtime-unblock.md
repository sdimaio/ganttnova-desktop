# Runtime Unblock

Data: 2026-03-29

## Input osservato

L'utente ha riportato che il programma "non parte" e ha salvato la schermata in:

- `ai/Schermata a 2026-03-29 11-38-22.png`

Il messaggio mostrato era:

- `Could not load license. Please try again later. Program will exit.`

## Diagnosi

Il problema non era la compilazione, ma il packaging/runtime della build Maven.

Cause identificate:

1. I file sotto `openproj_build/license` venivano copiati nel jar finale senza il prefisso `license/`.
2. `LicenseDialog` cercava `license/index.html` e `license/third-party/index.html`.
3. In caso di resource non trovata, il codice storico chiamava `System.exit(-1)`.

Questa combinazione spiega esattamente il popup visto nello screenshot utente.

## Decisioni prese

1. Correggere il packaging invece di aggirare solo il sintomo.
2. Rendere `LicenseDialog` non fatale, per evitare che una resource HTML legacy possa abbattere il desktop client.
3. Disabilitare di default i popup legacy di startup:
   - licenza
   - registrazione email
   - tip iniziale
4. Disabilitare di default anche il vecchio update checker, che punta a endpoint storici.
5. Aggiungere uno script di run semplice dal root del repository.

## Modifiche eseguite

- `projectlibre-app/pom.xml`
  - aggiunto `targetPath=license` per le risorse licenza.
- `openproj_ui/src/com/projity/dialog/LicenseDialog.java`
  - rimossa la terminazione fatale in caso di resource mancante;
  - introdotto fallback HTML innocuo.
- `openproj_ui/src/com/projity/pm/graphic/frames/StartupFactory.java`
  - popup legacy disattivati di default;
  - mantenuta la possibilita di riabilitarli via system property.
- `openproj_core/src/org/openproj/util/UpdateChecker.java`
  - update checker disattivato di default.
- `run-projectlibre.sh`
  - script di esecuzione dal root repo.
- `projectlibre-app/src/test/java/org/projectlibre/modernization/ClasspathResourcesTest.java`
  - test automatico sulle risorse `license/...` nel classpath.

## Verifica eseguita

Build:

- `mvn clean test package -DskipTests=false` -> `BUILD SUCCESS`

Run:

- `./run-projectlibre.sh`

Esito runtime verificato:

- nessun popup fatale di licenza;
- welcome dialog visibile;
- creazione progetto riuscita;
- vista Gantt aperta.

Screenshot di conferma:

- `ai/2026-03-29_runtime-screen.png`
- `ai/2026-03-29_runtime-after-enter.png`
- `ai/2026-03-29_runtime-after-new-project.png`

## Secondo difetto runtime emerso

Dopo il primo fix, creando un progetto compariva una NPE nella vista Gantt:

- `CommonSpreadSheetModel.getActionList(...)`
- `GraphicManager.setButtonState(...)`

Log osservato:

- `Formula not set: invalid formula text: return "Indent,Outdent,InsertTask,Delete,Copy,Cut,Paste,Expand,Collapse";`

Diagnosi puntuale:

- `ActionListFactory` usa `GroovyClassLoader`;
- su JDK moderni Groovy legacy va in `InaccessibleObjectException` su `java.lang.Object.finalize()`;
- il parser delle formule fallisce;
- l'`ActionList` resta `null`;
- la griglia va in NPE quando aggiorna lo stato dei pulsanti.

## Fix Groovy/JDK

Interventi eseguiti:

- aggiunto `--add-opens java.base/java.lang=ALL-UNNAMED` allo script `run-projectlibre.sh`;
- aggiunto `Add-Opens: java.base/java.lang` nel manifest del jar Maven;
- aggiunto `argLine` equivalente nel `maven-surefire-plugin`;
- aggiunto test automatico:
  - `projectlibre-app/src/test/java/com/projity/graphic/configuration/ActionListFactoryCompatibilityTest.java`

Verifica:

- il test di compatibilita Groovy passa;
- il percorso `welcome -> new project -> gantt` non produce piu la NPE immediata;
- la vista Gantt resta aperta con la colonna `Effort` visibile.

Screenshot aggiornato:

- `ai/2026-03-29_runtime-after-groovy-fix.png`

## Impatto sul requisito `effort`

La verifica runtime mostra che la colonna `Effort` e presente nel Gantt di default della build nuova.

Nota:

- nello screenshot il titolo e troncato per larghezza colonna, ma il campo e effettivamente presente e operativo.
- nella localizzazione italiana e stato registrato come `Impegno (gg/uomo)`.
