# flashcards.__main__

import sys

from PyQt6.QtWidgets import QApplication

from flashcards.adapter.driving.ui.main_window import MainWindow


def main() -> None:
    app = QApplication(sys.argv)
    window = MainWindow()
    if len(sys.argv) > 1:
        from pathlib import Path
        window.set_file(Path(sys.argv[1]))
    window.show()
    sys.exit(app.exec())


if __name__ == "__main__":
    main()
