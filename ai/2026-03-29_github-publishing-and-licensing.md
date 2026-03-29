# GitHub Publishing, Licensing And Naming

## Contesto

Obiettivo del turno: preparare questo fork per una pubblicazione seria su GitHub, replicando il metodo usato sul progetto `inxi-rs`:

- verificare il perimetro licenze;
- evitare una presentazione fuorviante verso l'upstream;
- scegliere un naming repository-side piu forte del vecchio `projectlibre`;
- rendere `README.md` e `doCommit.sh` adeguati a una pubblicazione pubblica.

## Fatti Verificati

### 1. Il codice e chiaramente derivato da ProjectLibre/OpenProj

Fatti locali:

- gli header di molti file sorgente richiamano esplicitamente la `Common Public Attribution License Version 1.0 (CPAL)`;
- gli stessi header impongono obblighi di attribuzione;
- il bundle licenze legacy e presente in `openproj_build/license/`.

Prove locali usate:

- header sorgente in `openproj_ui/src/com/projity/dialog/*.java` e molti altri file;
- testo licenza in `openproj_build/license/index.html`;
- notice terze parti in `openproj_build/license/third-party/`.

### 2. ProjectLibre desktop e ancora presentato ufficialmente come desktop open source

Fonte ufficiale ProjectLibre:

- https://www.projectlibre.com/sites/default/files/document/2023-08/ProjectLibre%20Cloud%20Document%20%281%29.pdf
- https://www.projectlibre.com/2025/05/15/stop-dreaming-start-doing-unleash-your-projects-with-projectlibre-cloud-ai/

Conclusione pratica: usare il marchio `ProjectLibre` come nome primario del nuovo repository non e la scelta migliore per un fork indipendente, perche rende ambiguo il confine tra fork e prodotto ufficiale.

### 3. GitHub premia la chiarezza della licenza, ma non tutto e riconosciuto automaticamente

Fonte ufficiale GitHub:

- https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/licensing-a-repository

Verifica ulteriore fatta oggi:

- interrogando `https://api.github.com/licenses`, il catalogo GitHub non include `CPAL-1.0`.

Inferenza tecnica: anche con un file `LICENSE` corretto, GitHub potrebbe non mostrare il badge automatico di licenza come fa per MIT, Apache o GPL. Questo non rende la pubblicazione scorretta; significa solo che il README e il notice diventano ancora piu importanti.

## Decisione Presa

### Naming repository-side

Ho scelto:

- nome pubblico consigliato: `GanttNova Desktop`
- slug GitHub consigliato: `ganttnova-desktop`

Ragione:

- evita di usare `ProjectLibre` come marchio primario del fork;
- resta comprensibile per chi cerca un planner/Gantt desktop;
- permette un README forte che dica subito "modernized fork of ProjectLibre/OpenProj".

### Posizionamento

Linea scelta:

- brand nuovo sul repository;
- derivazione esplicita nel README e nel notice;
- nessuna pretesa di affiliazione o ufficialita;
- mantenimento delle attribuzioni legacy esistenti nel codice.

Questa e la soluzione piu prudente e piu seria per un fork CPAL.

## File Preparati

### README

Ho riscritto `README.md` per GitHub con:

- nuovo nome pubblico;
- descrizione chiara del fork;
- stato attuale;
- valore tecnico gia creato;
- istruzioni build/run;
- sezione legale e di attribuzione.

Ho aggiunto anche `README.it.md`.

### Notice

Ho aggiunto `NOTICE.md` per dichiarare in modo esplicito:

- che il repository e un fork modificato;
- che non e una release ufficiale;
- che l'attribuzione legacy resta preservata;
- che il naming `GanttNova Desktop` e repository-side.

### License

Ho aggiunto un file root `LICENSE` generato dal bundle CPAL locale usando il testo gia incluso in `openproj_build/license/index.html`.

Scopo:

- rendere visibile subito la licenza a chi entra dal repository;
- non dipendere solo dal bundle HTML legacy interno.

### GitHub helper

Ho adattato `doCommit.sh`:

- variabili ambiente rinominate per il nuovo progetto;
- remote di default impostato a `git@github.com:sdimaio/ganttnova-desktop.git`;
- chiave SSH ancora overrideabile via env.

## Cosa Non Ho Fatto Di Proposito

- non ho ribattezzato in massa package Java, artifact interni o stringhe legacy `ProjectLibre`;
- non ho toccato i notice sorgente storici;
- non ho cercato di "nascondere" l'origine del fork.

Motivo:

- tecnicamente sarebbe costoso e rischioso;
- legalmente sarebbe la direzione sbagliata;
- su GitHub paga di piu un fork dichiarato bene che un fork camuffato male.

## Consiglio Operativo

Per la pubblicazione GitHub, la combinazione giusta e:

1. creare il repository `sdimaio/ganttnova-desktop`;
2. usare come descrizione breve: `A modernized desktop scheduling fork of ProjectLibre/OpenProj for current JDKs.`;
3. lasciare il branch attuale `master` solo se vuoi continuita con il fork storico; altrimenti rinominarlo in `main` subito dopo la prima pubblicazione;
4. usare il nuovo `README.md` come pagina di atterraggio principale;
5. non rimuovere `NOTICE.md`, `LICENSE` e il bundle `openproj_build/license/third-party/`.

## Esito

Raccomandazione finale:

- pubblicare come `GanttNova Desktop`;
- mantenere chiaro nel README che e un fork modernizzato di ProjectLibre/OpenProj;
- accettare che il badge licenza GitHub possa non apparire automaticamente;
- puntare sulla qualita del README e sulla trasparenza legale, non sulla mimetizzazione del fork.
