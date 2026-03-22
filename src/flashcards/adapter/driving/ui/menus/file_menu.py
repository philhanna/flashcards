# flashcards.adapter.driving.ui.menus.file_menu

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtGui import QAction
from PyQt6.QtWidgets import QMenu

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow


def build_file_menu(main_window: MainWindow) -> QMenu:
    menu = QMenu("&File", main_window)

    open_action = QAction("&Open...", main_window)
    open_action.triggered.connect(main_window.do_open)
    menu.addAction(open_action)

    edit_action = QAction("&Edit", main_window)
    edit_action.triggered.connect(main_window.do_edit)
    edit_action.setEnabled(False)
    menu.addAction(edit_action)
    main_window.edit_action = edit_action

    restart_action = QAction("&Restart", main_window)
    restart_action.triggered.connect(main_window.do_restart)
    restart_action.setEnabled(False)
    menu.addAction(restart_action)
    main_window.restart_action = restart_action

    menu.addSeparator()

    exit_action = QAction("E&xit", main_window)
    exit_action.triggered.connect(main_window.close)
    menu.addAction(exit_action)

    return menu
