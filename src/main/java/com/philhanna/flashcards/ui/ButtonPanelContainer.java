package com.philhanna.flashcards.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.philhanna.flashcards.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.ui.events.SessionStateChangeListener;

/**
 * A container created by the SessionContainer for a panel containing
 * two buttons. The buttons will change text and meaning, depending on
 * the session state.
 * <p>
 * First, it constructs a {@link javax.swing.JPanel} with its default
 * flow layout, then creates an inner panel that actually contains the
 * buttons.
 * <p>
 * The button panel container listens for {@link ActionListener} events
 * (which happen when buttons are clicked) and for
 * {@link SessionStateChangeEvent} events.
 * <ul>
 * <li>When a card is presented, the buttons have the "previous" and
 * "flip" states.</li>
 * <li>If the user flips the card, the answer is shown and the button
 * states change to ask the user whether the answer was wrong or right</li>
 * <li>In review mode, the buttons become just "previous" and "next"</li>
 * <li>When all the cards have been seen, and the summary table is being
 * displayed, the buttons allow the user to restart this deck or close
 * it and open a new one</li>
 * </ul>
 * @see StatisticsButtonContainer
 */
public class ButtonPanelContainer implements ActionListener,
      SessionStateChangeListener {

   // ==========================================================
   // Class variables and constants
   // ==========================================================

   private static final String PREVIOUS = "Previous";
   private static final String NEXT = "Next";
   private static final String FLIP = "Flip";
   private static final String WRONG = "Wrong";
   private static final String RIGHT = "Right";

   // ==========================================================
   // Instance variables
   // ==========================================================

   private JPanel panel;
   private JButton button1;
   private JButton button2;
   private SessionContainer sc;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new ButtonPanelContainer
    * @param sc the parent session container
    */
   public ButtonPanelContainer(SessionContainer sc) {
      this.sc = sc;
      this.panel = new JPanel();
      this.panel.add(createInnerPanel());

      sc.addSessionStateChangeListener(this);
   }

   // ==========================================================
   // Private methods used only in this class
   // ==========================================================

   private JPanel createInnerPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1, 2, 10, 10));

      this.button1 = new JButton();
      this.button1.addActionListener(this);
      panel.add(this.button1);

      this.button2 = new JButton();
      this.button2.addActionListener(this);
      panel.add(this.button2);
      
      // Make button 2 the default
      
      sc.getFrame().getRootPane().setDefaultButton(button2);

      return panel;
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Returns the contained panel to the parent {@link SessionContainer}
    */
   public Component getComponent() {
      return this.panel;
   }

   /**
    * Handles all button click events
    */
   public void actionPerformed(ActionEvent e) {
      String text = e.getActionCommand();
      if (text.equals(PREVIOUS))
         this.sc.previousCard();
      else if (text.equals(NEXT))
         this.sc.nextCard();
      else if (text.equals(FLIP))
         this.sc.flip();
      else if (text.equals(WRONG))
         this.sc.markWrong();
      else if (text.equals(RIGHT))
         this.sc.markRight();
   }

   /**
    * Handles a session state change event by changing the text of the
    * buttons
    * @param event the session state change event
    */
   public void sessionStateChanged(SessionStateChangeEvent event) {
      SessionState sessionState = event.getSessionState();
      switch (sessionState) {
         case REVIEW_MODE:
            this.button1.setText(PREVIOUS);
            this.button2.setText(NEXT);
            break;
         case ASKING_QUESTION:
            this.button1.setText(PREVIOUS);
            this.button2.setText(FLIP);
            break;
         case SHOWING_ANSWER:
            this.button1.setText(WRONG);
            this.button2.setText(RIGHT);
            break;
         default:
            throw new RuntimeException("Unexpected state change");
      }
   }
}
