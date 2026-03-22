# Flashcards — Java → Python + PyQt6 Conversion Plan

## Key Mapping Reference

| Java | Python |
|---|---|
| `interface` | `ABC` / `Protocol` |
| `enum` | `enum.Enum` |
| `@dataclass`-style class | `@dataclass` |
| `Stack<T>` | `list` (used as stack via `.append`/`.pop`) |
| `Properties` | `configparser.ConfigParser` |
| Custom `Listener` interfaces | PyQt6 `pyqtSignal` / plain callables |
| `JFrame` | `QMainWindow` |
| `JPanel` | `QWidget` |
| `BorderLayout` / `BoxLayout` | `QVBoxLayout` / `QHBoxLayout` |
| `GridLayout` | `QGridLayout` |
| `FlowLayout` | `QHBoxLayout` with `AlignCenter` |
| `JButton` | `QPushButton` |
| `JLabel` | `QLabel` |
| `JCheckBox` | `QCheckBox` |
| `JTable` | `QTableWidget` |
| `JMenuBar` | `QMenuBar` |
| `JMenu` | `QMenu` |
| `JMenuItem` | `QAction` |
| `JOptionPane` | `QMessageBox` / `QFileDialog` |
| `ImageIcon` | `QIcon` / `QPixmap` |
| `sqlite-jdbc` | `sqlite3` (stdlib) |
| Maven / `pom.xml` | `pyproject.toml` + `pip` |
| JUnit 5 | `pytest` |

---

## Phase 1 — Project Setup

- [ ] Create `pyproject.toml` with project metadata (`name`, `version`, `requires-python = ">=3.11"`)
- [ ] Add runtime dependency: `PyQt6`
- [ ] Add dev dependencies: `pytest`, `pytest-qt`, `mypy`
- [ ] Create package layout under `src/flashcards/`:
  ```
  src/flashcards/
  ├── domain/
  │   └── session/
  ├── port/
  ├── usecase/
  └── adapter/
      ├── out/sqlite/
      └── in_/ui/
          └── menus/
  tests/
  ```
- [ ] Add `__init__.py` files to every package, each with a dotted-name comment (e.g., `# flashcards.domain`)
- [ ] Copy the five `.db` sample decks from `src/test/resources/` into `tests/resources/`
- [ ] Copy `cardicon.png` into `src/flashcards/adapter/in_/ui/resources/`
- [ ] Create `src/flashcards/adapter/in_/ui/resources/sample.properties` (port from `src/main/resources/`)
- [ ] Verify `pytest` discovers and runs an empty test

---

## Phase 2 — Domain Layer

Port the pure-logic classes. No GUI or I/O dependencies. All Java interfaces become Python `ABC`s.

- [ ] `domain/exceptions.py` — `ApplicationException`, `EmptyDeckException`, `InvalidCardException`
- [ ] `domain/card_history.py` — `CardHistory(enum.Enum)` with `NEVER_ANSWERED`, `ANSWERED_RIGHT`, `ANSWERED_WRONG`
- [ ] `domain/card_statistics.py` — `CardStatistics` dataclass (right/wrong counts per card)
- [ ] `domain/card.py` — `Card(ABC)` with abstract `question`/`answer` properties; `BasicCard` concrete implementation
- [ ] `domain/deck.py` — `Deck(ABC)` with abstract `title` property and `cards` sequence
- [ ] `domain/session/session_cursor.py` — `SessionCursor(ABC)`: `total_card_count`, `unviewed_card_count`, `viewed_card_count`
- [ ] `domain/session/session_card.py` — `SessionCard(ABC)`: exposes `Card` plus `CardStatistics` and `CardHistory`
- [ ] `domain/session/tracked_card.py` — `TrackedCard(SessionCard)`: tracks right/wrong for a single `Card`
- [ ] `domain/session/session.py` — `Session(ABC)`: `next_card()`, `previous_card()`, `current_card`, `cursor`, `rotate()`, `shuffle()`, `elapsed_time`, `card_view_count`, `record_card_view()`
- [ ] `domain/session/deck_session.py` — `DeckSession(Session)`:
  - Two-stack model: `_unviewed: list[SessionCard]`, `_viewed: list[SessionCard]`
  - `shuffle()` using `random.shuffle`
  - `rotate()` moves top of `_unviewed` to bottom (index 0)
  - `elapsed_time` via `time.monotonic()`
  - Inner `_Cursor` implements `SessionCursor`

---

## Phase 3 — Persistence Layer

