package com.philhanna.flashcards.session;

import java.util.List;


/**
 * One pass through the deck. Has methods that keep track of the current
 * card and the progress through the deck.
 * @see SessionCard
 * @see SessionCursor
 */
public interface Session {

   /**
    * Returns the current card showing
    * @return a session card object
    */
   public SessionCard getCurrentCard();

   /**
    * Returns the cursor object for this session, which lets us know how
    * far we have gone in the deck
    * @return the session cursor
    */
   public SessionCursor getCursor();

   /**
    * Moves forward in the deck to the next card
    */
   public void nextCard();

   /**
    * Moves backward in the deck to the previous card
    */
   public void previousCard();

   /**
    * Shuffles the deck
    */
   public void shuffle();

   /**
    * Reinserts a missed card in the deck
    */
   public void rotate();

   /**
    * Returns the cards that have been viewed in the current session
    */
   public List<SessionCard> getViewedCards();
   
   /**
    * Returns the number of seconds that have elapsed since the start of the session
    */
   public double getElapsedTime();
   
   /**
    * Returns the number of times cards have been presented for viewing
    */
   public int getCardViewCount();
}