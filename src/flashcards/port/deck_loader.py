# flashcards.port.deck_loader

from abc import ABC, abstractmethod
from pathlib import Path

from flashcards.domain.deck import Deck


class DeckLoader(ABC):

    @abstractmethod
    def load(self, path: Path) -> Deck: ...
