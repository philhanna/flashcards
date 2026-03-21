package com.philhanna.flashcards.port.in;

import com.philhanna.flashcards.domain.session.Session;

/**
 * Inbound port: use cases for studying a flashcard deck.
 * The UI calls these methods; domain logic lives in the implementing service.
 */
public interface StudySessionUseCase {

   /** Marks the current card correct and advances to the next card */
   void markRight();

   /** Marks the current card incorrect and re-queues it */
   void markWrong();

   /** Advances to the next card */
   void nextCard();

   /** Goes back to the previous card */
   void previousCard();

   /** Returns the underlying session for read-only UI queries */
   Session getSession();
}
