# How This Application Uses PyQt6

PyQt6 is a set of Python bindings for the Qt GUI framework. Qt is a mature C++ toolkit
that provides windows, buttons, labels, layouts, and an event system. PyQt6 wraps all
of that so you can use it from Python.

This document walks through every window object in the application and explains how they
fit together.

---

## Core Concepts

Before looking at the specific classes, three Qt ideas appear everywhere:

### Widgets

A *widget* is any visible UI element — a window, a button, a label, a checkbox.
Every widget in Qt has an optional *parent*. When a parent is destroyed, Qt automatically
destroys all its children. Passing `parent` to a widget's constructor establishes that
relationship:

```python
self._label = QLabel(self)   # self is the parent
```

### Layouts

Layouts control how child widgets are arranged inside a parent widget. This app uses:

- `QVBoxLayout` — stacks children top-to-bottom
- `QHBoxLayout` — places children left-to-right

You attach a layout to a widget and then add child widgets to the layout:

```python
layout = QVBoxLayout(self)   # self owns the layout
layout.addWidget(some_child)
```

### Signals and Slots

Signals are notifications that something happened. Slots are Python methods that respond
to them. You connect them with `.connect()`:

```python
button.clicked.connect(self.on_button_clicked)
```

When the button is clicked, Qt calls `on_button_clicked`. This is the primary way Qt
widgets communicate without being tightly coupled to each other.

You can also define your *own* signals using `pyqtSignal`. `SessionPanel` does this so
that all the child panels can react to state changes without calling each other directly.

---

## Application Entry Point

**`__main__.py`**

```python
app = QApplication(sys.argv)
window = MainWindow()
window.show()
sys.exit(app.exec())
```

`QApplication` is the Qt runtime. There must be exactly one. It owns the event loop
(`app.exec()`), which runs until the main window closes.

---

## The Widget Tree

```
QApplication
└── MainWindow  (QMainWindow)
    ├── QMenuBar
    │   ├── File menu  (QMenu)
    │   ├── View menu  (QMenu)
    │   └── Help menu  (QMenu)
    └── central widget  (swapped at runtime)
        ├── SessionPanel  (QWidget)          ← while studying
        │   ├── CardPanel  (QWidget)
        │   │   ├── question label  (QLabel)
        │   │   └── answer label    (QLabel)
        │   ├── ButtonPanel  (QWidget)
        │   │   ├── btn1  (QPushButton)   "Previous" / "Wrong"
        │   │   └── btn2  (QPushButton)   "Flip" / "Right" / "Next"
        │   └── StatusBar  (QFrame)
        │       ├── CardProgressPanel  (QWidget)
        │       │   └── label  (QLabel)
        │       ├── CardStatsPanel  (QWidget)
        │       │   └── label  (QLabel)
        │       └── ReviewModeCheckbox  (QCheckBox)
        └── SummaryPanel  (QWidget)          ← when session ends
            ├── statistics label  (QLabel)
            └── SummaryButtonPanel  (QWidget)
                ├── Restart  (QPushButton)
                └── Close    (QPushButton)
```

---

## Window Objects in Detail

### `MainWindow` — `QMainWindow`

**File:** `adapter/driving/ui/main_window.py`

`QMainWindow` is a special top-level widget that provides a menu bar, a status bar, and
a *central widget* slot. The central widget fills the main content area.

This class:

- Reads window position and size from `Configuration` and restores them on startup via
  `setGeometry()`.
- Calls `build_menu_bar()` to attach a `QMenuBar`.
- Sets a window icon loaded from a bundled PNG resource.
- Overrides `closeEvent()` to save window geometry back to config before the window
  closes.
- Swaps the central widget at runtime: when a deck is opened it calls
  `setCentralWidget(SessionPanel(...))`, and when the session ends it calls
  `setCentralWidget(SummaryPanel(...))`. The old widget is cleaned up with
  `deleteLater()`, which schedules destruction after the current event has finished
  processing.

