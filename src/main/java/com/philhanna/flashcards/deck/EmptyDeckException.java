package com.philhanna.flashcards.deck;

import com.philhanna.flashcards.ApplicationException;

/**
 * Thrown when there are no cards in a deck
 * @see DeckImpl#getCardsFromXML(Document)
 */
public class EmptyDeckException extends ApplicationException {
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new empty deck exception with the specified error message
    * @param msg the error message
    */
   public EmptyDeckException(String msg) {
      super(msg);
   }
}
