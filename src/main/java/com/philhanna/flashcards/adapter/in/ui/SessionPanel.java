package com.philhanna.flashcards.adapter.in.ui;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.philhanna.flashcards.domain.*;
import com.philhanna.flashcards.domain.session.*;
import com.philhanna.flashcards.port.in.StudySessionUseCase;
import com.philhanna.flashcards.port.out.DeckLoader;
import com.philhanna.flashcards.adapter.in.ui.events.CardChangeEvent;
import com.philhanna.flashcards.adapter.in.ui.events.CardChangeListener;
import com.philhanna.flashcards.adapter.in.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.adapter.in.ui.events.SessionStateChangeListener;
import com.philhanna.flashcards.adapter.in.ui.menus.MenuItems;

/**
 * An object that creates and manages the GUI components and statistics
 * for a pass through a card deck.
 * <p>
 * Starts by parsing the card deck file using an {@link XmlDeck} and
 * creating a {@link Session} for it. A session provides methods for
 * moving forward and backward through the deck and maintains statistics
 * about right and wrong answers.
 * <p>
 * <img src="doc-files/SessionPanel-1.png"/>
 * <p>
 * After creating the session, this object then creates the GUI
 * components to represent the session in the frame:
 * <ul>
 * <li>A question and answer panel ({@link CardPanel}) at the
 * center. This has an upper panel that shows the text of the question,
 * and a lower panel that shows the answer. This lower panel is not
 * shown until the user clicks the <code>Flip</code> button</li>
 * <li>A {@link ButtonPanel} and {@link StatusBar}
 * at the bottom. The button panel contains two buttons, which change
 * labels according to the state of the session.</li>
 * </ul>
 * At the end of the deck, the session statistics panel is displayed
 * @see XmlDeck
 * @see Session
 * @see DeckSession
 * @see ButtonPanel
 * @see StatusBar
 * @see SessionStateChangeListener
 * @see SessionStateChangeEvent
 * @see CardChangeListener
 * @see CardChangeEvent
 * @see CardPanel
 * @see SummaryPanel
 */
public class SessionPanel {

   // ==========================================================
   // Class variables and constants
   // ==========================================================

   private Main main;
   private JPanel panel;
   private StudySessionUseCase studySession;
   private SessionState sessionState = SessionState.ASKING_QUESTION;
   private ButtonPanel bpc;
   private StatusBar spc;
   private List<SessionStateChangeListener> sessionStateChangeListeners = new ArrayList<SessionStateChangeListener>();
   private List<CardChangeListener> cardChangeListeners = new ArrayList<CardChangeListener>();
   private CardPanel qap;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new session container for the specified file and
    * switches its viewing mode from showing questions to showing
    * answers.
    * @param main the main program
    * @param file the file to be opened
    * @throws ApplicationException
    */
   public SessionPanel(Main main, DeckLoader deckLoader, File file)
         throws ApplicationException {

      // Save the reference to the main program

      this.main = main;

      // Turn on the "restart" and "edit" menu items

      MenuItems.EDIT.getMenuItem().setEnabled(true);
      MenuItems.RESTART.getMenuItem().setEnabled(true);

      // Create a new deck object by parsing the specified file

      Deck deck = deckLoader.load(file);

      // Set this deck's name in the frame title

      updateFrameTitle(deck);

      // Create a new session using this deck

      this.studySession = new StudySession(new DeckSession(deck));

      // Create a new panel with a border layout

      this.panel = new JPanel();
      this.panel.setLayout(new BorderLayout());

      // Add a Q&A panel in the center

      this.qap = new CardPanel(this);
      this.panel.add(this.qap.getComponent(), "Center");

      // Add a lower panel at the bottom

      JPanel lowerPanel = new JPanel();
      this.panel.add(lowerPanel, "South");
      lowerPanel.setLayout(new BoxLayout(lowerPanel, 1));

      // Add a button panel to the lower panel

      this.bpc = new ButtonPanel(this);
      lowerPanel.add(this.bpc.getComponent());

      // Add a status panel to the lower panel

      this.spc = new StatusBar(this);
      lowerPanel.add(this.spc.getComponent());

      // Start things up by firing a card change event and a session
      // state changes event

      fireCardChange();
      fireSessionStateChange();

      // Validate the main frame so that it shows the contents

      main.getFrame().validate();
   }

   // ==========================================================
   // Internal methods
   // ==========================================================

   /**
    * Updates the title in the frame to be "Flashcards - [deck title]"
    * @param deck the deck
    */
   private void updateFrameTitle(Deck deck) {
      final StringBuffer sb = new StringBuffer();
      sb.append("Flashcards - ");
      sb.append(deck.getTitle());
      final String title = sb.toString();
      main.setTitle(title);
   }

   /**
    * Fires a card change event to all listeners
    */
   private void fireCardChange() {
      studySession.getSession().recordCardView();
      SessionCard card = studySession.getSession().getCurrentCard();
      CardChangeEvent event = new CardChangeEvent(this, card);
      for (CardChangeListener listener : cardChangeListeners) {
         listener.cardChanged(event);
      }
   }

   /**
    * Fires a session state change event to all listeners
    */
   private void fireSessionStateChange() {
      SessionStateChangeEvent event = new SessionStateChangeEvent(
            this,
            sessionState);
      for (SessionStateChangeListener listener : sessionStateChangeListeners) {
         listener.sessionStateChanged(event);
      }
   }

