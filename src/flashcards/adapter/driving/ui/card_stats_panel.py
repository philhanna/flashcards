# flashcards.adapter.driving.ui.card_stats_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QLabel, QWidget, QHBoxLayout

from flashcards.adapter.driving.ui.session_state import SessionState
from flashcards.domain.card_history import CardHistory

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class CardStatsPanel(QWidget):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__(session_panel)
        layout = QHBoxLayout(self)
        layout.setContentsMargins(0, 0, 0, 0)
        self._label = QLabel()
        layout.addWidget(self._label)

        session_panel.session_state_changed.connect(self._on_state_changed)
        session_panel.card_changed.connect(self._on_card_changed)

    def _on_state_changed(self, state: SessionState) -> None:
        self._label.setVisible(
            state in (SessionState.ASKING_QUESTION, SessionState.SHOWING_ANSWER)
        )

    def _on_card_changed(self, card) -> None:
        if card is None:
            return
        stats = card.statistics
        history = stats.history
        times_wrong = stats.times_answered_wrong

        if history == CardHistory.NEVER_ANSWERED:
            text = "Not yet answered"
        elif history == CardHistory.ANSWERED_RIGHT:
            text = "Last answered right"
            if times_wrong == 1:
                text += ", missed once before"
            elif times_wrong > 1:
                text += f", missed {times_wrong} times before"
        else:
            text = "Missed last time"
            if times_wrong == 2:
                text += ", missed once before"
            elif times_wrong > 3:
                text += f", missed {times_wrong - 1} times before"

        self._label.setText(text)
