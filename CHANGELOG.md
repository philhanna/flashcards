# Changelog

All notable changes to this project will be documented in this file.

---

## [3.0.0] — 2026-03-22

### Changed
- Ported entire application from Java + Swing to Python + PyQt6
- Replaced Maven/`pom.xml` build with `pyproject.toml` (setuptools)
- Replaced Swing UI components with PyQt6 equivalents (`QMainWindow`, `QWidget`, `QLabel`, etc.)
- Replaced Java listener interfaces with PyQt6 signals (`pyqtSignal`)
- Replaced `sqlite-jdbc` with stdlib `sqlite3`
- Replaced JUnit 5 with pytest
- Sample decks moved from `src/test/resources/` to `tests/resources/`

### Removed
- All Java source (`src/main/`, `src/test/`)
- Maven artifacts (`pom.xml`, `dependency-reduced-pom.xml`, `target/`)
- `tools/xml_to_sqlite.py` migration script
- Swing look-and-feel configuration (`look_and_feel` property)

---

## [2.1.0] — 2026-03-21

### Changed
- Dropped XML deck support; SQLite (`.db`) is now the only supported deck format
- Abstracted use cases behind the `StudySessionUseCase` port interface
- Improved test coverage to 82%

### Removed
- Logging from SQLite adapter
- Application-level logging
- Toggle feature

---

## [2.0.0] — 2026-03-21

### Added
- SQLite deck support via `sqlite-jdbc`
- OS-standard configuration file location (XDG on Linux, `APPDATA` on Windows, `~/Library/Application Support` on macOS), replacing Java Preferences API
- Sample `.db` deck files under `src/test/resources/`

### Changed
- Restructured package naming to reflect Ports and Adapters (Hexagonal) architecture
- Refactored to hexagonal architecture with clean `port/in` and `port/out` separation
- Renamed implementation files to modern conventions
- Switched test framework from JUnit 4 to JUnit 5

### Removed
- XML deck format and all associated parsing code
- Gradle build system (replaced by Maven)
- Gradle artifacts and Eclipse-specific project files

---

## [1.0.0] — 2022-07-30 through 2023-02-17

### Added
- Working Java + Swing desktop application
- Flip / Right / Wrong card interaction
- Review mode (browse without being quizzed)
- Session statistics summary at end of deck
- Configurable Swing look and feel (Metal, Nimbus, Motif, GTK)
- XML deck format (`.xml` / `.flc`)
- Maven build producing a self-contained fat JAR
- JUnit 4 unit tests
- `LICENCE` file

### Changed
- Converted build system from Gradle to Maven
- Converted file extension from `.flc` to `.xml`
- Refactored package structure

---

## [0.1.0] — 2018-04-19

### Added
- Initial Gradle-based project
- Core flashcard domain: `Card`, `Deck`, `Session`
- Basic Swing UI
- Eclipse project configuration
