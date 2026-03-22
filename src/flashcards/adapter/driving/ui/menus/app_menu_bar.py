# flashcards.adapter.driving.ui.menus.app_menu_bar

from __future__ import annotations

from typing import TYPE_CHECKING

from PyQt6.QtWidgets import QMenuBar

from flashcards.adapter.driving.ui.menus.file_menu import build_file_menu
from flashcards.adapter.driving.ui.menus.help_menu import build_help_menu
from flashcards.adapter.driving.ui.menus.view_menu import build_view_menu

if TYPE_CHECKING:
    from flashcards.adapter.driving.ui.main_window import MainWindow


def build_menu_bar(main_window: MainWindow) -> None:
    mb = QMenuBar(main_window)
    mb.addMenu(build_file_menu(main_window))
    mb.addMenu(build_view_menu(main_window))
    mb.addMenu(build_help_menu(main_window))
    main_window.setMenuBar(mb)
