# flashcards.adapter.driving.ui.card_progress_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtGui import QFont
from PyQt6.QtWidgets import QHBoxLayout, QLabel, QWidget

from flashcards.adapter.driving.ui.session_state import SessionState

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class CardProgressPanel(QWidget):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__(session_panel)
        self._session_panel = session_panel

        layout = QHBoxLayout(self)
        layout.setContentsMargins(10, 5, 5, 5)

        self._label = QLabel()
        font = self._label.font()
        font.setWeight(QFont.Weight.Bold)
        self._label.setFont(font)
        layout.addWidget(self._label)

        session_panel.session_state_changed.connect(self._on_state_changed)
        session_panel.card_changed.connect(self._on_card_changed)

    def _update_numbers(self) -> None:
        session = self._session_panel.session
        cursor = session.cursor
        viewed = cursor.viewed_card_count
        total = cursor.total_card_count
        elapsed = session.elapsed_time
        view_count = session.card_view_count
        avg = elapsed / view_count if view_count else 0.0
        self._label.setText(
            f"Card {viewed + 1} of {total}. Average {avg:.2f} seconds per card."
        )

    def _on_state_changed(self, state: SessionState) -> None:
        self._label.setVisible(state != SessionState.SESSION_COMPLETE)

    def _on_card_changed(self, card) -> None:
        self._update_numbers()
