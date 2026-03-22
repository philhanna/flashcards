# flashcards.domain.card_statistics

from dataclasses import dataclass, field

from flashcards.domain.card_history import CardHistory


@dataclass
class CardStatistics:
    history: CardHistory = CardHistory.NEVER_ANSWERED
    times_answered_right: int = 0
    times_answered_wrong: int = 0
