# flashcards.adapter.driving.ui.main_window

from __future__ import annotations

import importlib.resources
import subprocess
from pathlib import Path

from PyQt6.QtGui import QIcon
from PyQt6.QtWidgets import QMainWindow, QMessageBox

from flashcards.adapter.driving.ui.configuration import Configuration
from flashcards.adapter.driving.ui.menus.app_menu_bar import build_menu_bar


class MainWindow(QMainWindow):

    VERSION = "2.1.1"

    def __init__(self) -> None:
        super().__init__()
        self._config = Configuration()
        self._path: Path | None = None
        self._session_panel = None

        self.setWindowTitle("Flashcards")
        self.setGeometry(
            self._config.X, self._config.Y,
            self._config.WIDTH, self._config.HEIGHT,
        )

        icon_ref = (
            importlib.resources.files("flashcards.adapter.driving.ui")
            .joinpath("resources")
            .joinpath("cardicon.png")
        )
        with importlib.resources.as_file(icon_ref) as icon_path:
            self.setWindowIcon(QIcon(str(icon_path)))

        build_menu_bar(self)

    def closeEvent(self, event) -> None:
        geo = self.geometry()
        self._config.save("x", str(geo.x()))
        self._config.save("y", str(geo.y()))
        self._config.save("width", str(geo.width()))
        self._config.save("height", str(geo.height()))
        event.accept()

    def do_open(self) -> None:
        from flashcards.adapter.driving.ui.open_dialog_handler import OpenDialogHandler
        OpenDialogHandler(self).run()

    def do_restart(self) -> None:
        if self._session_panel is not None:
            self._session_panel.restart()

    def do_edit(self) -> None:
        if self._path is not None:
            subprocess.Popen([self._config.TEXT_EDITOR, str(self._path)])

    def do_reset_size(self) -> None:
        self._config.save("x", str(self._config.X))
        self._config.save("y", str(self._config.Y))
        self._config.save("width", str(self._config.WIDTH))
        self._config.save("height", str(self._config.HEIGHT))
        self.setGeometry(
            self._config.X, self._config.Y,
            self._config.WIDTH, self._config.HEIGHT,
        )

    def do_help_about(self) -> None:
        QMessageBox.information(
            self,
            "About Flashcards",
            f"Flashcards {self.VERSION}\n"
            "A computer-based flash cards program\n"
            "Phil Hanna - ph1204@gmail.com",
        )

    def set_file(self, path: Path | None) -> None:
        self._path = path
        old = self.centralWidget()
        if old is not None:
            old.deleteLater()
        self._session_panel = None

        if path is None:
            self.setWindowTitle("Flashcards")
            self.edit_action.setEnabled(False)
            self.restart_action.setEnabled(False)
        else:
            self._save_last_directory(path)
            self._start_session(path)

    def _start_session(self, path: Path) -> None:
        from flashcards.adapter.driven.sqlite.sqlite_deck_loader import SqliteDeckLoader
        from flashcards.adapter.driving.ui.session_panel import SessionPanel
        from flashcards.domain.exceptions import ApplicationException
        try:
            deck = SqliteDeckLoader().load(path)
            self.setWindowTitle(f"Flashcards - {deck.title}")
            self.edit_action.setEnabled(True)
            self.restart_action.setEnabled(True)
            self._session_panel = SessionPanel(self, deck)
            self.setCentralWidget(self._session_panel)
        except ApplicationException as e:
            QMessageBox.critical(self, "Error", str(e))

    def get_last_directory(self) -> Path:
        dir_str = self._config._config.get(
            self._config._SECTION, "directory", fallback="."
        )
        p = Path(dir_str)
        return p if p.is_dir() else Path(".")

    def _save_last_directory(self, path: Path) -> None:
        directory = path.parent if path.is_file() else path
        self._config.save("directory", str(directory))
