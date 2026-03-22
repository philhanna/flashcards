# flashcards.adapter.driving.ui.menus.view_menu

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtGui import QAction
from PyQt6.QtWidgets import QMenu

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow


def build_view_menu(main_window: MainWindow) -> QMenu:
    menu = QMenu("&View", main_window)

    reset_action = QAction("&Reset Size", main_window)
    reset_action.triggered.connect(main_window.do_reset_size)
    menu.addAction(reset_action)

    return menu
