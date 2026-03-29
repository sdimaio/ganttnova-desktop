# Effort, Duration Approval, And LookAndFeel Settings

Data: 2026-03-29

## Osservazione utente confermata

- `Unita massime` nella vista risorse non rappresenta la percentuale di allocazione "di quella risorsa su questo progetto".
- `Unita massime` descrive la capacita nominale della risorsa.
- L'allocazione effettiva sul singolo task viene invece gestita correttamente con la sintassi di assegnazione `me[50%]`.

Questa distinzione e coerente con il modello classico di Microsoft Project / ProjectLibre:

- capacita risorsa = quanto la risorsa puo offrire in generale;
- units assegnazione = quanta parte della risorsa viene consumata da quel task.

## Decisione tecnica

Non ho reinterpretato `Unita massime` come allocazione per progetto, perche romperebbe il modello esistente di assegnazioni e produrrebbe regressioni su scheduling, leveling e import/export.

Ho invece corretto il punto debole reale:

- quando l'utente inserisce un `effort` esplicito;
- e la durata viene ricalcolata in modo deterministico dal motore;
- la `Durata` non deve piu restare marcata come stimata (`?`).

## Modifica applicata

Nel motore task, dopo un aggiornamento reale di `work/effort`, la durata viene ora "approvata" e il flag `estimated` viene portato a `false`, salvo i casi di parse-only / scripting / no-update.

Effetto pratico:

- task con `Impegno = 3 gg/uomo`;
- assegnazione `me[50%]`;
- durata ricalcolata a `6 giorni`;
- il valore non resta piu con il punto interrogativo.

## UI settings

E stata aggiunta una voce di settings sul ribbon di `View` per scegliere il `Look&Feel`.

Scelte esposte:

- `Automatic (recommended)`
- `Modern light`
- `System`
- `Nimbus`
- `Metal`

La preferenza viene salvata nelle `Preferences` utente e l'app ricarica la finestra subito dopo la scelta, evitando stati Swing inconsistenti.

## Verifica

- `mvn test package -DskipTests=false` -> successo
- nuovo test su persistenza/validazione Look&Feel -> successo
- test su `effort -> duration` senza marker stimato -> successo
- `timeout 10s ./run-projectlibre.sh` -> processo avviato correttamente, terminato solo dal timeout

## Regressione intercettata e corretta

Durante la prima integrazione della voce ribbon `Look&Feel`, l'app mostrava solo una finestra grigia e poi falliva con:

- `MissingListenerException: no listener for mainFrame (LookAndFeelAction, bundle: com.projity.pm.graphic.frames.GraphicManager)`

La causa non era il `Look&Feel` in se, ma il vecchio registry `MenuActionsMap`:

- l'action `LookAndFeelAction` veniva richiesta dal ribbon;
- ma il registry storico indicizza le action usando gli item base del menu;
- per `LookAndFeel` mancava la definizione base nel bundle menu, quindi la chiave restava nulla.

Correzione applicata:

- aggiunte le action base mancanti (`ProjectLibre`, `Palette`, `LookAndFeel`, `FullScreen`, `Refresh`) in `menuInternal.properties`;
- aggiunti i testi base in `menu.properties`;
- rigenerato il jar Maven.

Dopo la ricostruzione:

- i warning `Invalid item` spariscono;
- `./run-projectlibre.sh` non lancia piu la `MissingListenerException`;
- la finestra grigia iniziale non resta piu bloccata sul bootstrap fallito.

## Seconda correzione sul cambio tema

Nel log consegnato in `ai/errors.txt` non compariva un errore specifico del cambio tema a runtime: la parte finale mostrava invece un parse error separato sulla colonna risorse (`FieldParseException` su nomi risorsa).

Il blocco percepito sul menu `Appearance` e stato comunque trattato come difetto reale della UI:

- il comando eseguiva un restart completo della finestra subito dentro l'action Swing;
- questa strategia e troppo fragile per la combinazione legacy di Swing + ribbon + workspace restore;
- il risultato pratico era un freeze o una UI apparentemente bloccata durante il reload.

Correzione applicata:

- la scelta del `Look&Feel` viene ancora salvata nelle `Preferences`;
- non viene piu tentato il restart live della finestra;
- l'utente riceve un messaggio esplicito che il tema verra applicato al prossimo avvio.

Questa soluzione e meno ambiziosa ma molto piu robusta sul codice storico attuale.

## Limiti residui

- il sistema menu/action resta fragile e fortemente legacy, anche se i warning specifici intercettati in questo passaggio sono stati eliminati;
- la localizzazione italiana dei nuovi testi UI non e stata rifatta nello stesso passaggio per evitare di destabilizzare i bundle storici in encoding legacy;
- la prossima iterazione sensata e la pulizia del sistema menu/action, cosi il nuovo comando `Look&Feel` esce da una zona ancora molto fragile del codice storico.
