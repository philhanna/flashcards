# flashcards.port.study_session_use_case

from abc import ABC, abstractmethod

from flashcards.domain.session.session import Session


class StudySessionUseCase(ABC):

    @abstractmethod
    def mark_right(self) -> None: ...

    @abstractmethod
    def mark_wrong(self) -> None: ...

    @abstractmethod
    def next_card(self) -> None: ...

    @abstractmethod
    def previous_card(self) -> None: ...

    @property
    @abstractmethod
    def session(self) -> Session: ...
