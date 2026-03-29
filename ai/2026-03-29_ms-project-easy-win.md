# Prossima Feature Facile Da Portare Da Microsoft Project

## Proposta

La funzione con miglior rapporto valore/sforzo da importare adesso e il `Task Inspector`.

## Perche Proprio Questa

Microsoft Project espone gia un pannello che spiega perche un task ha quella data, quella durata o quel blocco schedulativo. Dalle fonti ufficiali Microsoft, il pannello mostra fattori come:

- predecessori che guidano il task;
- vincoli e relativa data;
- leveling delay;
- calendario applicato;
- summary task che impatta la schedulazione;
- differenza fra task manuale e automatico.

Fonti ufficiali:

- https://support.microsoft.com/en-us/office/view-and-track-scheduling-factors-647becca-89da-4f58-a790-ba0ee8765683
- https://support.microsoft.com/en-us/office/highlight-how-tasks-link-to-other-tasks-afad334a-6051-4b18-acdb-73b63a651b4d

## Perche E Realistica In ProjectLibre

Inferenza tecnica sul codebase locale:

- il motore ha gia dipendenze, vincoli, calendari, effort/work, units e critical path;
- il problema oggi non e la mancanza dei dati, ma la loro spiegazione in UI;
- quindi una prima versione puo essere un pannello laterale testuale, senza toccare il core scheduler.

## MVP Consigliato

1. Selezioni un task.
2. Si apre un pannello `Perche questa data?`.
3. Mostra:
   - predecessore driver;
   - vincolo;
   - calendario;
   - allocazione risorse e effort;
   - eventuale ritardo da leveling;
   - motivo per cui la durata aumenta o la fine slitta.

## Motivo Per Cui Lo Ritengo Piu Facile Di Altre Feature

E piu semplice di:

- resource engagements enterprise;
- portfolio management;
- planner/board/sprint views complete;
- automazioni cloud o copilot.

Qui possiamo riusare il motore esistente e costruire sopra un layer esplicativo. E un salto di qualita molto visibile per il PM, senza dover rifondare l'architettura.
