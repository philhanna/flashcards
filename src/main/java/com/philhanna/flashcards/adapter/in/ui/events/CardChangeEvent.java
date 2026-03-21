package com.philhanna.flashcards.adapter.in.ui.events;

import com.philhanna.flashcards.domain.session.SessionCard;
import com.philhanna.flashcards.adapter.in.ui.SessionPanel;

import java.util.EventObject;

/**
 * An event fired when a different card is displayed.
 * @see com.philhanna.flashcards.adapter.in.ui.CardProgressPanel
 * @see com.philhanna.flashcards.adapter.in.ui.CardStatsPanel
 * @see com.philhanna.flashcards.adapter.in.ui.CardPanel
 * @see com.philhanna.flashcards.adapter.in.ui.SessionPanel
 */
public class CardChangeEvent extends EventObject {

   private static final long serialVersionUID = 1L;
   private SessionCard card;

   /**
    * Creates a new CardChangeEvent
    * @param source
    * @param card
    */
   public CardChangeEvent(SessionPanel source, SessionCard card) {
      super(source);
      this.card = card;
   }

   /**
    * Returns the underlying card.
    * @see com.philhanna.flashcards.adapter.in.ui.CardStatsPanel#cardChanged(CardChangeEvent)
    */
   public SessionCard getCard() {
      return card;
   }
}