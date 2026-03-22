# flashcards.domain.deck

from abc import ABC, abstractmethod
from typing import Sequence

from flashcards.domain.card import Card


class Deck(ABC):

    @property
    @abstractmethod
    def title(self) -> str: ...

    @property
    @abstractmethod
    def cards(self) -> Sequence[Card]: ...
