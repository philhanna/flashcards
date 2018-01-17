package com.philhanna.flashcards.deck;

import com.philhanna.flashcards.ApplicationException;

/**
 * Thrown while parsing an XML deck when a card is improperly formed.
 * @see DeckImpl#getCardsFromXML(org.w3c.dom.Document)
 */
public class InvalidCardException extends ApplicationException {
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new invalid card exception with the specified error message
    * @param msg the error message
    */
   public InvalidCardException(String msg) {
      super(msg);
   }
}