package com.philhanna.flashcards.domain;

import com.philhanna.flashcards.domain.session.Session;
import com.philhanna.flashcards.domain.session.SessionCard;
import com.philhanna.flashcards.port.in.StudySessionUseCase;

/**
 * Application service that implements the {@link StudySessionUseCase} inbound
 * port. Coordinates domain objects ({@link Session}, {@link SessionCard}) to
 * carry out the core study-session use cases, keeping all domain logic out of
 * the UI adapter.
 */
public class StudySession implements StudySessionUseCase {

   private final Session session;

   public StudySession(Session session) {
      this.session = session;
   }

   @Override
   public void markRight() {
      SessionCard card = session.getCurrentCard();
      if (card != null)
         card.markRight();
      session.nextCard();
   }

   @Override
   public void markWrong() {
      SessionCard card = session.getCurrentCard();
      if (card != null) {
         card.markWrong();
         session.rotate();
         session.shuffle();
      }
   }

   @Override
   public void nextCard() {
      session.nextCard();
   }

   @Override
   public void previousCard() {
      session.previousCard();
   }

   @Override
   public Session getSession() {
      return session;
   }
}
