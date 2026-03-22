# flashcards.adapter.in_.ui.configuration

import configparser
import importlib.resources
import os
import platform
from pathlib import Path


class Configuration:

    _SECTION = "flashcards"

    def __init__(self) -> None:
        self._config = configparser.ConfigParser()

        # Load bundled defaults from sample.properties
        sample = (
            importlib.resources.files("flashcards.adapter.in_.ui")
            .joinpath("resources")
            .joinpath("sample.properties")
        )
        content = sample.read_text(encoding="utf-8")
        self._config.read_string(f"[{self._SECTION}]\n" + content)

        # Overlay with user config file if it exists
        user_file = self._user_config_path()
        if user_file.exists():
            self._config.read(user_file, encoding="utf-8")

        s = self._SECTION
        self.CARD_ICON_PATH: str = self._config.get(s, "card_icon", fallback="/cardicon.png")
        self.X: int = self._config.getint(s, "x", fallback=100)
        self.Y: int = self._config.getint(s, "y", fallback=100)
        self.WIDTH: int = self._config.getint(s, "width", fallback=600)
        self.HEIGHT: int = self._config.getint(s, "height", fallback=400)
        self.TEXT_EDITOR: str = self._config.get(s, "text_editor", fallback="gvim")

    def save(self, key: str, value: str) -> None:
        self._config.set(self._SECTION, key, value)
        user_file = self._user_config_path()
        user_file.parent.mkdir(parents=True, exist_ok=True)
        with user_file.open("w", encoding="utf-8") as f:
            self._config.write(f)

    @staticmethod
    def _user_config_path() -> Path:
        system = platform.system()
        home = Path.home()
        if system == "Windows":
            app_data = os.environ.get("APPDATA")
            base = Path(app_data) if app_data else home
        elif system == "Darwin":
            base = home / "Library" / "Application Support"
        else:
            xdg = os.environ.get("XDG_CONFIG_HOME")
            base = Path(xdg) if xdg else home / ".config"
        return base / "flashcards" / "config.ini"
