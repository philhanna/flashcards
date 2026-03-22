# Changelog

All notable changes to this project will be documented in this file.

---

## [Unreleased] — Python + PyQt6 port (branch: python)

### Added
- `conversion.md` — phased plan for porting the application from Java + Swing to Python + PyQt6
- Phase 1 scaffolding: `pyproject.toml`, Python package skeleton under `src/flashcards/`, `.venv`, sample decks copied to `tests/resources/`
- Phase 2 domain layer: `exceptions`, `card_history`, `card_statistics`, `card`, `deck`, `session_cursor`, `session_card`, `tracked_card`, `session`, `deck_session` — pure Python with no GUI or I/O dependencies
- Phase 3 persistence layer: `port/deck_loader.py`, `adapter/out/sqlite/sqlite_deck.py`, `adapter/out/sqlite/sqlite_deck_loader.py` — SQLite-backed `Deck` using stdlib `sqlite3`
- Phase 4 use case layer: `port/study_session_use_case.py`, `usecase/study_session.py` — coordinates `Session` and `SessionCard` to implement mark-right/wrong, navigation
- Phase 5 configuration: `adapter/in_/ui/configuration.py` — loads bundled `sample.properties` via `importlib.resources`, overlays OS-appropriate user config (XDG/APPDATA/Library), persists changes with `save()`

---

## [2.1.1] — 2026-03-21

### Changed
- Updated `pom.xml` build configuration

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
