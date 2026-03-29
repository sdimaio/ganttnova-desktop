# Final Summary

Data: 2026-03-29

## Cosa e stato fatto

- build Ant modernizzata per JDK attuali;
- build Maven introdotta con un reactor a due moduli;
- encoding di compilazione esplicitato;
- API JAXB reintrodotta nel bundle di dipendenze;
- runtime JAXB gestito in Maven;
- nuovo campo `Field.effort` aggiunto al modello task;
- colonna `effort` aggiunta alla vista Gantt di default;
- scheduling aggiornato per reagire anche a `Field.effort`;
- test automatici sul motore di scheduling aggiunti;
- test automatico sulle risorse runtime del classpath aggiunto;
- primo refresh UI su look-and-feel, font e bootstrap;
- startup runtime sbloccato eliminando il crash sulla licenza legacy;
- compatibilita Groovy su JDK moderni ripristinata con `Add-Opens` per evitare NPE nella Gantt view;
- script `run-projectlibre.sh` aggiunto per avvio rapido;
- distinzione documentata e confermata tra capacita risorsa (`Unita massime`) e allocazione task (`me[50%]`);
- `Durata` non piu marcata come stimata quando deriva da `effort/work` espliciti;
- preferenza utente persistente per il `Look&Feel` aggiunta nella UI;
- regressione bootstrap del ribbon corretta definendo le action base menu richieste dal registry legacy;
- documentazione tecnica completa salvata in `ai/`.

## Risultato

- `ant -f openproj_build/build.xml compile` -> successo
- `ant -f openproj_build/build.xml dist` -> successo
- `mvn test` -> successo
- `mvn package -DskipTests` -> successo
- `mvn clean test package -DskipTests=false` -> successo
- `mvn test package -DskipTests=false` -> successo
- artefatto disponibile in `openproj_build/dist/projectlibre.jar`
- artefatto Maven disponibile in `projectlibre-app/target/projectlibre-app-1.6.2-modernized-SNAPSHOT.jar`
- avvio verificato con `./run-projectlibre.sh`
- screenshot runtime salvati in `ai/2026-03-29_runtime-screen.png`, `ai/2026-03-29_runtime-after-new-project.png` e `ai/2026-03-29_runtime-after-groovy-fix.png`

## Conclusione

Si, la gestione `effort` in giorno/uomo era possibile e ora e integrata in modo coerente con il motore esistente.

Il porting a Java 25 non e concluso in senso architetturale definitivo, ma la base e stata realmente sbloccata:

- il progetto ora compila e si impacchetta con toolchain moderne;
- il progetto ora ha anche un percorso Maven reale per build e packaging;
- esiste una prima base di test automatici sul cuore dello scheduling;
- l'app desktop ora si avvia nella build modernizzata senza il blocco licenza che la faceva uscire;
- l'app desktop ora supera anche il parsing Groovy delle action list su JDK moderni;
- la colonna `effort` e visibile nella vista Gantt runtime;
- l'inserimento `me[50%]` continua a pilotare correttamente l'elapsed, ma ora la durata derivata viene anche approvata senza `?`;
- l'utente puo scegliere un `Look&Feel` persistente senza intervenire a mano su `UIManager`;
- i principali debiti tecnici iniziali sono stati identificati e documentati;
- esiste una roadmap concreta per proseguire verso un prodotto piu avanzato.

Il lavoro utile da fare dopo questo step e chiaro:

1. smontare gradualmente il bridge `thirdparty` e sostituire i jar legacy con dipendenze Maven esplicite;
2. aumentare il coverage del motore su assegnazioni multiple, actual work e import/export;
3. fare un redesign piu aggressivo delle schermate principali invece di limitarsi al solo look-and-feel.
