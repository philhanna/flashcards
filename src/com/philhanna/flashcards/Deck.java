package com.philhanna.flashcards;

import java.util.List;

import com.philhanna.flashcards.deck.DeckImpl;

/**
 * A deck of flash cards having a title and a list of cards. The deck can be
 * toggled between showing the answers or showing the questions.
 * @see DeckImpl
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