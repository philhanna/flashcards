# flashcards.adapter.driving.ui.summary_button_panel

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QHBoxLayout, QPushButton, QWidget

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.session_panel import SessionPanel


class SummaryButtonPanel(QWidget):

    def __init__(self, session_panel: SessionPanel) -> None:
        super().__init__()
        layout = QHBoxLayout(self)

        btn_restart = QPushButton("Restart")
        btn_restart.clicked.connect(session_panel.restart)
        layout.addWidget(btn_restart)

        btn_close = QPushButton("Close")
        btn_close.clicked.connect(session_panel.close_session)
        layout.addWidget(btn_close)
