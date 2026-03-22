# flashcards.adapter.driven.sqlite.sqlite_deck

import sqlite3
from pathlib import Path
from typing import Sequence

from flashcards.domain.card import BasicCard, Card
from flashcards.domain.deck import Deck
from flashcards.domain.exceptions import ApplicationException, EmptyDeckException, InvalidCardException


class SqliteDeck(Deck):

    def __init__(self, path: Path) -> None:
        try:
            conn = sqlite3.connect(path)
            conn.row_factory = sqlite3.Row
            with conn:
                self._title = self._load_title(conn)
                self._cards = self._load_cards(conn, path.name)
            conn.close()
        except (ApplicationException, EmptyDeckException, InvalidCardException):
            raise
        except Exception as e:
            raise ApplicationException(f"Failed to read SQLite deck: {path.name}: {e}") from e

    @staticmethod
    def _load_title(conn: sqlite3.Connection) -> str:
        row = conn.execute("SELECT title FROM deck LIMIT 1").fetchone()
        return row["title"] if row else "Untitled"

    @staticmethod
    def _load_cards(conn: sqlite3.Connection, filename: str) -> list[Card]:
        rows = conn.execute(
            "SELECT question, answer FROM card ORDER BY position, id"
        ).fetchall()
        cards: list[Card] = []
        for index, row in enumerate(rows, start=1):
            question = row["question"]
            answer = row["answer"]
            if not question or not question.strip():
                raise InvalidCardException(f"No question found on card {index} in deck")
            if not answer or not answer.strip():
                raise InvalidCardException(f"No answer found on card {index} in deck")
            cards.append(BasicCard(question, answer))
        if not cards:
            raise EmptyDeckException("Empty deck - no cards found")
        return cards

    @property
    def title(self) -> str:
        return self._title

    @property
    def cards(self) -> Sequence[Card]:
        return self._cards
