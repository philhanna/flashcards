package com.philhanna.flashcards.ui.events;

import com.philhanna.flashcards.session.SessionCard;
import com.philhanna.flashcards.ui.SessionContainer;

import java.util.EventObject;

/**
 * An event fired when a different card is displayed.
 * @see com.philhanna.flashcards.ui.CardNumbersContainer
 * @see com.philhanna.flashcards.ui.CardStatisticsContainer
 * @see com.philhanna.flashcards.ui.QandAPanelContainer
 * @see com.philhanna.flashcards.ui.SessionContainer
 */
public class CardChangeEvent extends EventObject {

   private static final long serialVersionUID = 1L;
   private SessionCard card;

   /**
    * Creates a new CardChangeEvent
    * @param source
    * @param card
    */
   public CardChangeEvent(SessionContainer source, SessionCard card) {
      super(source);
      this.card = card;
   }

   /**
    * Returns the underlying card.
    * @see com.philhanna.flashcards.ui.CardStatisticsContainer#cardChanged(CardChangeEvent)
    */
   public SessionCard getCard() {
      return card;
   }
}