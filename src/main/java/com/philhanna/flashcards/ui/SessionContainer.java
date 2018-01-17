package com.philhanna.flashcards.ui;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.xml.sax.SAXException;

import com.philhanna.flashcards.*;
import com.philhanna.flashcards.deck.*;
import com.philhanna.flashcards.session.*;
import com.philhanna.flashcards.ui.events.CardChangeEvent;
import com.philhanna.flashcards.ui.events.CardChangeListener;
import com.philhanna.flashcards.ui.events.SessionStateChangeEvent;
import com.philhanna.flashcards.ui.events.SessionStateChangeListener;
import com.philhanna.flashcards.ui.menus.MenuItems;

/**
 * An object that creates and manages the GUI components and statistics
 * for a pass through a card deck.
 * <p>
 * Starts by parsing the card deck file using an {@link DeckImpl} and
 * creating a {@link Session} for it. A session provides methods for
 * moving forward and backward through the deck and maintains statistics
 * about right and wrong answers.
 * <p>
 * <img src="doc-files/SessionContainer-1.png"/>
 * <p>
 * After creating the session, this object then creates the GUI
 * components to represent the session in the frame:
 * <ul>
 * <li>A question and answer panel ({@link QandAPanelContainer}) at the
 * center. This has an upper panel that shows the text of the question,
 * and a lower panel that shows the answer. This lower panel is not
 * shown until the user clicks the <code>Flip</code> button</li>
 * <li>A {@link ButtonPanelContainer} and {@link StatusPanelContainer}
 * at the bottom. The button panel contains two buttons, which change
 * labels according to the state of the session.</li>
 * </ul>
 * At the end of the deck, the session statistics panel is displayed
 * @see DeckImpl
 * @see Session
 * @see SessionImpl
 * @see ButtonPanelContainer
 * @see StatusPanelContainer
 * @see SessionStateChangeListener
 * @see SessionStateChangeEvent
 * @see CardChangeListener
 * @see CardChangeEvent
 * @see QandAPanelContainer
 * @see StatisticsPanelContainer
 */
public class SessionContainer {

   // ==========================================================
   // Class variables and constants
   // ==========================================================

