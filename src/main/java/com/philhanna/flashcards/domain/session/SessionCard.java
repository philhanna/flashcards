package com.philhanna.flashcards.domain.session;

import com.philhanna.flashcards.domain.Card;
import com.philhanna.flashcards.domain.CardStatistics;

/**
 * An extension of the <code>Card</code> interface that adds functions to
 * return the statistics, and to mark a card as being answered right or
 * wrong in this session.
 * @see CardStatistics
 */
public interface SessionCard extends Card {
   
   /**
    * Returns the card statistics object
    */
   public CardStatistics getStatistics();

   /**
    * Marks a card as having been answered correctly in this session
    */
   public void markRight();

   /**
    * Marks a card as having been answered incorrectly in this session
    */
   public void markWrong();
}