### `QMenuBar` and `QMenu` / `QAction`

**Files:** `menus/app_menu_bar.py`, `menus/file_menu.py`, `menus/view_menu.py`,
`menus/help_menu.py`

`QMenuBar` is the horizontal bar at the top. `QMenu` is a single drop-down menu.
`QAction` is a clickable item within a menu.

```python
open_action = QAction("&Open...", main_window)
open_action.triggered.connect(main_window.do_open)
menu.addAction(open_action)
```

The `&` before a letter makes that letter the keyboard shortcut (Alt+O for Open on most
platforms). The `triggered` signal fires when the user clicks the item.

Two actions (`Edit` and `Restart`) start disabled and are enabled only when a deck is
loaded. References to them are stored as attributes on `MainWindow`
(`main_window.edit_action`, `main_window.restart_action`) so the window can toggle them.

### `SessionPanel` — `QWidget` with custom signals

**File:** `adapter/driving/ui/session_panel.py`

This is the hub of the study UI. It owns the `StudySession` use case and coordinates
all child panels through two custom signals:

```python
card_changed = pyqtSignal(object)
session_state_changed = pyqtSignal(object)
```

`SessionState` is an enum with four values:

| State | Meaning |
|---|---|
| `ASKING_QUESTION` | Card is face-up showing the question |
| `SHOWING_ANSWER` | Card is flipped; answer is visible |
| `REVIEW_MODE` | Browsing without being quizzed |
| `SESSION_COMPLETE` | All cards answered correctly |

Every child panel connects to one or both signals in its constructor. When the user
clicks a button, `SessionPanel` calls the use-case method, updates its internal state,
then emits the appropriate signal. All panels update themselves in response — no panel
talks to another panel directly.

The layout is a `QVBoxLayout` with `CardPanel` taking all the stretch (filling available
height) and a fixed-height lower strip containing `ButtonPanel` and `StatusBar`.

### `CardPanel` — `QWidget`

**File:** `adapter/driving/ui/card_panel.py`

Displays the question and answer as two `QLabel` widgets stacked vertically. Each label:

- Has a `QFrame.Shape.Box` border drawn around it.
- Is left- and top-aligned (`Qt.AlignmentFlag.AlignTop | AlignLeft`).
- Has `setWordWrap(True)` so long text wraps instead of being clipped.
- Uses `QSizePolicy.Policy.Ignored` on the horizontal axis, meaning it can shrink or
  grow freely to fill available space.

Text is rendered as HTML so that question history can be shown in color (green for
last answered right, red for last answered wrong).

The answer label is hidden (`setVisible(False)`) when the state is `ASKING_QUESTION`
and shown when the state is `SHOWING_ANSWER` or `REVIEW_MODE`.

### `ButtonPanel` — `QWidget`

**File:** `adapter/driving/ui/button_panel.py`

Two `QPushButton` widgets in a `QHBoxLayout`. The buttons change their labels and
behavior depending on the session state:

| State | btn1 | btn2 |
|---|---|---|
| `ASKING_QUESTION` | Previous | Flip |
| `SHOWING_ANSWER` | Wrong | Right |
| `REVIEW_MODE` | Previous | Next |

Both buttons use a single `clicked` signal each. The `_on_btn1` / `_on_btn2` methods
check `self._state` (kept current by listening to `session_state_changed`) to decide
which action to call on `SessionPanel`.

### `StatusBar` — `QFrame`

**File:** `adapter/driving/ui/status_bar.py`

A `QFrame` with `Shape.Panel` and `Shadow.Sunken` to give it a recessed look — the
standard Qt idiom for a status bar. It uses `QHBoxLayout` to place three child widgets
side by side: `CardProgressPanel`, `CardStatsPanel`, and `ReviewModeCheckbox`.

Note this is *not* Qt's built-in `QStatusBar` (which attaches to `QMainWindow`). It is
a plain `QFrame` used as a custom status area inside `SessionPanel`.

