# Effort Column

Data: 2026-03-29

## Domanda

E' possibile aggiungere in ProjectLibre una colonna `effort` espressa in giorno/uomo?

Risposta breve: si.

## Scelta implementativa

Ho implementato `effort` come vista/edit alias del campo `work`:

- l'engine continua a salvare e schedulare `work`;
- la UI espone `effort` come `Duration` in giorni;
- il setter di `effort` scrive internamente su `work`;
- il formatter della nuova colonna mostra sempre valori in giorni, non nell'unita di lavoro globale.

## Perche questa scelta e corretta

- Evita di introdurre due sorgenti di verita (`work` e `effort`) che potrebbero divergere.
- Riusa le regole esistenti di scheduling.
- Mantiene compatibilita con import/export e campi gia presenti.
- Permette all'utente di ragionare in uomo-giorno senza alterare il motore.

## Comportamento atteso

- Se l'utente inserisce `5d` in `effort`, il task riceve 5 giorni/uomo di lavoro.
- La durata risultante dipende ancora da:
  - task type,
  - effort driven,
  - numero di risorse,
  - unita assegnate,
  - calendario.
- Su task senza assegnazioni labor, il campo resta di fatto non modificabile, coerentemente con `work`.

## Limiti attuali

- `effort` e un alias di `work`, non un nuovo dato persistente indipendente.
- La semantica e pensata per risorse labor; non ha senso forte su risorse materiali.
- Non ho introdotto ancora una preferenza globale dedicata al "person-day display mode": la nuova colonna serve proprio a offrire una vista fissa in gg/uomo.

## File toccati

- `openproj_core/src/com/projity/pm/task/NormalTask.java`
- `openproj_core/src/com/projity/configuration/configuration.xml`
- `openproj_core/src/com/projity/configuration/view.xml`
- `openproj_core/src/com/projity/pm/criticalpath/CriticalPathFields.java`
- `openproj_core/src/com/projity/strings/client.properties`
- `openproj_core/src/com/projity/strings/client_it.properties`
