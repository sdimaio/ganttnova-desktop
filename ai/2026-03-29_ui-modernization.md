# UI Modernization

Data: 2026-03-29

## Problema

La UI attuale aveva tre difetti immediati:

- su Linux veniva ancora forzato `MetalLookAndFeel`;
- il bootstrap usava impostazioni tipografiche datate e non automatiche;
- il codice chiamava direttamente `System.setSecurityManager(null)`, punto fragile su JDK moderni.

## Interventi eseguiti

### Look and feel

`LafManagerImpl` ora usa una catena di fallback piu moderna:

1. `FlatMacLightLaf` o `FlatLightLaf` se disponibile;
2. look-and-feel di sistema;
3. Nimbus;
4. Metal come ultima spiaggia.

In aggiunta sono stati impostati alcuni default visivi piu moderni:

- arrotondamento componenti;
- scrollbar piu larga;
- tabella con linee orizzontali attive e verticali disattive.

### Font e resa testo

`Main` imposta subito:

- `awt.useSystemAAFontSettings=on`
- `swing.aatext=true`
- `apple.laf.useScreenMenuBar=true`

`ApplicationStartupFactory` ora seleziona automaticamente un font UI piu credibile, per piattaforma:

- Windows: `Segoe UI`, poi fallback;
- macOS: `SF Pro Text`, `Helvetica Neue`, fallback;
- Linux: `Inter`, `Noto Sans`, `Cantarell`, `DejaVu Sans`, fallback.

Il font scelto viene portato a una dimensione di default coerente (`-13`) quando l'utente non fornisce un override esplicito.

### Bootstrap compatibile con JDK moderni

`StartupFactory` non invoca piu direttamente `System.setSecurityManager(null)`.

Ora usa reflection e degrada in modo silenzioso se l'API non e piu disponibile. Questo riduce il rischio di breakage su runtime recenti.

### Dimensioni finestra

`MainFrame` e stato aggiornato con una finestra iniziale e una `minimumSize` piu adatte a display attuali:

- Windows: `1280x800`
- macOS: preferenza `1440x900`
- altri sistemi: preferenza `1366x860`
- `minimumSize`: `1100x720`

## Impatto atteso

Questo non trasforma ancora la UI in un prodotto moderno, ma migliora subito quattro aspetti concreti:

- aspetto meno datato;
- leggibilita migliore;
- resa piu coerente su Linux;
- bootstrap meno fragile per JDK recenti.

## Debiti UI ancora aperti

I punti strutturali ancora brutti restano:

- layout Swing estremamente denso;
- ribbon e toolbar legacy;
- uso di componenti applet/JApplet deprecati;
- assenza di un redesign vero di spacing, gerarchia visiva e navigazione.

Il passo successivo serio non e "cambiare altri colori", ma:

1. ripulire le schermate principali Gantt/Task/Resources;
2. ridurre la densita visuale delle tabelle;
3. rivedere ribbon e toolbar;
4. eliminare definitivamente gli appigli storici ad applet/JNLP dove non servono piu.