### `CardProgressPanel` — `QWidget`

**File:** `adapter/driving/ui/card_progress_panel.py`

A single bold `QLabel` showing "Card N of M. Average X.XX seconds per card."

The font weight is set programmatically:

```python
font = self._label.font()
font.setWeight(QFont.Weight.Bold)
self._label.setFont(font)
```

Updates on both `card_changed` and `session_state_changed`. Hides itself when the
session is complete.

### `CardStatsPanel` — `QWidget`

**File:** `adapter/driving/ui/card_stats_panel.py`

A single `QLabel` that shows text like "Last answered right, missed once before" for
the current card. It reads `card.statistics` on every `card_changed` event. Hides
itself during `REVIEW_MODE` and `SESSION_COMPLETE`.

### `ReviewModeCheckbox` — `QCheckBox`

**File:** `adapter/driving/ui/review_mode_checkbox.py`

A single checkbox labelled "Review mode". It subclasses `QCheckBox` directly.

It listens to two signals:

- Its own `stateChanged` — fires when the user ticks or unticks it, calls
  `session_panel.set_review_mode(self.isChecked())`.
- `SessionPanel.session_state_changed` — hides the checkbox when the session is
  complete.

### `SummaryPanel` — `QWidget`

**File:** `adapter/driving/ui/summary_panel.py`

Shown when the session ends, replacing `SessionPanel` as the central widget. Contains:

- A `QLabel` that renders an HTML statistics table built by `build_statistics_html()`.
  `QLabel` supports a subset of HTML including tables, so the statistics are formatted
  as an HTML `<table>` and passed directly to `label.setText()`.
- A `SummaryButtonPanel` with Restart and Close buttons.

### `SummaryButtonPanel` — `QWidget`

**File:** `adapter/driving/ui/summary_button_panel.py`

Two `QPushButton` widgets in an `QHBoxLayout`. Restart calls
`session_panel.restart()`, which tells `MainWindow` to reload the same file from disk.
Close calls `session_panel.close_session()`, which calls `main_window.set_file(None)`
to clear the central widget.

### `QFileDialog` (used by `OpenDialogHandler`)

**File:** `adapter/driving/ui/open_dialog_handler.py`

`QFileDialog.getOpenFileName()` is a static convenience method that shows the OS
native file-picker dialog and returns the selected path (or an empty string if the user
cancelled). The `"Flashcard decks (*.db)"` argument is the file filter shown in the
dialog.

---

## Signal Flow Summary

Here is how a typical user action — clicking **Right** — flows through the system:

```
User clicks "Right" button
  → ButtonPanel._on_btn2()
    → SessionPanel.mark_right()
      → StudySession.mark_right()        # domain use case
        → card.mark_right()             # marks statistics
        → DeckSession.next_card()       # advances cursor
      → if deck exhausted:
          SessionPanel._set_state(SESSION_COMPLETE)
            → SessionPanel._display_statistics()
              → MainWindow.setCentralWidget(SummaryPanel)
        else:
          SessionPanel._fire_card_change()
            → session_panel.card_changed.emit(card)   ← signal
              → CardPanel._on_card_changed()           ← slot
              → CardProgressPanel._on_card_changed()   ← slot
              → CardStatsPanel._on_card_changed()      ← slot
          SessionPanel._set_state(ASKING_QUESTION)
            → session_panel.session_state_changed.emit(state)  ← signal
              → CardPanel._on_state_changed()                  ← slot
              → ButtonPanel._on_state_changed()                ← slot
              → CardProgressPanel._on_state_changed()          ← slot
              → CardStatsPanel._on_state_changed()             ← slot
              → ReviewModeCheckbox._on_session_state_changed() ← slot
```

Each panel updates itself independently. `SessionPanel` never needs to know which
panels exist or call them directly — the signal broadcasts to whoever connected.
