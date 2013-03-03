package com.philhanna.flashcards.ui;

import java.awt.Component;

import javax.swing.JLabel;

import com.philhanna.flashcards.CardHistory;
import com.philhanna.flashcards.CardStatistics;
import com.philhanna.flashcards.session.SessionCard;
import com.philhanna.flashcards.ui.events.CardChangeEvent;
import com.philhanna.flashcards.ui.events.CardChangeListener;
import com.philhanna.flashcards.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.ui.events.SessionStateChangeListener;

/**
 * A container for a label that describes how the currently visible card
 * has been answered during this session. The card may have never been
 * answered (the normal case), or it may have been missed one or more
 * times previously, or answered correctly.
 */
public class CardStatisticsContainer implements SessionStateChangeListener,
      CardChangeListener {

   private JLabel label;

   /**
    * Creates a new CardStatisticsContainer
    * @param sc the session container
    */
   public CardStatisticsContainer(SessionContainer sc) {
      this.label = new JLabel();
      sc.addSessionStateChangeListener(this);
      sc.addCardChangeListener(this);
   }

   /**
    * Returns the underlying label component to the status panel
    * container
    */
   public Component getComponent() {
      return this.label;
   }

   /**
    * Makes the label visible or not depending on the session state
    * @param event the session state change event
    */
   public void sessionStateChanged(SessionStateChangeEvent event) {
      SessionState sessionState = event.getSessionState();
      switch (sessionState) {
         case REVIEW_MODE:
            label.setVisible(false);
            break;
         case ASKING_QUESTION:
            label.setVisible(true);
            break;
         case SHOWING_ANSWER:
            label.setVisible(true);
            break;
         case SESSION_COMPLETE:
            label.setVisible(false);
            break;
      }
   }

   /**
    * Builds the text of the label based on the current card
    * @param event a card change event
    */
   public void cardChanged(CardChangeEvent event) {

      // Get the new card

      SessionCard card = event.getCard();
      if (card == null) {
         return;
      }

      // Get the history for this card

      final CardStatistics stats = card.getStatistics();
      final CardHistory history = stats.getHistory();
      final int timesWrong = stats.getTimesAnsweredWrong();
      final StringBuffer sb = new StringBuffer();

      switch (history) {
         case NEVER_ANSWERED:
            label.setText("Not yet answered");
            break;
         case ANSWERED_RIGHT:
            sb.append("Last answered right");
            if (timesWrong == 1)
               sb.append(", missed once before");
            else if (timesWrong > 1)
               sb.append(", missed ").append(timesWrong)
                     .append(" times before");
            label.setText(sb.toString());
            break;
         case ANSWERED_WRONG:
            sb.append("Missed last time");
            if (timesWrong == 2)
               sb.append(", missed once before");
            else if (timesWrong > 3)
               sb.append(", missed ").append(timesWrong - 1).append(
                     " times before");
            label.setText(sb.toString());
      }
   }
}