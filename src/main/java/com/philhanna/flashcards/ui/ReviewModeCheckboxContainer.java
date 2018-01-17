package com.philhanna.flashcards.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import com.philhanna.flashcards.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.ui.events.SessionStateChangeListener;

/**
 * A container for a check box that turns review mode on and off
 */
public class ReviewModeCheckboxContainer implements ItemListener,
      SessionStateChangeListener {

   private SessionContainer sc;
   private JCheckBox cb;

   /**
    * Creates a new ReviewModeCheckboxContainer
    * @param sc
    */
   public ReviewModeCheckboxContainer(SessionContainer sc) {
      this.sc = sc;
      sc.setReviewMode(false);
      this.cb = new JCheckBox("Review mode");
      this.cb.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
      this.cb.setSelected(false);
      this.cb.addItemListener(this);
      sc.addSessionStateChangeListener(this);
   }

   /**
    * Returns the contained check box
    */
   public Component getComponent() {
      return this.cb;
   }

   /**
    * Turns review mode on or off when the state of the check box checks
    */
   public void itemStateChanged(ItemEvent e) {
      switch (e.getStateChange()) {
         case 1:
            sc.setReviewMode(true);
            break;
         case 2:
            sc.setReviewMode(false);
            break;
      }
   }

   /**
    * Makes the review mode checkbox visible or not, depending on the
    * session state. It is not visible when the session is complete, but
    * is visible during all other states
    */
   public void sessionStateChanged(SessionStateChangeEvent event) {
      SessionState sessionState = event.getSessionState();
      switch (sessionState) {
         case REVIEW_MODE:
            cb.setVisible(true);
            break;
         case ASKING_QUESTION:
            cb.setVisible(true);
            break;
         case SHOWING_ANSWER:
            cb.setVisible(true);
            break;
         case SESSION_COMPLETE:
            cb.setVisible(false);
            break;
      }
   }
}
