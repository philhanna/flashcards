package com.philhanna.flashcards.ui.events;

import java.util.EventObject;

import com.philhanna.flashcards.ui.SessionContainer;
import com.philhanna.flashcards.ui.SessionState;

/**
 * An event fired when the session state changes. Many parts of the
 * application listen for these events.
 * @see SessionState
 */
public class SessionStateChangeEvent extends EventObject {
   private static final long serialVersionUID = 1L;
   private SessionState sessionState;

   /**
    * Creates a new SessionStateChangeEvent
    * @param source the session container that fired the event
    * @param sessionState the new session state
    */
   public SessionStateChangeEvent(
         SessionContainer source,
         SessionState sessionState) {
      super(source);
      this.sessionState = sessionState;
   }

   /**
    * Returns the new session state
    */
   public SessionState getSessionState() {
      return sessionState;
   }
}
