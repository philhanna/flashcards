package com.philhanna.flashcards.session;

/**
 * Information about the session - total number of cards, number viewed,
 * number not viewed.
 * @see SessionImpl
 */
public interface SessionCursor {
   
   /**
    * Returns the number of cards in the deck
    */
   public int getTotalCardCount();

   /**
    * Returns the number of cards not yet seen
    */
   public int getUnviewedCardCount();

   /**
    * Returns the number of cards already seen
    */
   public int getViewedCardCount();
}