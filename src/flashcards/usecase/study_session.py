# flashcards.usecase.study_session

from flashcards.domain.session.session import Session
from flashcards.port.study_session_use_case import StudySessionUseCase


class StudySession(StudySessionUseCase):

    def __init__(self, session: Session) -> None:
        self._session = session

    def mark_right(self) -> None:
        card = self._session.current_card
        if card is not None:
            card.mark_right()
        self._session.next_card()

    def mark_wrong(self) -> None:
        card = self._session.current_card
        if card is not None:
            card.mark_wrong()
            self._session.rotate()
            self._session.shuffle()

    def next_card(self) -> None:
        self._session.next_card()

    def previous_card(self) -> None:
        self._session.previous_card()

    @property
    def session(self) -> Session:
        return self._session
