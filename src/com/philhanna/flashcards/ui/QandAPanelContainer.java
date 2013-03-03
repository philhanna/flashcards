package com.philhanna.flashcards.ui;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.philhanna.flashcards.CardHistory;
import com.philhanna.flashcards.session.SessionCard;
import com.philhanna.flashcards.ui.events.CardChangeEvent;
import com.philhanna.flashcards.ui.events.CardChangeListener;
import com.philhanna.flashcards.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.ui.events.SessionStateChangeListener;

/**
 * A container created by the SessionContainer for a panel containing a
 * question and answer. The answer may or may not be hidden, depending
 * on the session state.
 * <p>
 * <img src="doc-files/QandAPanelContainer-1.png"/>
 * </p>
 * First, it constructs a {@link javax.swing.JPanel} with a
 * {@link javax.swing.BoxLayout}, then adds two text area containers
 * with borders titled "Question" and "Answer". The question area is
 * shown in different colors depending on whether it was answered
 * correctly the last time it was seen in this session.
 * <p>
 * Listens for card change and session state change events. When the
 * card changes, the panel needs to use new question and answer text.
 * When the session state changes, the visibility of the question and
 * answer fields may change.
 * @see com.philhanna.flashcards.ui.SessionContainer
 * @see com.philhanna.flashcards.session.SessionCard
 * @see com.philhanna.flashcards.CardHistory
 */
public class QandAPanelContainer implements SessionStateChangeListener,
      CardChangeListener {

   // ==========================================================
   // Instance variables
   // ==========================================================

   private JPanel panel;
   private JLabel lblQuestion;
   private JLabel lblAnswer;
   private SessionContainer sc;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new Q&amp;A panel container in the specified session
    * container.
    * @param sc the session container
    */
   public QandAPanelContainer(SessionContainer sc) {
      this.sc = sc;
      this.panel = new JPanel();
      this.panel.setLayout(new BoxLayout(this.panel, 1));
      this.panel.add(this.lblQuestion = createQuestionComponent());
      this.panel.add(this.lblAnswer = createAnswerComponent());
      sc.addSessionStateChangeListener(this);
      sc.addCardChangeListener(this);
      sc.getFrame().addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            updateText();
         }
      });
   }

   // ==========================================================
   // Internal methods
   // ==========================================================

   /**
    * Creates the question component
    */
   private JLabel createQuestionComponent() {
      JLabel label = new JLabel();
      label.setBorder(BorderFactory.createTitledBorder("Question"));
      return label;
   }

   /**
    * Creates the answer component
    */
   private JLabel createAnswerComponent() {
      JLabel label = new JLabel();
      label.setBorder(BorderFactory.createTitledBorder("Answer"));
      return label;
   }

   /**
    * Updates the question and answer text from the current session card
    */
   private void updateText() {

      // Get the current card in this session. If there is none, does
      // nothing

      final SessionCard card = this.sc.getSession().getCurrentCard();
      if (card == null) {
         return;
      }

      // Make the text width 20 pixels less than the frame width

      final int width = sc.getWidth() - 20;

      // Construct the text for the question box

      final String questionText = card.getQuestion();
      final CardHistory lastAnswer = card.getStatistics().getHistory();
      lblQuestion
            .setText(generateQuestionHTML(questionText, lastAnswer, width));

      // Construct the text for the answer box

      final String answer = card.getAnswer();
      lblAnswer.setText(generateAnswerHTML(answer, width));

   }

   /**
    * Generates question text in HTML format. Declared static and
    * package visible so that unit tests can be written.
    */
   static String generateQuestionHTML(
         String questionText,
         CardHistory lastAnswer,
         int width) {

      final StringBuffer sb = new StringBuffer();
      final StringBuffer style = new StringBuffer();

      sb.append("<html>");
      sb.append("<table width=").append(width).append(">");
      sb.append("<tr>");
      sb.append("<td");
      style.append("font-size: 16pt;");
      switch (lastAnswer) {
         case NEVER_ANSWERED:
            break;
         case ANSWERED_RIGHT:
            style.append("color: #00FF00;");
            break;
         case ANSWERED_WRONG:
            style.append("color: #FF0000;");
      }

      style.append("background: #FFFFFF;");
      sb.append(" style='").append(style).append("'");
      sb.append(">");
      sb.append(questionText);
      sb.append("</td>");
      sb.append("</tr>");
      sb.append("</table></html>");
      final String s = sb.toString();
      return s;
   }

   /**
    * Generates answer text in HTML format. Declared static and package
    * visible so that unit tests can be written.
    */
   static String generateAnswerHTML(String answer, int width) {

      final StringBuffer sb = new StringBuffer();
      final StringBuffer style = new StringBuffer();

      sb.append("<html>");
      sb.append("<table width=").append(width).append(">");
      sb.append("<tr>");
      sb.append("<td");
      style.setLength(0);
      style.append("font-size: 16pt;");
      style.append("background: #FFFFFF;");
      sb.append(" style='").append(style).append("'");
      sb.append(">");
      sb.append(answer);
      sb.append("</td>");
      sb.append("</tr>");
      sb.append("</table></html>");
      final String s = sb.toString();
      return s;
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Changes the visibility of the question and answers when the
    * session state changes
    * @param event the session state change event
    */
   public void sessionStateChanged(SessionStateChangeEvent event) {
      SessionState sessionState = event.getSessionState();
      switch (sessionState) {
         case REVIEW_MODE:
            lblQuestion.setVisible(true);
            lblAnswer.setVisible(true);
            break;
         case ASKING_QUESTION:
            lblQuestion.setVisible(true);
            lblAnswer.setVisible(false);
            break;
         case SHOWING_ANSWER:
            lblQuestion.setVisible(true);
            lblAnswer.setVisible(true);
            break;
         case SESSION_COMPLETE:
            lblQuestion.setVisible(false);
            lblAnswer.setVisible(false);
      }

      updateText();
   }

   /**
    * Updates the text when the card is changed
    * @param event the card change event
    */
   public void cardChanged(CardChangeEvent event) {
      updateText();
   }

   /**
    * Returns the panel contained by this component so that it can be
    * added to the main frame
    */
   public Component getComponent() {
      return this.panel;
   }
}
