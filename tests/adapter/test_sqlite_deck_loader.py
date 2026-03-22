# tests.adapter.test_sqlite_deck_loader

from pathlib import Path

import pytest

from flashcards.adapter.driven.sqlite.sqlite_deck_loader import SqliteDeckLoader
from flashcards.domain.exceptions import ApplicationException, EmptyDeckException


RESOURCES = Path(__file__).parent.parent / "resources"


@pytest.fixture
def loader():
    return SqliteDeckLoader()


def test_load_returns_deck_with_title(loader):
    deck = loader.load(RESOURCES / "Shakespeare.db")
    assert deck.title != ""


def test_load_returns_deck_with_cards(loader):
    deck = loader.load(RESOURCES / "Shakespeare.db")
    assert len(deck.cards) > 0


def test_load_cards_have_non_empty_questions(loader):
    deck = loader.load(RESOURCES / "NBA.db")
    for card in deck.cards:
        assert card.question.strip() != ""


def test_load_cards_have_non_empty_answers(loader):
    deck = loader.load(RESOURCES / "NBA.db")
    for card in deck.cards:
        assert card.answer.strip() != ""


def test_load_multiple_decks(loader):
    for name in ("Shakespeare.db", "Jane_Austen.db", "Norse_Mythology.db", "Best_Picture_Awards.db"):
        deck = loader.load(RESOURCES / name)
        assert len(deck.cards) > 0


def test_load_raises_for_missing_file(loader, tmp_path):
    with pytest.raises(ApplicationException):
        loader.load(tmp_path / "nonexistent.db")


def test_load_raises_for_invalid_sqlite(loader, tmp_path):
    bad_file = tmp_path / "bad.db"
    bad_file.write_bytes(b"this is not sqlite data")
    with pytest.raises(ApplicationException):
        loader.load(bad_file)


def test_load_raises_for_empty_deck(loader, tmp_path):
    import sqlite3
    db_path = tmp_path / "empty.db"
    conn = sqlite3.connect(db_path)
    conn.execute("CREATE TABLE deck (title TEXT)")
    conn.execute("INSERT INTO deck VALUES ('Empty')")
    conn.execute("CREATE TABLE card (id INTEGER, question TEXT, answer TEXT, position INTEGER)")
    conn.commit()
    conn.close()
    with pytest.raises(EmptyDeckException):
        loader.load(db_path)
