# flashcards.domain.card

from abc import ABC, abstractmethod


class Card(ABC):

    @property
    @abstractmethod
    def question(self) -> str: ...

    @property
    @abstractmethod
    def answer(self) -> str: ...


class BasicCard(Card):

    def __init__(self, question: str, answer: str) -> None:
        self._question = question
        self._answer = answer

    @property
    def question(self) -> str:
        return self._question

    @property
    def answer(self) -> str:
        return self._answer
