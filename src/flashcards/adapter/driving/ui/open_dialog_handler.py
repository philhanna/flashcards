# flashcards.adapter.driving.ui.open_dialog_handler

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QFileDialog

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow


class OpenDialogHandler:

    def __init__(self, main_window: MainWindow) -> None:
        self._main_window = main_window

    def run(self) -> None:
        start_dir = str(self._main_window.get_last_directory())
        path_str, _ = QFileDialog.getOpenFileName(
            self._main_window,
            "Open Deck",
            start_dir,
            "Flashcard decks (*.db)",
        )
        if path_str:
            from pathlib import Path
            self._main_window.set_file(Path(path_str))
