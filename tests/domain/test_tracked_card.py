# tests.domain.test_tracked_card

import pytest

from flashcards.domain.card import BasicCard
from flashcards.domain.card_history import CardHistory
from flashcards.domain.session.tracked_card import TrackedCard


@pytest.fixture
def card():
    return TrackedCard(BasicCard("Capital of France?", "Paris"))


def test_inherits_question(card):
    assert card.question == "Capital of France?"


def test_inherits_answer(card):
    assert card.answer == "Paris"


def test_initial_history_is_never_answered(card):
    assert card.statistics.history == CardHistory.NEVER_ANSWERED


def test_initial_right_count_is_zero(card):
    assert card.statistics.times_answered_right == 0


def test_initial_wrong_count_is_zero(card):
    assert card.statistics.times_answered_wrong == 0


def test_mark_right_updates_history(card):
    card.mark_right()
    assert card.statistics.history == CardHistory.ANSWERED_RIGHT


def test_mark_right_increments_right_count(card):
    card.mark_right()
    assert card.statistics.times_answered_right == 1


def test_mark_right_does_not_change_wrong_count(card):
    card.mark_right()
    assert card.statistics.times_answered_wrong == 0


def test_mark_wrong_updates_history(card):
    card.mark_wrong()
    assert card.statistics.history == CardHistory.ANSWERED_WRONG


def test_mark_wrong_increments_wrong_count(card):
    card.mark_wrong()
    assert card.statistics.times_answered_wrong == 1


def test_mark_wrong_does_not_change_right_count(card):
    card.mark_wrong()
    assert card.statistics.times_answered_right == 0


def test_mark_right_multiple_times_accumulates(card):
    card.mark_right()
    card.mark_right()
    card.mark_right()
    assert card.statistics.times_answered_right == 3


def test_mark_wrong_multiple_times_accumulates(card):
    card.mark_wrong()
    card.mark_wrong()
    assert card.statistics.times_answered_wrong == 2


def test_history_reflects_last_answer(card):
    card.mark_right()
    card.mark_wrong()
    assert card.statistics.history == CardHistory.ANSWERED_WRONG
