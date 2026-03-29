# GanttNova Desktop

`GanttNova Desktop` is a modernized desktop scheduling fork of
ProjectLibre/OpenProj.

The goal is not to pretend this codebase was rewritten from scratch.
The goal is to turn a historically important but aging Java desktop planner
into a buildable, runnable, inspectable base for serious modernization on
current JDKs.

This repository is maintained by Simmaco Di Maio.

It is not an official ProjectLibre release and it is not affiliated with
ProjectLibre Inc. or upstream maintainers.

The current public fork base tracked during this modernization effort is
[`smartqubit/projectlibre`](https://github.com/smartqubit/projectlibre).

Italian documentation is available in [README.it.md](README.it.md).

## Status

Current status: experimental, but already usable as a modernized fork.

What is already in place:

- Maven reactor and cleaner dependency packaging
- runtime fixes for current JDKs
- `effort` exposed as a person-day column
- default resource allocation on the resource sheet, reused in Gantt parsing
- automatic removal of the estimated `?` marker when `effort` determines
  the final duration
- restart-safe `Look&Feel` preference
- first `Task Inspector` to explain schedule drivers, constraints and
  dependencies

## Why This Exists

ProjectLibre still matters because a local desktop project planner is useful.

What does not matter is preserving every legacy build assumption forever.

This fork exists to:

- keep the desktop, local-first use case alive
- make the codebase work on current JVMs
- improve scheduling semantics where the original UX is weak
- prepare a cleaner platform for deeper UI and engine modernization

## Current Focus

The work is intentionally pragmatic:

- stabilize build and packaging first
- remove runtime blockers on modern JDKs
- improve planning semantics around `effort`, allocation and duration
- add explainability to scheduling before adding more surface area
- modernize the UI incrementally instead of attempting a risky rewrite

## Build And Run

Build everything with tests:

```bash
mvn package -DskipTests=false
```

Run the desktop application:

```bash
./run-projectlibre.sh
```

The packaged jar is produced at:

```text
projectlibre-app/target/projectlibre-app-1.6.2-modernized-SNAPSHOT.jar
```

## Notable Changes Already Landed

- `Effort` in person-days is now a first-class task field.
- Resource default allocation can drive assignment units automatically.
- `me` in the Gantt can resolve to `me[50%]` when the resource default is `50%`.
- Duration is no longer left as estimated when fixed work plus assignment units
  already determine the schedule.
- A first `Task Inspector` explains predecessors, constraints, deadline,
  leveling delay, slack and resource impact for the selected task.

## Roadmap

Near-term priorities:

- deeper scheduling-engine tests
- JDK 25-oriented cleanup of legacy APIs and packaging assumptions
- side-panel version of the `Task Inspector`
- visual highlighting of task path and scheduling blockers
- stronger UI modernization beyond legacy Swing defaults

## Legal And Attribution

This repository distributes a modified fork of ProjectLibre/OpenProj code.

- upstream attribution remains important and is intentionally preserved
- the public fork base currently tracked is
  [`smartqubit/projectlibre`](https://github.com/smartqubit/projectlibre)
- legacy source headers still reference CPAL obligations and attribution rules
- the repository does not claim to be upstream or official
- bundled third-party notices remain under
  [`openproj_build/license/third-party/`](openproj_build/license/third-party/)
- dated engineering notes and change documentation are kept under [`ai/`](ai/)

See [NOTICE.md](NOTICE.md) for the fork/derivation notice.

## License

This repository contains code distributed under `CPAL-1.0` together with
various third-party open source components listed in the bundled notices.

See:

- [LICENSE](LICENSE)
- [NOTICE.md](NOTICE.md)
- [`openproj_build/license/third-party/`](openproj_build/license/third-party/)
