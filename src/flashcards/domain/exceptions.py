# flashcards.domain.exceptions


class ApplicationException(Exception):
    pass


class EmptyDeckException(ApplicationException):
    pass


class InvalidCardException(ApplicationException):
    pass
