# flashcards.domain.session.session_cursor

from abc import ABC, abstractmethod


class SessionCursor(ABC):

    @property
    @abstractmethod
    def total_card_count(self) -> int: ...

    @property
    @abstractmethod
    def unviewed_card_count(self) -> int: ...

    @property
    @abstractmethod
    def viewed_card_count(self) -> int: ...
