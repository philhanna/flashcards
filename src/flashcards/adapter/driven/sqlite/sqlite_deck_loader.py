# flashcards.adapter.driven.sqlite.sqlite_deck_loader

from pathlib import Path

from flashcards.adapter.driven.sqlite.sqlite_deck import SqliteDeck
from flashcards.domain.deck import Deck
from flashcards.domain.exceptions import ApplicationException
from flashcards.port.deck_loader import DeckLoader


class SqliteDeckLoader(DeckLoader):

    def load(self, path: Path) -> Deck:
        if not path.exists():
            raise ApplicationException(f"Deck file not found: {path}")
        return SqliteDeck(path)
