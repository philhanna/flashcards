# FlashCards

A Python/PyQt6 desktop application for studying flashcard decks. Originally designed for
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

## Requirements

- Python 3.11 or later
- PyQt6

## Installing

```bash
pip install .
```

For development (includes pytest, mypy, etc.):

```bash
pip install -e ".[dev]"
```

## Running

```bash
flashcards [deckfile]
```

Or directly:

```bash
python -m flashcards [deckfile]
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

## Sample decks

Several ready-to-use decks are included under `tests/resources/`:

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

If no user config file is found, the bundled defaults are used.

| Property | Default | Description |
|---|---|---|
| `x` | `100` | Horizontal position of the main window in pixels from the left of the screen. Updated automatically when the window is closed. |
| `y` | `100` | Vertical position of the main window in pixels from the top of the screen. Updated automatically when the window is closed. |
| `width` | `600` | Width of the main window in pixels. Updated automatically when the window is closed. |
| `height` | `400` | Height of the main window in pixels. Updated automatically when the window is closed. |
| `directory` | `.` | Last directory visited in the open dialog. Updated automatically each time a deck is opened. |
| `card_icon` | *(bundled)* | Path to the window icon image. |
| `text_editor` | `gvim` | Command used to launch an external text editor when *Edit* is chosen from the menu. |

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
