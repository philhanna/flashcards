# flashcards.adapter.driving.ui.session_state

import enum


class SessionState(enum.Enum):
    ASKING_QUESTION = "ASKING_QUESTION"
    SHOWING_ANSWER = "SHOWING_ANSWER"
    REVIEW_MODE = "REVIEW_MODE"
    SESSION_COMPLETE = "SESSION_COMPLETE"
