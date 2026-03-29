# GanttNova Desktop

`GanttNova Desktop` e un fork modernizzato di ProjectLibre/OpenProj per uso
desktop.

L'obiettivo non e fingere che questo codice sia stato riscritto da zero.
L'obiettivo e trasformare uno storico planner Java desktop, ormai invecchiato,
in una base buildabile, eseguibile e modernizzabile sui JDK correnti.

Questo repository e mantenuto da Simmaco Di Maio.

Non e una release ufficiale di ProjectLibre e non e affiliato a
ProjectLibre Inc. o agli upstream maintainer.

La base pubblica del fork usata in questa modernizzazione e
[`smartqubit/projectlibre`](https://github.com/smartqubit/projectlibre).

## Stato

Stato attuale: sperimentale, ma gia utilizzabile come fork modernizzato.

Cosa c'e gia:

- reactor Maven e packaging dipendenze piu pulito
- fix runtime per JDK moderni
- colonna `effort` esposta in gg/uomo
- percentuale di allocazione di default sulla risorsa, riusata dal parser Gantt
- rimozione automatica del `?` dalla durata quando `effort` determina davvero
  la schedulazione
- preferenza `Look&Feel` sicura con riavvio
- primo `Task Inspector` per spiegare vincoli, dipendenze e driver del piano

## Perche Esiste

ProjectLibre continua ad avere valore perche un project planner desktop,
locale e offline, resta utile.

Quello che non ha valore e conservare per sempre ogni assunzione tecnica del
vecchio build legacy.

Questo fork esiste per:

- mantenere vivo il caso d'uso desktop locale
- far funzionare il codice su JVM correnti
- migliorare la semantica di schedulazione dove la UX originale e debole
- preparare una base piu pulita per UI e motore

## Focus Corrente

Il lavoro segue una linea pragmatica:

- prima stabilizzare build e packaging
- poi rimuovere i blocchi runtime sui JDK moderni
- migliorare `effort`, allocazione e durata
- aggiungere spiegabilita alla schedulazione
- modernizzare la UI in modo incrementale, senza un rewrite ad alto rischio

## Build Ed Esecuzione

Build completa con test:

```bash
mvn package -DskipTests=false
```

Avvio dell'applicazione:

```bash
./run-projectlibre.sh
```

Jar generato:

```text
projectlibre-app/target/projectlibre-app-1.6.2-modernized-SNAPSHOT.jar
```

## Cambiamenti Gia Introdotti

- `Effort` in gg/uomo come campo task reale.
- Percentuale di allocazione di default sulla tabella risorse.
- In Gantt `me` puo diventare automaticamente `me[50%]`.
- La durata non resta piu stimata quando fixed work e units la determinano.
- Un primo `Task Inspector` spiega predecessori, vincoli, deadline,
  leveling delay, slack e impatto delle risorse.

## Roadmap

Priorita a breve:

- test piu profondi sul motore di scheduling
- cleanup orientato a JDK 25
- versione laterale persistente del `Task Inspector`
- evidenziazione visuale del task path e dei blocker
- modernizzazione UI oltre i default Swing legacy

## Note Legali E Attribuzione

Questo repository distribuisce un fork modificato del codice
ProjectLibre/OpenProj.

- l'attribuzione upstream resta importante ed e stata mantenuta
- la base pubblica del fork oggi tracciata e
  [`smartqubit/projectlibre`](https://github.com/smartqubit/projectlibre)
- gli header sorgente legacy continuano a richiamare CPAL e obblighi di
  attribuzione
- il repository non si presenta come upstream o release ufficiale
- le licenze terze parti restano sotto
  [`openproj_build/license/third-party/`](openproj_build/license/third-party/)
- le note tecniche e la storia del lavoro restano nel folder [`ai/`](ai/)

Vedi anche [NOTICE.md](NOTICE.md).

## Licenza

Questo repository contiene codice distribuito sotto `CPAL-1.0` insieme a
componenti open source terzi elencati nei notice inclusi.

Riferimenti:

- [LICENSE](LICENSE)
- [NOTICE.md](NOTICE.md)
- [`openproj_build/license/third-party/`](openproj_build/license/third-party/)
