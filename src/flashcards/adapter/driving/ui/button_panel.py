# flashcards.adapter.driving.ui.button_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QHBoxLayout, QPushButton, QWidget

from flashcards.adapter.driving.ui.session_state import SessionState

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class ButtonPanel(QWidget):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__(session_panel)
        self._session_panel = session_panel
        self._state = SessionState.ASKING_QUESTION

        layout = QHBoxLayout(self)

        self._btn1 = QPushButton("Previous")
        self._btn2 = QPushButton("Flip")
        layout.addWidget(self._btn1)
        layout.addWidget(self._btn2)

        self._btn1.clicked.connect(self._on_btn1)
        self._btn2.clicked.connect(self._on_btn2)

        session_panel.session_state_changed.connect(self._on_state_changed)

    def _on_btn1(self) -> None:
        if self._state in (SessionState.ASKING_QUESTION, SessionState.REVIEW_MODE):
            self._session_panel.previous_card()
        elif self._state == SessionState.SHOWING_ANSWER:
            self._session_panel.mark_wrong()

    def _on_btn2(self) -> None:
        if self._state == SessionState.ASKING_QUESTION:
            self._session_panel.flip()
        elif self._state == SessionState.REVIEW_MODE:
            self._session_panel.next_card()
        elif self._state == SessionState.SHOWING_ANSWER:
            self._session_panel.mark_right()

    def _on_state_changed(self, state: SessionState) -> None:
        self._state = state
        if state == SessionState.ASKING_QUESTION:
            self._btn1.setText("Previous")
            self._btn2.setText("Flip")
        elif state == SessionState.SHOWING_ANSWER:
            self._btn1.setText("Wrong")
            self._btn2.setText("Right")
        elif state == SessionState.REVIEW_MODE:
            self._btn1.setText("Previous")
            self._btn2.setText("Next")
