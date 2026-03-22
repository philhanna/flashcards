# flashcards.__main__

import argparse
import sys

from PyQt6.QtWidgets import QApplication

from flashcards.adapter.driving.ui.main_window import MainWindow


def main() -> None:
    parser = argparse.ArgumentParser(
        prog="flashcards",
        description="A computer-based flash cards program.",
    )
    parser.add_argument(
        "deck",
        nargs="?",
        metavar="DECK",
        help="SQLite deck file (.db) to open on startup",
    )
    args = parser.parse_args()

    app = QApplication(sys.argv)
    window = MainWindow()
    if args.deck:
        from pathlib import Path
        window.set_file(Path(args.deck))
    window.show()
    sys.exit(app.exec())


if __name__ == "__main__":
    main()
