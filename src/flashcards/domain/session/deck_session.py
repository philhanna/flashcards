# flashcards.domain.session.deck_session

import random
import time
from typing import Sequence

from flashcards.domain.deck import Deck
from flashcards.domain.session.session import Session
from flashcards.domain.session.session_card import SessionCard
from flashcards.domain.session.session_cursor import SessionCursor
from flashcards.domain.session.tracked_card import TrackedCard


class DeckSession(Session):

    class _Cursor(SessionCursor):
        def __init__(self, session: "DeckSession") -> None:
            self._session = session

        @property
        def total_card_count(self) -> int:
            return self.viewed_card_count + self.unviewed_card_count

        @property
        def unviewed_card_count(self) -> int:
            return len(self._session._unviewed)

        @property
        def viewed_card_count(self) -> int:
            return len(self._session._viewed)

    def __init__(self, deck: Deck) -> None:
        self._unviewed: list[SessionCard] = [TrackedCard(card) for card in deck.cards]
        self._viewed: list[SessionCard] = []
        self._cursor = DeckSession._Cursor(self)
        self.shuffle()
        self._start_time = time.monotonic()
        self._card_view_count = 0

    def shuffle(self) -> None:
        for _ in range(3):
            random.shuffle(self._unviewed)

    def next_card(self) -> None:
        if self._unviewed:
            self._viewed.append(self._unviewed.pop())

    def previous_card(self) -> None:
        if self._viewed:
            self._unviewed.append(self._viewed.pop())

    @property
    def current_card(self) -> SessionCard | None:
        return self._unviewed[-1] if self._unviewed else None

    @property
    def cursor(self) -> SessionCursor:
        return self._cursor

    def rotate(self) -> None:
        if self._unviewed:
            self._unviewed.insert(0, self._unviewed.pop())

    @property
    def viewed_cards(self) -> list[SessionCard]:
        return self._viewed

    @property
    def elapsed_time(self) -> float:
        return time.monotonic() - self._start_time

    @property
    def card_view_count(self) -> int:
        return self._card_view_count

    def record_card_view(self) -> None:
        self._card_view_count += 1
