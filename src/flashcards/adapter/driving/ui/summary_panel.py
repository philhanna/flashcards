# flashcards.adapter.driving.ui.summary_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QLabel, QVBoxLayout, QWidget

from flashcards.adapter.driving.ui.statistics_table import build_statistics_html
from flashcards.adapter.driving.ui.summary_button_panel import SummaryButtonPanel

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class SummaryPanel(QWidget):

    def __init__(self, main_window: MainWindow, session_panel: SessionPanel) -> None:
        super().__init__(main_window)
        layout = QVBoxLayout(self)

        table_label = QLabel(build_statistics_html(session_panel.session))
        table_label.setWordWrap(True)
        layout.addWidget(table_label, stretch=1)

        layout.addWidget(SummaryButtonPanel(session_panel))
