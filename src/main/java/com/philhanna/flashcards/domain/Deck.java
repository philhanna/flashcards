package com.philhanna.flashcards.domain;

import java.util.List;

/**
 * A deck of flash cards having a title and a list of cards.
 */
public interface Deck {

   /**
    * Returns the deck title
    */
   public String getTitle();

   /**
    * Returns an ordered list of the cards in the deck
    */
   public List<Card> getCards();
}
