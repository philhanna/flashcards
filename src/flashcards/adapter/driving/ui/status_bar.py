# flashcards.adapter.driving.ui.status_bar

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QFrame, QHBoxLayout, QWidget

from flashcards.adapter.driving.ui.card_progress_panel import CardProgressPanel
from flashcards.adapter.driving.ui.card_stats_panel import CardStatsPanel
from flashcards.adapter.driving.ui.review_mode_checkbox import ReviewModeCheckbox

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class StatusBar(QWidget):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__(session_panel)
        self.setFrameShape(QFrame.Shape.Panel)
        self.setFrameShadow(QFrame.Shadow.Sunken)

        layout = QHBoxLayout(self)
        layout.setContentsMargins(0, 0, 0, 0)
        layout.addWidget(CardProgressPanel(session_panel))
        layout.addWidget(CardStatsPanel(session_panel), stretch=1)
        layout.addWidget(ReviewModeCheckbox(session_panel))
