# flashcards.adapter.driving.ui.statistics_table

from __future__ import annotations

from typing import TYPE_CHECKING

from flashcards.domain.session.session import Session

if TYPE_CHECKING:
    pass


def build_statistics_html(session: Session) -> str:
    cards = session.viewed_cards
    n = len(cards)
    denom = float(n)
    count = [0, 0, 0, 0]

    for card in cards:
        missed = card.statistics.times_answered_wrong
        denom += missed
        count[min(missed, 3)] += 1

    rating = n / denom if denom else 1.0
    avg = session.elapsed_time / session.card_view_count if session.card_view_count else 0.0

    def row_int(label: str, k: int) -> str:
        pct = k / n if n else 0.0
        return (
            f"<tr><td align='left'>{label}</td>"
            f"<td align='right'>{k}</td>"
            f"<td align='right'>{pct:.2%}</td></tr>"
        )

    def row_str(label: str, value: str) -> str:
        return (
            f"<tr><td colspan='2' align='left'>{label}</td>"
            f"<td align='right'>{value}</td></tr>"
        )

    sep = "<tr><td colspan='3'><hr/></td></tr>"

    return (
        "<html>"
        "<head><style>td {font-size: 14pt;}</style></head>"
        "<table border='0' cellpadding='0' cellspacing='3' width='500'>"
        + row_int("Number of cards:", n)
        + row_int("Number answered correctly:", count[0])
        + row_int("Number missed once:", count[1])
        + row_int("Number missed twice:", count[2])
        + row_int("Number missed more than twice:", count[3])
        + row_str("Average time per card:", f"{avg:.2f} seconds")
        + sep
        + row_str("Rating:", f"{rating:.2%}")
        + "</table></html>"
    )