- [ ] `port/deck_loader.py` — `DeckLoader(ABC)` with abstract `load(path: Path) -> Deck`
- [ ] `adapter/out/sqlite/sqlite_deck.py` — `SqliteDeck(Deck)`:
  - Constructor takes a `sqlite3.Connection`
  - Reads `deck` table for title, `card` table for questions/answers ordered by `position`
- [ ] `adapter/out/sqlite/sqlite_deck_loader.py` — `SqliteDeckLoader(DeckLoader)`:
  - Opens connection with `sqlite3.connect(path)` and returns `SqliteDeck`
  - Raises `ApplicationException` on missing file or bad schema

---

## Phase 4 — Use Case Layer

- [ ] `port/study_session_use_case.py` — `StudySessionUseCase(ABC)`: `mark_right()`, `mark_wrong()`, `next_card()`, `previous_card()`, `session` property
- [ ] `usecase/study_session.py` — `StudySession(StudySessionUseCase)`:
  - Wraps a `Session`
  - `mark_right()` → calls `session.next_card()` after recording
  - `mark_wrong()` → calls `session.rotate()` so the card re-queues
  - Port directly from `StudySession.java`

---

## Phase 5 — Configuration

- [ ] `adapter/in_/ui/configuration.py` — `Configuration`:
  - Load bundled `sample.properties` from package resources (`importlib.resources`)
  - Overlay with OS-appropriate user config file (XDG on Linux, `APPDATA` on Windows, `~/Library/Application Support` on macOS)
  - Use `configparser.ConfigParser` for reading/writing
  - Expose constants: `X`, `Y`, `WIDTH`, `HEIGHT`, `TEXT_EDITOR`, `CARD_ICON_PATH`
  - `save(key, value)` persists back to the user config file

---

## Phase 6 — Event System

Replace Java `CardChangeListener` / `SessionStateChangeListener` with PyQt6 signals.

- [ ] `adapter/in_/ui/session_state.py` — `SessionState(enum.Enum)`: `ASKING_QUESTION`, `SHOWING_ANSWER`, `REVIEW_MODE`, `SESSION_COMPLETE`
- [ ] Define `card_changed = pyqtSignal(object)` and `session_state_changed = pyqtSignal(SessionState)` as signals on `SessionPanel` (a `QWidget` subclass)
- [ ] Remove the separate `events/` package — signals replace `CardChangeEvent`, `CardChangeListener`, `SessionStateChangeEvent`, `SessionStateChangeListener`

---

## Phase 7 — UI Layer

Each Java class becomes a `QWidget` subclass (or `QDialog` / `QAction` where appropriate). The `getComponent()` pattern disappears — widgets *are* the component.

### 7a — Menu System

- [ ] `menus/app_menu_bar.py` — build `QMenuBar`; attach to `QMainWindow` via `setMenuBar()`
- [ ] `menus/file_menu.py` — `QMenu("&File")` with Open, Restart, Edit, Exit `QAction`s
- [ ] `menus/view_menu.py` — `QMenu("&View")` with Reset Size `QAction`
- [ ] `menus/help_menu.py` — `QMenu("&Help")` with About `QAction`
- [ ] Replace `AbstractMenuItem` / `AbstractMenu` base classes with direct `QAction` construction (no base class needed)
- [ ] Replace `MenuItems` enum (global menu item registry) with direct references held on the main window

### 7b — Main Window

- [ ] `adapter/in_/ui/main_window.py` — `MainWindow(QMainWindow)`:
  - Constructor: set title, restore geometry from `Configuration`, set window icon
  - `closeEvent()` → save window geometry before closing
  - `do_open()`, `do_restart()`, `do_edit()`, `do_reset_size()`, `do_help_about()`
  - `set_file(path)` → clears central widget, starts new `SessionPanel`
  - `get_last_directory()` / `set_last_directory()` → read/write config

### 7c — Session Panel

- [ ] `adapter/in_/ui/session_panel.py` — `SessionPanel(QWidget)`:
  - Signals: `card_changed = pyqtSignal(object)`, `session_state_changed = pyqtSignal(SessionState)`
  - Layout: `CardPanel` in center, lower `QVBoxLayout` with `ButtonPanel` + `StatusBar`
  - `flip()`, `mark_right()`, `mark_wrong()`, `next_card()`, `previous_card()`
  - `set_review_mode(bool)`
  - `display_statistics()` — swaps central widget to `SummaryPanel`
  - `restart()` → calls `main_window.set_file(path)`

### 7d — Card Panel

