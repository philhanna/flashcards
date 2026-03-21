#!/usr/bin/env python3
"""
Convert a flashcard XML deck file to a SQLite database.

Usage:
    xml_to_sqlite.py <input.xml> [output.db]

If output.db is not specified, it is placed alongside the input file
with the same base name and a .db extension.  Any existing output file
is overwritten.

Expected XML format:
    <cards>
        <title>Deck title</title>
        <card>
            <question>...</question>
            <answer>...</answer>
        </card>
        ...
    </cards>

Resulting SQLite schema:
    CREATE TABLE deck (title TEXT NOT NULL);
    CREATE TABLE card (
        id       INTEGER PRIMARY KEY AUTOINCREMENT,
        question TEXT NOT NULL,
        answer   TEXT NOT NULL,
        position INTEGER NOT NULL DEFAULT 0
    );
"""

import sqlite3
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


def convert(xml_path: Path, db_path: Path) -> None:
    tree = ET.parse(xml_path)
    root = tree.getroot()

    title_elem = root.find("title")
    title = title_elem.text.strip() if title_elem is not None and title_elem.text else "Untitled"

    cards = []
    for i, card_elem in enumerate(root.findall("card"), start=1):
        q_elem = card_elem.find("question")
        a_elem = card_elem.find("answer")

        question = q_elem.text.strip() if q_elem is not None and q_elem.text else ""
        answer   = a_elem.text.strip() if a_elem is not None and a_elem.text else ""

        if not question:
            raise ValueError(f"No question found on card {i} in deck")
        if not answer:
            raise ValueError(f"No answer found on card {i} in deck")

        cards.append((question, answer, i))

    if not cards:
        raise ValueError("Empty deck - no cards found")

    db_path.unlink(missing_ok=True)
    conn = sqlite3.connect(db_path)
    try:
        conn.executescript("""
            CREATE TABLE deck (
                title TEXT NOT NULL
            );
            CREATE TABLE card (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                question TEXT NOT NULL,
                answer   TEXT NOT NULL,
                position INTEGER NOT NULL DEFAULT 0
            );
        """)
        conn.execute("INSERT INTO deck (title) VALUES (?)", (title,))
        conn.executemany(
            "INSERT INTO card (question, answer, position) VALUES (?, ?, ?)",
            cards,
        )
        conn.commit()
    finally:
        conn.close()

    print(f"Converted {len(cards)} cards  →  {db_path}")


def main() -> None:
    if len(sys.argv) < 2:
        print(__doc__)
        sys.exit(1)

    xml_path = Path(sys.argv[1])
    if not xml_path.exists():
        print(f"Error: file not found: {xml_path}", file=sys.stderr)
        sys.exit(1)

    db_path = Path(sys.argv[2]) if len(sys.argv) >= 3 else xml_path.with_suffix(".db")

    try:
        convert(xml_path, db_path)
    except (ET.ParseError, ValueError) as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
