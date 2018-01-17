package com.philhanna.flashcards.ui;

import com.philhanna.flashcards.session.*;
import com.philhanna.flashcards.ui.events.CardChangeEvent;
import com.philhanna.flashcards.ui.events.CardChangeListener;
import com.philhanna.flashcards.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.ui.events.SessionStateChangeListener;

import java.awt.Component;
import java.awt.Font;
import java.text.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Contains a GUI component that displays "Card i of n" in a bold font. Updated
 * as the user moves through the deck, obtaining this information from the
 * session cursor.
 * @see SessionCursor
 */
public class CardNumbersContainer implements SessionStateChangeListener,
      CardChangeListener {

   private static final DecimalFormat SECONDS_FMT = new DecimalFormat("#0.##");

   private JLabel label;
   private SessionContainer sc;

   /**
    * Creates a new CardNumbersContainer
    * @param sc the session container
    */
   public CardNumbersContainer(SessionContainer sc) {
      this.sc = sc;

      this.label = new JLabel();
      this.label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

      // Use a bold font of the same style and size

      Font oldFont = this.label.getFont();
      String fontName = oldFont.getName();
      int fontSize = oldFont.getSize();
      Font newFont = new Font(fontName, 1, fontSize);
      this.label.setFont(newFont);

      // Listen for both session change events and card change events

      sc.addSessionStateChangeListener(this);
      sc.addCardChangeListener(this);
   }

   /**
    * Visually updates the cards viewed and total cards numbers
    */
   public void updateNumbers() {
      Session session = this.sc.getSession();
      SessionCursor cursor = session.getCursor();
      int vc = cursor.getViewedCardCount();
      int tc = cursor.getTotalCardCount();
      double numer = session.getElapsedTime();
      double denom = session.getCardViewCount();
      double spc = numer / denom;
      final String template = "Card {0} of {1}. Average {2} seconds per card.";
      Object[] args = { Integer.valueOf(vc + 1), Integer.valueOf(tc),
            SECONDS_FMT.format(spc) };
      String text = MessageFormat.format(template, args);
      this.label.setText(text);
   }

   /**
    * Returns the contained label to the status panel container
    */
   public Component getComponent() {
      return this.label;
   }

   /**
    * Makes the label visible during all states except session complete
    * @param event a session state change event
    */
   public void sessionStateChanged(SessionStateChangeEvent event) {
      SessionState sessionState = event.getSessionState();
      switch (sessionState) {
         case REVIEW_MODE:
            label.setVisible(true);
            break;
         case ASKING_QUESTION:
            label.setVisible(true);
            break;
         case SHOWING_ANSWER:
            label.setVisible(true);
            break;
         case SESSION_COMPLETE:
            label.setVisible(false);
      }
   }

   /**
    * Invokes the update numbers method when the card is changed.
    * @param event a card change event
    */
   public void cardChanged(CardChangeEvent event) {
      updateNumbers();
   }
}
