# flashcards.adapter.driving.ui.menus.help_menu

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtGui import QAction
from PyQt6.QtWidgets import QMenu

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow


def build_help_menu(main_window: MainWindow) -> QMenu:
    menu = QMenu("&Help", main_window)

    about_action = QAction("&About...", main_window)
    about_action.triggered.connect(main_window.do_help_about)
    menu.addAction(about_action)

    return menu