   /**
    * Sets the session state to its new value
    * @param newState the new session state
    */
   private void setSessionState(SessionState newState) {
      sessionState = newState;
      if (newState != SessionState.SESSION_COMPLETE) {
         fireSessionStateChange();
         return;
      }
      displayStatistics();
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Adds a {@link CardChangeListener} to the list of those that will
    * receive notification from this session
    * <ul>
    * <li>when a new card is displayed,
    * <li>when it is flipped over, or
    * <li>when it is marked right or wrong.
    * </ul>
    * @param listener the card change listener. This includes
    *        {@link CardProgressPanel},
    *        {@link CardStatsPanel}, and
    *        {@link CardPanel}.
    */
   public void addCardChangeListener(CardChangeListener listener) {
      cardChangeListeners.add(listener);
   }

   /**
    * Adds a {@link SessionStateChangeListener} to the list of those
    * that will receive notification from this session when
    * <ul>
    * <li>A card is flipped over</li>
    * <li>A card is marked right or wrong</li>
    * <li>The "show answers" checkbox is selected or unselected</li>
    * </ul>
    * @param listener the session state change listener. This includes
    *        {@link ButtonPanel} {@link CardProgressPanel}
    *        {@link CardStatsPanel} {@link CardPanel}
    */
   public void addSessionStateChangeListener(SessionStateChangeListener listener) {
      sessionStateChangeListeners.add(listener);
   }

   /**
    * Closes the file. Called when the <code>close</code> button is
    * clicked.
    * @see SummaryButtonPanel
    */
   public void close() {
      main.setFile(null);
   }

   /**
    * When the user clicks the flip button, this method fires a session
    * state change event indicating that the session state has changed
    * to <code>SHOWING_ANSWER</code>.
    * @see ButtonPanel
    */
   public void flip() {
      setSessionState(SessionState.SHOWING_ANSWER);
   }

   /**
    * Returns the main panel contained by this component. Called in Main
    * when a new session is started so that this panel can be added to
    * the main frame and validated
    * @see Main
    */
   public JPanel getComponent() {
      return panel;
   }
   
   /**
    * Returns the main object
    */
   public Main getMain() {
      return main;
   }

   /**
    * Convenience method that surfaces the main application frame so
    * that the question and answer panel can add a component listener to
    * handle resize events.
    * @see CardPanel#CardPanel(SessionPanel)
    */
   public JFrame getFrame() {
      return main.getFrame();
   }

   /**
    * Called in a number of places by lower-level components to get the
    * contained session.
    * @see CardProgressPanel
    * @see CardPanel
    * @see StatisticsTable
    */
   public Session getSession() {
      return studySession.getSession();
   }

   /**
    * Called by the question and answer panel to determine the width of
    * the main frame, so that it can size itself accordingly
    * @see CardPanel
    */
   public int getWidth() {
      return main.getWidth();
   }

   /**
    * Called when the "Right" button is clicked
    */
   public void markRight() {
      studySession.markRight();
      if (studySession.getSession().getCurrentCard() == null) {
         setSessionState(SessionState.SESSION_COMPLETE);
      }
      else {
         fireCardChange();
         setSessionState(SessionState.ASKING_QUESTION);
      }
   }

   /**
    * Called when the "Wrong" button is clicked
    */
   public void markWrong() {
      studySession.markWrong();
      fireCardChange();
      setSessionState(SessionState.ASKING_QUESTION);
   }

   /**
    * Called when the "next card" or "right" button has been clicked
    */
   public void nextCard() {
      studySession.nextCard();
      if (studySession.getSession().getCurrentCard() == null)
         displayStatistics();
      else
         fireCardChange();
   }

   /**
    * Called when the "previous card" button has been clicked
    */
   public void previousCard() {
      studySession.previousCard();
      fireCardChange();
   }

   /**
    * Removes a card change listener from the list
    */
   public void removeCardChangeListener(CardChangeListener listener) {
      cardChangeListeners.remove(listener);
   }

   /**
    * Removes a session state change listener from the list
    */
   public void removeSessionStateChangeListener(
         SessionStateChangeListener listener) {
      sessionStateChangeListeners.remove(listener);
   }

   /**
    * Called at the end of a session if the "restart" button has been
    * clicked.
    * @see SummaryButtonPanel
    */
   public void restart() {
      main.setFile(main.getFile());
   }

   /**
    * Sets the session state to either REVIEW_MODE (if the flag is true)
    * or ASKING_QUESTION (if the flag is false)
    * @param enabled a boolean flag
    * @see ReviewModeCheckbox#itemStateChanged(java.awt.event.ItemEvent)
    * @see ReviewModeCheckbox#ReviewModeCheckbox(SessionPanel)
    */
   public void setReviewMode(boolean enabled) {
      final SessionState newState = enabled
            ? SessionState.REVIEW_MODE
            : SessionState.ASKING_QUESTION;
      setSessionState(newState);
   }

   /**
    * Displays statistics
    * @see SummaryPanel
    */
   public void displayStatistics() {
      cardChangeListeners.clear();
      sessionStateChangeListeners.clear();

      final Container container = main.getFrame().getContentPane();
      container.removeAll();
      container.setVisible(false);
      container.validate();
      container.setVisible(true);

      SummaryPanel ssp = new SummaryPanel(
            this);
      container.add(ssp.getComponent(), "Center");
      container.validate();
   }
}
