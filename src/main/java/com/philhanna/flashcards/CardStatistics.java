package com.philhanna.flashcards;

/**
 * Statistics about the user history with this card during this session.
 */
public interface CardStatistics {
   /**
    * Returns whether the question was never answered, answered correctly, or
    * answered incorrectly the last time it was asked during this session.
    */
   public CardHistory getHistory();

   /**
    * Returns the number of time this question was answered correctly during
    * this session
    */
   public int getTimesAnsweredRight();

   /**
    * Returns the number of time this question was answered incorrectly during
    * this session
    */
   public int getTimesAnsweredWrong();
}