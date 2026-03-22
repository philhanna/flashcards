# tests.usecase.test_study_session

import pytest
from typing import Sequence

from flashcards.domain.card import BasicCard, Card
from flashcards.domain.card_history import CardHistory
from flashcards.domain.deck import Deck
from flashcards.domain.session.deck_session import DeckSession
from flashcards.usecase.study_session import StudySession


class _SimpleDeck(Deck):
    def __init__(self, cards: list[Card]) -> None:
        self._cards = cards

    @property
    def title(self) -> str:
        return "Test Deck"

    @property
    def cards(self) -> Sequence[Card]:
        return self._cards


def _make_session(*questions: str) -> StudySession:
    cards = [BasicCard(q, f"Answer to {q}") for q in questions]
    deck_session = DeckSession(_SimpleDeck(cards))
    return StudySession(deck_session)


@pytest.fixture
def two_card_study():
    return _make_session("Q1", "Q2")


@pytest.fixture
def single_card_study():
    return _make_session("Q1")


# --- session property ---

def test_session_property_returns_session(two_card_study):
    assert two_card_study.session is not None


# --- mark_right ---

def test_mark_right_advances_to_next_card(two_card_study):
    two_card_study.mark_right()
    assert two_card_study.session.cursor.viewed_card_count == 1
    assert two_card_study.session.cursor.unviewed_card_count == 1


def test_mark_right_sets_card_history(two_card_study):
    current = two_card_study.session.current_card
    two_card_study.mark_right()
    viewed = two_card_study.session.viewed_cards[-1]
    assert viewed is current
    assert viewed.statistics.history == CardHistory.ANSWERED_RIGHT


def test_mark_right_on_last_card_exhausts_deck(single_card_study):
    single_card_study.mark_right()
    assert single_card_study.session.current_card is None


def test_mark_right_when_no_card_does_not_raise(single_card_study):
    single_card_study.mark_right()  # exhaust deck
    single_card_study.mark_right()  # no current card — should not raise


# --- mark_wrong ---

def test_mark_wrong_keeps_card_in_unviewed(two_card_study):
    two_card_study.mark_wrong()
    assert two_card_study.session.cursor.viewed_card_count == 0
    assert two_card_study.session.cursor.unviewed_card_count == 2


def test_mark_wrong_sets_card_history(two_card_study):
    current_before = two_card_study.session.current_card
    two_card_study.mark_wrong()
    assert current_before.statistics.history == CardHistory.ANSWERED_WRONG


def test_mark_wrong_total_count_unchanged(two_card_study):
    two_card_study.mark_wrong()
    assert two_card_study.session.cursor.total_card_count == 2


# --- next_card / previous_card ---

def test_next_card_advances(two_card_study):
    two_card_study.next_card()
    assert two_card_study.session.cursor.viewed_card_count == 1


def test_previous_card_goes_back(two_card_study):
    two_card_study.next_card()
    two_card_study.previous_card()
    assert two_card_study.session.cursor.viewed_card_count == 0
