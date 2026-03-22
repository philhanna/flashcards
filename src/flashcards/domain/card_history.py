# flashcards.domain.card_history

import enum


class CardHistory(enum.Enum):
    NEVER_ANSWERED = "NEVER_ANSWERED"
    ANSWERED_RIGHT = "ANSWERED_RIGHT"
    ANSWERED_WRONG = "ANSWERED_WRONG"
