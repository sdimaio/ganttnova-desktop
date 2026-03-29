# Build And Verification

Data: 2026-03-29

## Problemi iniziali osservati

### Build failure 1

`ant -f openproj_build/build.xml compile`

Errore iniziale:

- `Source option 5 is no longer supported. Use 8 or later.`
- `Target option 5 is no longer supported. Use 8 or later.`

### Build failure 2

Dopo l'aggiornamento di `source/target`, la compilazione si fermava su sorgenti Latin-1:

- `unmappable character ... for encoding UTF-8`

### Build failure 3

Dopo aver fissato l'encoding, la compilazione si fermava su JAXB:

- `package javax.xml.bind does not exist`

## Interventi eseguiti

1. Parametrizzazione della build Ant:
   - `javac.source=8`
   - `javac.target=8`
   - `javac.encoding=ISO-8859-1`
   - `javac.includeantruntime=false`

2. Aggiornamento di tutti i `build.xml` per usare le proprieta sopra.

3. Aggiunta di `jaxb-api` al classpath bundled:
   - `openproj_contrib/lib/jaxb/jaxb-api.jar`
   - inclusione nel jar `openproj-contrib.jar`

## Verifiche finali

### Compilazione

Comando:

- `ant -f openproj_build/build.xml compile`

Esito:

- `BUILD SUCCESSFUL`

### Packaging

Comando:

- `ant -f openproj_build/build.xml dist`

Esito:

- `BUILD SUCCESSFUL`
- artefatto generato: `openproj_build/dist/projectlibre.jar`

### Build Maven

Comandi:

- `mvn test`
- `mvn package -DskipTests`
- `mvn clean test package -DskipTests=false`

Esito:

- `BUILD SUCCESS`
- test scheduling verdi
- test risorse classpath verdi
- artefatto applicativo generato: `projectlibre-app/target/projectlibre-app-1.6.2-modernized-SNAPSHOT.jar`
- dipendenze runtime copiate in `projectlibre-app/target/lib`

### Controlli aggiuntivi

- verificata la presenza delle classi JAXB API dentro `openproj_contrib/openproj-contrib.jar`;
- verificata la presenza dei file di configurazione e stringhe nel jar finale.
- verificato il manifest del jar Maven con `Main-Class: com.projity.main.Main`;
- verificata la presenza del bundle runtime JAXB e di `FlatLaf` nella directory `target/lib`.
- verificata la presenza di `license/index.html` e `license/third-party/index.html` nel jar Maven finale.

## Verifica runtime

### Problema osservato sulla build precedente

Schermata ricevuta dal run utente:

- `ai/Schermata a 2026-03-29 11-38-22.png`

Sintomo:

- popup `Could not load license. Please try again later. Program will exit.`

Diagnosi:

- il jar Maven impacchettava i file licenza nel path sbagliato;
- `LicenseDialog` trattava l'assenza della pagina HTML come errore fatale e invocava `System.exit(-1)`.

### Correzioni runtime

- risorse licenza spostate correttamente sotto `license/` nel classpath Maven;
- `LicenseDialog` reso tollerante alle risorse mancanti;
- popup legacy di licenza e registrazione disabilitati di default allo startup modernizzato;
- update checker legacy disabilitato di default;
- script di lancio aggiunto: `./run-projectlibre.sh`.

### Esecuzione verificata

Comando:

- `./run-projectlibre.sh`

Esito:

- l'applicazione si avvia senza uscire sul gate di licenza;
- compare la welcome dialog e si puo creare un progetto;
- la vista Gantt mostra la colonna `Effort`;
- il fix `Add-Opens` evita la NPE runtime emersa nel parsing Groovy delle action list.

Screenshot di verifica:

- `ai/2026-03-29_runtime-screen.png`
- `ai/2026-03-29_runtime-after-enter.png`
- `ai/2026-03-29_runtime-after-new-project.png`
- `ai/2026-03-29_runtime-after-groovy-fix.png`

## Nota tecnica importante

Il repository ora:

- compila;
- impacchetta;
- si avvia nella build Maven modernizzata;
- include un percorso Maven reale con runtime JAXB esplicito.

La parte ancora incompleta non e piu il build, ma la modernizzazione del codice:

- restano API deprecate o prossime alla rimozione;
- resta un bridge Ant nel modulo Maven `thirdparty`;
- resta da ripulire la configurazione menu legacy che stampa gli `Invalid item` allo startup.
