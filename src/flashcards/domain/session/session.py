# flashcards.domain.session.session

from abc import ABC, abstractmethod
from typing import Sequence

from flashcards.domain.session.session_card import SessionCard
from flashcards.domain.session.session_cursor import SessionCursor


class Session(ABC):

    @property
    @abstractmethod
    def current_card(self) -> SessionCard | None: ...

    @property
    @abstractmethod
    def cursor(self) -> SessionCursor: ...

    @abstractmethod
    def next_card(self) -> None: ...

    @abstractmethod
    def previous_card(self) -> None: ...

    @abstractmethod
    def shuffle(self) -> None: ...

    @abstractmethod
    def rotate(self) -> None: ...

    @property
    @abstractmethod
    def viewed_cards(self) -> Sequence[SessionCard]: ...

    @property
    @abstractmethod
    def elapsed_time(self) -> float: ...

    @property
    @abstractmethod
    def card_view_count(self) -> int: ...

    @abstractmethod
    def record_card_view(self) -> None: ...
