# tests.domain.test_deck_session

import pytest
from typing import Sequence

from flashcards.domain.card import BasicCard, Card
from flashcards.domain.deck import Deck
from flashcards.domain.session.deck_session import DeckSession


class _SimpleDeck(Deck):
    def __init__(self, cards: list[Card]) -> None:
        self._cards = cards

    @property
    def title(self) -> str:
        return "Test Deck"

    @property
    def cards(self) -> Sequence[Card]:
        return self._cards


def _make_deck(*questions: str) -> Deck:
    return _SimpleDeck([BasicCard(q, f"Answer to {q}") for q in questions])


@pytest.fixture
def single_card_session():
    return DeckSession(_make_deck("Q1"))


@pytest.fixture
def three_card_session():
    return DeckSession(_make_deck("Q1", "Q2", "Q3"))


# --- cursor counts ---

def test_cursor_total_count(three_card_session):
    assert three_card_session.cursor.total_card_count == 3


def test_cursor_unviewed_count_initially_equals_total(three_card_session):
    assert three_card_session.cursor.unviewed_card_count == 3


def test_cursor_viewed_count_is_zero_initially(three_card_session):
    assert three_card_session.cursor.viewed_card_count == 0


# --- current_card ---

def test_current_card_is_not_none_when_cards_remain(three_card_session):
    assert three_card_session.current_card is not None


def test_current_card_is_none_after_all_cards_viewed(single_card_session):
    single_card_session.next_card()
    assert single_card_session.current_card is None


# --- next_card ---

def test_next_card_moves_card_to_viewed(three_card_session):
    three_card_session.next_card()
    assert three_card_session.cursor.viewed_card_count == 1
    assert three_card_session.cursor.unviewed_card_count == 2


def test_next_card_total_count_unchanged(three_card_session):
    three_card_session.next_card()
    assert three_card_session.cursor.total_card_count == 3


def test_next_card_on_empty_unviewed_does_nothing(single_card_session):
    single_card_session.next_card()  # exhaust
    single_card_session.next_card()  # should be a no-op
    assert single_card_session.cursor.viewed_card_count == 1


# --- previous_card ---

def test_previous_card_moves_card_back_to_unviewed(three_card_session):
    three_card_session.next_card()
    three_card_session.previous_card()
    assert three_card_session.cursor.viewed_card_count == 0
    assert three_card_session.cursor.unviewed_card_count == 3


def test_previous_card_on_empty_viewed_does_nothing(three_card_session):
    three_card_session.previous_card()  # nothing viewed yet — no-op
    assert three_card_session.cursor.unviewed_card_count == 3


# --- rotate ---

def test_rotate_does_not_change_card_counts(three_card_session):
    three_card_session.rotate()
    assert three_card_session.cursor.total_card_count == 3
    assert three_card_session.cursor.unviewed_card_count == 3


def test_rotate_on_single_card_keeps_same_current(single_card_session):
    before = single_card_session.current_card
    single_card_session.rotate()
    assert single_card_session.current_card is before


def test_rotate_changes_current_card(three_card_session):
    before = three_card_session.current_card
    three_card_session.rotate()
    assert three_card_session.current_card is not before


# --- record_card_view ---

def test_card_view_count_starts_at_zero(three_card_session):
    assert three_card_session.card_view_count == 0


def test_record_card_view_increments_count(three_card_session):
    three_card_session.record_card_view()
    three_card_session.record_card_view()
    assert three_card_session.card_view_count == 2


# --- elapsed_time ---

def test_elapsed_time_is_non_negative(three_card_session):
    assert three_card_session.elapsed_time >= 0


# --- viewed_cards ---

def test_viewed_cards_is_empty_initially(three_card_session):
    assert three_card_session.viewed_cards == []


def test_viewed_cards_grows_after_next_card(three_card_session):
    three_card_session.next_card()
    assert len(three_card_session.viewed_cards) == 1