   private Main main;
   private JPanel panel;
   private Session session;
   private SessionState sessionState = SessionState.ASKING_QUESTION;
   private ButtonPanelContainer bpc;
   private StatusPanelContainer spc;
   private List<SessionStateChangeListener> sessionStateChangeListeners = new ArrayList<SessionStateChangeListener>();
   private List<CardChangeListener> cardChangeListeners = new ArrayList<CardChangeListener>();
   private QandAPanelContainer qap;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new session container for the specified file and
    * switches its viewing mode from showing questions to showing
    * answers.
    * @param main the main program
    * @param file the file to be opened
    * @throws SAXException
    * @throws ApplicationException
    */
   public SessionContainer(Main main, File file, boolean toggle)
         throws SAXException, ApplicationException {

      // Save the reference to the main program

      this.main = main;
      
      // Turn on the "restart" and "edit" menu items
      
      MenuItems.EDIT.getMenuItem().setEnabled(true);
      MenuItems.RESTART.getMenuItem().setEnabled(true);

      // Create a new deck object by parsing the specified XML file

      Deck deck = new DeckImpl(file);
      if (toggle) {
         deck.toggle();
      }

      // Set this deck's name in the frame title

      updateFrameTitle(deck);

      // Create a new session using this deck

      this.session = new SessionImpl(deck);
      addCardChangeListener((CardChangeListener) session);

      // Create a new panel with a border layout

      this.panel = new JPanel();
      this.panel.setLayout(new BorderLayout());

      // Add a Q&A panel in the center

      this.qap = new QandAPanelContainer(this);
      this.panel.add(this.qap.getComponent(), "Center");

      // Add a lower panel at the bottom

      JPanel lowerPanel = new JPanel();
      this.panel.add(lowerPanel, "South");
      lowerPanel.setLayout(new BoxLayout(lowerPanel, 1));

      // Add a button panel to the lower panel

      this.bpc = new ButtonPanelContainer(this);
      lowerPanel.add(this.bpc.getComponent());

      // Add a status panel to the lower panel

      this.spc = new StatusPanelContainer(this);
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
      SessionCard card = session.getCurrentCard();
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
    *        {@link CardNumbersContainer},
    *        {@link CardStatisticsContainer}, and
    *        {@link QandAPanelContainer}.
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
    *        {@link ButtonPanelContainer} {@link CardNumbersContainer}
    *        {@link CardStatisticsContainer} {@link QandAPanelContainer}
    */
   public void addSessionStateChangeListener(SessionStateChangeListener listener) {
      sessionStateChangeListeners.add(listener);
   }

   /**
    * Closes the file. Called when the <code>close</code> button is
    * clicked.
    * @see StatisticsButtonContainer
    */
   public void close() {
      main.setFile(null, false);
   }

   /**
    * When the user clicks the flip button, this method fires a session
    * state change event indicating that the session state has changed
    * to <code>SHOWING_ANSWER</code>.
    * @see ButtonPanelContainer
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
    * @see QandAPanelContainer#QandAPanelContainer(SessionContainer)
    */
   public JFrame getFrame() {
      return main.getFrame();
   }

   /**
    * Called in a number of places by lower-level components to get the
    * contained session.
    * @see CardNumbersContainer
    * @see QandAPanelContainer
    * @see StatisticsTable
    */
   public Session getSession() {
      return session;
   }

   /**
    * Called by the question and answer panel to determine the width of
    * the main frame, so that it can size itself accordingly
    * @see QandAPanelContainer
    */
   public int getWidth() {
      return main.getWidth();
   }

   /**
    * Called when the "Right" button is clicked
    */
   public void markRight() {
      SessionCard card = session.getCurrentCard();
      if (card != null)
         card.markRight();
      nextCard();
      if (session.getCurrentCard() == null) {
         setSessionState(SessionState.SESSION_COMPLETE);
      }
      else {
         setSessionState(SessionState.ASKING_QUESTION);
      }
   }

   /**
    * Called when the "Wrong" button is clicked
    */
   public void markWrong() {
      SessionCard card = session.getCurrentCard();
      if (card != null) {
         card.markWrong();
         session.rotate();
         session.shuffle();
         fireCardChange();
      }
      setSessionState(SessionState.ASKING_QUESTION);
   }

   /**
    * Called when the "next card" or "right" button has been clicked
    */
   public void nextCard() {
      session.nextCard();
      if (session.getCurrentCard() == null)
         displayStatistics();
      else
         fireCardChange();
   }

   /**
    * Called when the "previous card" button has been clicked
    */
   public void previousCard() {
      session.previousCard();
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
    * @see StatisticsButtonContainer
    */
   public void restart() {
      final File file = main.getFile();
      main.setFile(file, false);
   }

   /**
    * Sets the session state to either REVIEW_MODE (if the flag is true)
    * or ASKING_QUESTION (if the flag is false)
    * @param enabled a boolean flag
    * @see ReviewModeCheckboxContainer#itemStateChanged(java.awt.event.ItemEvent)
    * @see ReviewModeCheckboxContainer#ReviewModeCheckboxContainer(SessionContainer)
    */
   public void setReviewMode(boolean enabled) {
      final SessionState newState = enabled
            ? SessionState.REVIEW_MODE
            : SessionState.ASKING_QUESTION;
      setSessionState(newState);
   }

   /**
    * Displays statistics
    * @see StatisticsPanelContainer
    */
   public void displayStatistics() {
      cardChangeListeners.clear();
      sessionStateChangeListeners.clear();

      final Container container = main.getFrame().getContentPane();
      container.removeAll();
      container.setVisible(false);
      container.validate();
      container.setVisible(true);

      StatisticsPanelContainer ssp = new StatisticsPanelContainer(
            this);
      container.add(ssp.getComponent(), "Center");
      container.validate();
   }
}