- [ ] `adapter/in_/ui/card_panel.py` — `CardPanel(QWidget)`:
  - Two `QLabel` areas (question top, answer bottom)
  - Connects to `session_panel.card_changed` and `session_panel.session_state_changed`
  - Hides answer label when state is `ASKING_QUESTION`; shows it on `SHOWING_ANSWER` / `REVIEW_MODE`
  - Handles resize via `resizeEvent()`

### 7e — Button Panel

- [ ] `adapter/in_/ui/button_panel.py` — `ButtonPanel(QWidget)`:
  - Three `QPushButton`s: Flip / Right / Wrong (labels and enabled state driven by session state)
  - Connects to `session_panel.session_state_changed`
  - Connects button clicks to `session_panel.flip()`, `.mark_right()`, `.mark_wrong()`

### 7f — Status Bar

- [ ] `adapter/in_/ui/status_bar.py` — `StatusBar(QWidget)`:
  - `QLabel` showing "Card N of M"
  - Connects to `session_panel.card_changed` to refresh

### 7g — Card Stats and Progress Panels

- [ ] `adapter/in_/ui/card_stats_panel.py` — `CardStatsPanel(QWidget)`:
  - Shows right/wrong counts for the current card
  - Connects to `session_panel.card_changed`
- [ ] `adapter/in_/ui/card_progress_panel.py` — `CardProgressPanel(QWidget)`:
  - Shows overall session progress (viewed / total)
  - Connects to both `card_changed` and `session_state_changed`

### 7h — Summary / End-of-Session

- [ ] `adapter/in_/ui/statistics_table.py` — `StatisticsTable`:
  - Returns an HTML string (or builds a `QTableWidget`) summarising per-card right/wrong stats
  - Port `getStatisticsAsHTML(Session)` directly
- [ ] `adapter/in_/ui/summary_panel.py` — `SummaryPanel(QWidget)`:
  - Displays statistics table and elapsed time
  - Contains `SummaryButtonPanel`
- [ ] `adapter/in_/ui/summary_button_panel.py` — `SummaryButtonPanel(QWidget)`:
  - Restart and Close `QPushButton`s
  - Connect to `session_panel.restart()` and `session_panel.close()`

### 7i — Review Mode and Open Dialog

- [ ] `adapter/in_/ui/review_mode_checkbox.py` — `ReviewModeCheckbox(QCheckBox)`:
  - `stateChanged` signal → `session_panel.set_review_mode(bool)`
- [ ] `adapter/in_/ui/open_dialog_handler.py` — `OpenDialogHandler`:
  - Use `QFileDialog.getOpenFileName()` filtering `*.db`
  - Calls `main_window.set_file(path)` on selection

---

## Phase 8 — Entry Point

- [ ] `src/flashcards/__main__.py`:
  ```python
  import sys
  from PyQt6.QtWidgets import QApplication
  from flashcards.adapter.in_.ui.main_window import MainWindow

  def main():
      app = QApplication(sys.argv)
      window = MainWindow()
      if len(sys.argv) > 1:
          window.set_file(sys.argv[1])
      window.show()
      sys.exit(app.exec())
  ```
- [ ] Add `[project.scripts] flashcards = "flashcards.__main__:main"` to `pyproject.toml`

---

## Phase 9 — Tests

Port all six test classes to pytest. Use `tmp_path` fixture in place of `BaseTest` setup/teardown.

- [ ] `tests/__init__.py`
- [ ] `tests/base_test.py` — shared `deck_of(*args)` helper that builds a `BasicCard` list and wraps it in a `Deck`
- [ ] `tests/test_basic_card.py` — constructor and getter assertions
- [ ] `tests/test_tracked_card.py` — answer tracking, statistics, `CardHistory` transitions
- [ ] `tests/test_deck_session.py` — navigation, shuffle, rotate, cursor counts
- [ ] `tests/test_sqlite_deck.py` — load from sample `.db` files; error on bad path
- [ ] `tests/test_study_session.py` — `mark_right`, `mark_wrong`, navigation through `StudySessionUseCase`
- [ ] Confirm coverage ≥ 82% (`pytest --cov=flashcards`)

---

## Phase 10 — Packaging and Distribution

- [ ] Confirm `pip install -e .` works and `flashcards` command launches the app
- [ ] Add `PyInstaller` as a dev dependency
- [ ] Write a `build.sh` (or `Makefile` target) that produces a standalone executable:
  ```
  pyinstaller --onefile --windowed --name flashcards src/flashcards/__main__.py
  ```
- [ ] Verify the bundled binary finds `cardicon.png` and sample decks correctly
- [ ] Update `README.md` with new build and run instructions
