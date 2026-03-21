package com.philhanna.flashcards.domain;

import java.util.List;


/**
 * A deck of flash cards having a title and a list of cards. The deck can be
 * toggled between showing the answers or showing the questions.
 * @see XmlDeck
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

   /**
    * Swaps the state of the deck between showing answers or showing questions
    */
   public void toggle();
}