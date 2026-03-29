# Microsoft Project Gap Analysis

Data di confronto: 2026-03-29

## Sintesi

ProjectLibre resta forte come desktop scheduler open source tradizionale, ma e nettamente indietro rispetto all'offerta Microsoft attuale su cinque fronti:

1. portfolio e demand management;
2. enterprise resource management;
3. reporting moderno e status automation;
4. AI/copilot/agent orchestration;
5. collaborazione cloud e UX multi-view.

## Gap principali

### 1. Portfolio e prioritarizzazione

Microsoft dichiara esplicitamente per Planner and Project Plan 5:

- demand management;
- enterprise resource management;
- portfolio analysis and optimization.

Nel repository corrente non esiste una vera area portfolio-level con scoring, scenario comparison, prioritizzazione investimenti o balancing cross-project.

### 2. Resource management enterprise

Microsoft espone una vista unica della capacita e disponibilita delle risorse a livello portfolio.

ProjectLibre gestisce risorse, assegnazioni e disponibilita base, ma non offre ancora una UX enterprise comparabile per capacity planning multi-progetto, approvazioni, governance e allocazione portfolio-wide.

### 3. Reporting e analytics

Microsoft Project desktop offre report grafici nativi, burndown e integrazione con Excel/Power BI.

ProjectLibre ha stampa ed export, ma il layer reporting e molto piu debole:

- niente esperienza analitica moderna out-of-the-box;
- niente burndown agile integrato;
- niente bridge nativo verso Power BI;
- UX di reporting meno flessibile e meno executive-friendly.

### 4. AI e automazione

L'offerta Microsoft attuale include Planner agent / Project Manager agent, generazione task da goal e status report automatici.

ProjectLibre non ha nulla di equivalente:

- nessun assistente che scompone obiettivi in task;
- nessun agente che esegue o monitora task;
- nessun status report automatico basato su milestones e progresso;
- nessun supporto AI per rischio, assegnazioni o ribilanciamento.

### 5. UX multi-view moderna

La roadmap Microsoft moderna mette insieme grid, board, timeline, goals, custom fields, dependencies, sprints, team workload.

ProjectLibre ha soprattutto:

- Gantt,
- usage views,
- PERT/network,
- RBS.

Mancano o sono deboli:

- board/kanban veri;
- goals come oggetto di primo livello;
- sprint workflow nativo;
- team workload moderno e leggibile;
- esperienza cloud collaborativa continua.

## Gap che considero prioritari per un rilascio "avanzato"

1. `effort` / uomo-giorno / uomo-ora ben trattati.
2. workload e capacity view moderne.
3. reporting executive e burndown.
4. baselines e variance UX piu leggibili.
5. import/export robusto e moderno.
6. API e automazioni.
7. AI assistita per pianificazione e status.

## Fonti ufficiali usate

- Microsoft Planner and Project Plan 5:
  https://www.microsoft.com/en-us/microsoft-365/planner/project-plan-5
- Microsoft Support, Create a Project report:
  https://support.microsoft.com/en-us/office/create-a-project-report-6e74dc79-0e2d-480b-b600-3a466bf289a3
- Microsoft Support, Create or update a baseline or an interim plan in Project desktop:
  https://support.microsoft.com/en-us/office/create-or-update-a-baseline-or-an-interim-plan-in-project-desktop-7e775482-ac84-4f4a-bbd0-592f9ac91953
- Microsoft Support, Access Planner agent:
  https://support.microsoft.com/en-us/office/access-planner-agent-86bf60a1-239d-4c37-b7b6-9a4111e1cc02
- Microsoft Support, Generate automatic status reports with Planner Agent:
  https://support.microsoft.com/en-us/office/generate-automatic-status-reports-with-planner-agent-d9c87789-da05-4606-b1ff-27fc4f1bf999
- Microsoft Planner FAQ PDF:
  https://cdn.techcommunity.microsoft.com/assets/Planner/Microsoft_Planner_FAQ_May_2024_M365.pdf
