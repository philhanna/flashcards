package com.philhanna.flashcards.ui.events;

import java.util.EventListener;

/**
 * Defines an object that listens for session state change events
 */
public interface SessionStateChangeListener extends EventListener {

   /**
    * Invoked when the session state changes
    * @param event a session state change event
    */
   public void sessionStateChanged(SessionStateChangeEvent event);
}