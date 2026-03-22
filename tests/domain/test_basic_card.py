# tests.domain.test_basic_card

import pytest

from flashcards.domain.card import BasicCard


def test_question_property():
    card = BasicCard("What is 2+2?", "4")
    assert card.question == "What is 2+2?"


def test_answer_property():
    card = BasicCard("What is 2+2?", "4")
    assert card.answer == "4"


def test_question_and_answer_are_independent():
    card = BasicCard("Q", "A")
    assert card.question != card.answer
