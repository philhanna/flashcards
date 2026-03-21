# FlashCards

A Java Swing desktop application for studying flashcard decks. Originally designed for
Jeopardy, Quiz Bowl, and similar trivia contests, but suitable for any subject where
question-and-answer memorization is useful.

## Features

- **SQLite decks** — card decks are stored as SQLite database files (`.db`)
- **Flip interaction** — each card shows a question; click *Flip* to reveal the answer
- **Right / Wrong tracking** — mark each answer as right or wrong; missed cards are
  re-inserted and reshuffled into the remaining deck so you keep seeing them until you
  get them right
- **Review mode** — a checkbox that shows all answers immediately, so you can browse
  the deck without being quizzed
- **Session statistics** — at the end of a session the app displays a summary table
  showing how many times each card was answered right and wrong, plus the total
  elapsed time and card-view count
- **Configurable look and feel** — supports Metal, Nimbus, Motif, and GTK themes via
  `config.properties`

## Requirements

- Java 11 or later
- Maven 3.x (to build from source)
- Python 3 (optional — only needed to convert legacy XML decks to SQLite)

## Building

```bash
mvn package
```

This produces `target/flashcards-1.0-SNAPSHOT.jar` as a self-contained fat jar with
all dependencies (including the SQLite JDBC driver) bundled inside.

## Installing

```bash
mvn install
```

This copies the jar to `~/.local/lib/flashcards.jar`, the launcher script to
`~/.local/bin/flashcards`, and — on first install only — places a default
`config.properties` at `~/.config/flashcards/config.properties` for you to edit.

## Running

```bash
flashcards [deckfile]
```

Or directly:

```bash
java -jar target/flashcards-1.0-SNAPSHOT.jar [deckfile]
```

## Deck format

Each deck is a single `.db` SQLite file with the following schema:

```sql
CREATE TABLE deck (title TEXT NOT NULL);
CREATE TABLE card (
  id       INTEGER PRIMARY KEY AUTOINCREMENT,
  question TEXT NOT NULL,
  answer   TEXT NOT NULL,
  position INTEGER NOT NULL DEFAULT 0
);
```

### Migrating from XML

If you have existing XML decks, convert them with the included Python script:

```bash
python3 tools/xml_to_sqlite.py path/to/deck.xml
# produces path/to/deck.db alongside the source file

# convert a whole directory at once
for f in /path/to/decks/*.xml; do
    python3 tools/xml_to_sqlite.py "$f"
done
```

## Sample decks

Several ready-to-use decks are included under `src/test/resources/`:

| Deck | Subject |
|---|---|
| `Shakespeare.db` | Shakespeare tragedies and comedies |
| `Best_Picture_Awards.db` | Academy Award Best Picture winners |
| `Jane_Austen.db` | Jane Austen novels |
| `NBA.db` | NBA trivia |
| `Norse_Mythology.db` | Norse mythology |

## Configuration

The app reads configuration from a `config.properties` file in the standard
OS location for user configuration:

| OS | Path |
|---|---|
| Linux | `~/.config/flashcards/config.properties` (or `$XDG_CONFIG_HOME/flashcards/config.properties`) |
| Windows | `%APPDATA%\flashcards\config.properties` |
| macOS | `~/Library/Application Support/flashcards/config.properties` |

`mvn install` creates this file automatically on first install from the bundled
`src/main/resources/sample.properties`. You can also create it manually by copying
that file to the appropriate location above.

If no user config file is found, the bundled defaults are used.

| Property | Default | Description |
|---|---|---|
| `x` | `100` | Horizontal position of the main window in pixels from the left of the screen. Updated automatically when the window is closed. |
| `y` | `100` | Vertical position of the main window in pixels from the top of the screen. Updated automatically when the window is closed. |
| `width` | `600` | Width of the main window in pixels. Updated automatically when the window is closed. |
| `height` | `400` | Height of the main window in pixels. Updated automatically when the window is closed. |
| `directory` | `.` | Last directory visited in the open dialog. Updated automatically each time a deck is opened. |
| `card_icon` | `/cardicon.png` | Classpath path to the window icon image. The default is bundled in the jar. |
| `text_editor` | `gvim` | Command used to launch an external text editor when *Edit* is chosen from the menu. |
| `look_and_feel` | `javax.swing.plaf.metal.MetalLookAndFeel` | Fully-qualified class name of the Swing look and feel. Supported values: `javax.swing.plaf.metal.MetalLookAndFeel`, `com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel`, `com.sun.java.swing.plaf.motif.MotifLookAndFeel`, `com.sun.java.swing.plaf.gtk.GTKLookAndFeel`. |

## How a session works

1. Open a deck via *File → Open*.
2. The first card's question is displayed. Click **Flip** to reveal the answer.
3. Click **Right** if you knew the answer — the card is retired for this session.
4. Click **Wrong** if you didn't — the card is re-inserted into the remaining deck and
   reshuffled so it will appear again.
5. When all cards have been answered correctly, the session statistics screen is shown.
6. Click **Restart** to go through the deck again, or **Close** to return to the start.

## Licence

See [LICENCE](LICENCE).
