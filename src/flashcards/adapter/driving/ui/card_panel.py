# flashcards.adapter.driving.ui.card_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import QFrame, QLabel, QSizePolicy, QVBoxLayout, QWidget

from flashcards.adapter.driving.ui.session_state import SessionState
from flashcards.domain.card_history import CardHistory

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class CardPanel(QWidget):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__(session_panel)
        self._session_panel = session_panel

        layout = QVBoxLayout(self)

        self._lbl_question = self._make_label()
        layout.addWidget(self._lbl_question, stretch=1)

        self._lbl_answer = self._make_label()
        layout.addWidget(self._lbl_answer, stretch=1)

        session_panel.session_state_changed.connect(self._on_state_changed)
        session_panel.card_changed.connect(self._on_card_changed)

    @staticmethod
    def _make_label() -> QLabel:
        label = QLabel()
        label.setFrameShape(QFrame.Shape.Box)
        label.setAlignment(Qt.AlignmentFlag.AlignTop | Qt.AlignmentFlag.AlignLeft)
        label.setWordWrap(True)
        label.setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Preferred)
        return label

    def _update_text(self) -> None:
        card = self._session_panel.session.current_card
        if card is None:
            return
        history = card.statistics.history
        self._lbl_question.setText(self._question_html(card.question, history))
        self._lbl_answer.setText(self._answer_html(card.answer))

    @staticmethod
    def _question_html(text: str, history: CardHistory) -> str:
        color = ""
        if history == CardHistory.ANSWERED_RIGHT:
            color = "color: #00AA00;"
        elif history == CardHistory.ANSWERED_WRONG:
            color = "color: #FF0000;"
        return f"<span style='font-size: 14pt; {color}'>{text}</span>"

    @staticmethod
    def _answer_html(text: str) -> str:
        return f"<span style='font-size: 14pt;'>{text}</span>"

    def _on_state_changed(self, state: SessionState) -> None:
        self._lbl_question.setVisible(state != SessionState.SESSION_COMPLETE)
        self._lbl_answer.setVisible(
            state in (SessionState.SHOWING_ANSWER, SessionState.REVIEW_MODE)
        )
        self._update_text()

    def _on_card_changed(self, card) -> None:
        self._update_text()
