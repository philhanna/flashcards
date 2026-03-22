# flashcards.adapter.driving.ui.session_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtCore import pyqtSignal
from PyQt6.QtWidgets import QVBoxLayout, QWidget

from flashcards.adapter.driving.ui.session_state import SessionState
from flashcards.domain.deck import Deck
from flashcards.domain.session.deck_session import DeckSession
from flashcards.domain.session.session import Session
from flashcards.usecase.study_session import StudySession

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow


class SessionPanel(QWidget):

    card_changed = pyqtSignal(object)
    session_state_changed = pyqtSignal(object)

    def __init__(self, main_window: MainWindow, deck: Deck) -> None:
        super().__init__(main_window)
        self._main_window = main_window
        self._study_session = StudySession(DeckSession(deck))
        self._state = SessionState.ASKING_QUESTION

        layout = QVBoxLayout(self)
        layout.setContentsMargins(4, 4, 4, 4)

        from flashcards.adapter.driving.ui.card_panel import CardPanel
        from flashcards.adapter.driving.ui.button_panel import ButtonPanel
        from flashcards.adapter.driving.ui.status_bar import StatusBar

        self._card_panel = CardPanel(self)
        layout.addWidget(self._card_panel, stretch=1)

        lower = QWidget()
        lower_layout = QVBoxLayout(lower)
        lower_layout.setContentsMargins(0, 0, 0, 0)
        lower_layout.addWidget(ButtonPanel(self))
        lower_layout.addWidget(StatusBar(self))
        layout.addWidget(lower)

        self._fire_card_change()
        self._fire_session_state_change()

    @property
    def session(self) -> Session:
        return self._study_session.session

    def _fire_card_change(self) -> None:
        self._study_session.session.record_card_view()
        self.card_changed.emit(self._study_session.session.current_card)

    def _fire_session_state_change(self) -> None:
        self.session_state_changed.emit(self._state)

    def _set_state(self, state: SessionState) -> None:
        self._state = state
        if state != SessionState.SESSION_COMPLETE:
            self._fire_session_state_change()
        else:
            self._display_statistics()

    def flip(self) -> None:
        self._set_state(SessionState.SHOWING_ANSWER)

    def mark_right(self) -> None:
        self._study_session.mark_right()
        if self._study_session.session.current_card is None:
            self._set_state(SessionState.SESSION_COMPLETE)
        else:
            self._fire_card_change()
            self._set_state(SessionState.ASKING_QUESTION)

    def mark_wrong(self) -> None:
        self._study_session.mark_wrong()
        self._fire_card_change()
        self._set_state(SessionState.ASKING_QUESTION)

    def next_card(self) -> None:
        self._study_session.next_card()
        if self._study_session.session.current_card is None:
            self._display_statistics()
        else:
            self._fire_card_change()

    def previous_card(self) -> None:
        self._study_session.previous_card()
        self._fire_card_change()

    def set_review_mode(self, enabled: bool) -> None:
        state = SessionState.REVIEW_MODE if enabled else SessionState.ASKING_QUESTION
        self._set_state(state)

    def _display_statistics(self) -> None:
        from flashcards.adapter.driving.ui.summary_panel import SummaryPanel
        self._main_window.setCentralWidget(SummaryPanel(self._main_window, self))

    def restart(self) -> None:
        self._main_window.set_file(self._main_window._path)

    def close_session(self) -> None:
        self._main_window.set_file(None)
