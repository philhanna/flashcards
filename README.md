# FlashCards2

A Java Swing desktop application for studying flashcard decks. Originally designed for
Jeopardy, Quiz Bowl, and similar trivia contests, but suitable for any subject where
question-and-answer memorization is useful.

## Features

- **XML-based decks** — card decks are plain XML files you create and edit yourself
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

## Building

```bash
mvn package
```

This produces `target/flashcards-1.0-SNAPSHOT.jar` with the main class embedded in the
manifest.

## Running

```bash
java -jar target/flashcards-1.0-SNAPSHOT.jar
```

Or copy the jar to a convenient location and use the provided launcher script:

```bash
# Copy the jar
cp target/flashcards-1.0-SNAPSHOT.jar ~/lib/fc2.jar

# Run via the shell script
shell/flashcards
```

## Deck file format

Decks are XML files with the following structure:

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

- The `<title>` element is displayed in the window title bar.
- Every `<card>` must have both a `<question>` and an `<answer>`; the app will report
  an error and refuse to load the deck if either is missing.

## Sample decks

Several ready-to-use decks are included under `src/test/resources/`:

| File | Subject |
|---|---|
| `Shakespeare.xml` | Shakespeare tragedies and comedies |
| `Best_Picture_Awards.xml` | Academy Award Best Picture winners |
| `Jane_Austen.xml` | Jane Austen novels |
| `NBA.xml` | NBA trivia |
| `Norse_Mythology.xml` | Norse mythology |

## Configuration

The file `src/main/resources/config.properties` controls the window position, size,
text editor, and look-and-feel:

| Property | Default | Description |
|---|---|---|
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
