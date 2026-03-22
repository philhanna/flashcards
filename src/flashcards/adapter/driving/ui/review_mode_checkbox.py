# flashcards.adapter.driving.ui.review_mode_checkbox

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QCheckBox

from flashcards.adapter.driving.ui.session_state import SessionState

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class ReviewModeCheckbox(QCheckBox):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__("Review mode", session_panel)
        self._session_panel = session_panel
        self.setChecked(False)
        self.stateChanged.connect(self._on_state_changed)
        session_panel.session_state_changed.connect(self._on_session_state_changed)

    def _on_state_changed(self, state: int) -> None:
        self._session_panel.set_review_mode(self.isChecked())

    def _on_session_state_changed(self, state: SessionState) -> None:
        self.setVisible(state != SessionState.SESSION_COMPLETE)
