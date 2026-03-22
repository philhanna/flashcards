# flashcards.domain.session.session_card

from abc import abstractmethod

from flashcards.domain.card import Card
from flashcards.domain.card_statistics import CardStatistics


class SessionCard(Card):

    @property
    @abstractmethod
    def statistics(self) -> CardStatistics: ...

    @abstractmethod
    def mark_right(self) -> None: ...

    @abstractmethod
    def mark_wrong(self) -> None: ...
