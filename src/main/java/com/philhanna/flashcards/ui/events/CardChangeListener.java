package com.philhanna.flashcards.ui.events;

import java.util.EventListener;

/**
 * Defines an object that listens for card change events
 */
public interface CardChangeListener extends EventListener {
   
   /**
    * Invoked when the a new card has been displayed
    * @param event a card change event
    */
   public void cardChanged(CardChangeEvent event);
}