# flashcards.domain.session.tracked_card

from flashcards.domain.card import BasicCard, Card
from flashcards.domain.card_history import CardHistory
from flashcards.domain.card_statistics import CardStatistics
from flashcards.domain.session.session_card import SessionCard


class TrackedCard(BasicCard, SessionCard):

    def __init__(self, card: Card) -> None:
        super().__init__(card.question, card.answer)
        self._statistics = CardStatistics()

    @property
    def statistics(self) -> CardStatistics:
        return self._statistics

    def mark_right(self) -> None:
        self._statistics.history = CardHistory.ANSWERED_RIGHT
        self._statistics.times_answered_right += 1

    def mark_wrong(self) -> None:
        self._statistics.history = CardHistory.ANSWERED_WRONG
        self._statistics.times_answered_wrong += 1
