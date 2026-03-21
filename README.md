# FlashCards

A Java Swing desktop application for studying flashcard decks. Originally designed for
Jeopardy, Quiz Bowl, and similar trivia contests, but suitable for any subject where
question-and-answer memorization is useful.

## Features

- **XML or SQLite decks** — card decks are stored as plain XML files or SQLite databases;
  the format is selected via `config.properties`
- **Flip interaction** — each card shows a question; click *Flip* to reveal the answer
- **Right / Wrong tracking** — mark each answer as right or wrong; missed cards are
  re-inserted and reshuffled into the remaining deck so you keep seeing them until you
  get them right
- **Review mode** — a checkbox that shows all answers immediately, so you can browse
  the deck without being quizzed
- **Toggle mode** — swap questions and answers so you can drill from the answer side
- **Session statistics** — at the end of a session the app displays a summary table
  showing how many times each card was answered right and wrong, plus the total
  elapsed time and card-view count
- **Configurable look and feel** — supports Metal, Nimbus, Motif, and GTK themes via
  `config.properties`

## Requirements

- Java 11 or later
- Maven 3.x (to build from source)
- Python 3 (optional — only needed to convert XML decks to SQLite)

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

Copies the jar to `~/.local/lib/flashcards.jar` and the launcher script to
`~/.local/bin/flashcards`.

## Running

```bash
flashcards [deckfile]
```

Or directly:

```bash
java -jar target/flashcards-1.0-SNAPSHOT.jar [deckfile]
```

## Deck file formats

The app supports two storage formats, controlled by the `deck_format` property in
`config.properties`. Both formats represent the same data: a deck title and an ordered
list of question/answer cards.

### XML

```xml
<?xml version="1.0" encoding="utf-8"?>
<cards>
  <title>My Deck Title</title>

  <card>
    <question>What is the capital of France?</question>
    <answer>Paris</answer>
  </card>

  <card>
    <question>Who wrote Hamlet?</question>
    <answer>William Shakespeare</answer>
  </card>
</cards>
```

### SQLite

Each deck is a single `.db` file with the following schema:

```sql
CREATE TABLE deck (title TEXT NOT NULL);
CREATE TABLE card (
  id       INTEGER PRIMARY KEY AUTOINCREMENT,
  question TEXT NOT NULL,
  answer   TEXT NOT NULL,
  position INTEGER NOT NULL DEFAULT 0
);
```

To convert an existing XML deck to SQLite, use the included Python script:

```bash
python3 tools/xml_to_sqlite.py path/to/deck.xml
# produces path/to/deck.db alongside the source file

# convert a whole directory at once
for f in /path/to/decks/*.xml; do
    python3 tools/xml_to_sqlite.py "$f"
done
```

## Sample decks

Several ready-to-use decks are included under `src/test/resources/` in both formats:

| Deck | Subject |
|---|---|
| `Shakespeare` | Shakespeare tragedies and comedies |
| `Best_Picture_Awards` | Academy Award Best Picture winners |
| `Jane_Austen` | Jane Austen novels |
| `NBA` | NBA trivia |
| `Norse_Mythology` | Norse mythology |

## Configuration

Edit `src/main/resources/config.properties` before building, or replace
`~/.local/lib/flashcards.jar` with a rebuild after changing settings.

| Property | Default | Description |
|---|---|---|
| `deck_format` | `xml` | Storage format: `xml` or `sqlite` |
| `x` | `100` | Initial window X position |
| `y` | `100` | Initial window Y position |
| `width` | `600` | Initial window width |
| `height` | `400` | Initial window height |
| `text_editor` | `gvim` | Editor launched by the *Edit* menu item |
| `look_and_feel` | `javax.swing.plaf.metal.MetalLookAndFeel` | Swing look and feel class |

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
