package com.philhanna.flashcards.deck;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.philhanna.flashcards.ApplicationException;
import com.philhanna.flashcards.Card;
import com.philhanna.flashcards.Deck;

/**
 * An implementation of the <code>Deck</code> interface that reads a titled
 * deck of cards from an XML file. Primary attributes are:
 * <ul>
 * <li>The title of the deck</title>
 * <li>A {@link List} of the {@link Card} objects it contains</li>
 * </ul>
 */
public class DeckImpl implements Deck {

   // ==========================================================
   // Constants and class variables
   // ==========================================================

   private static final XPath xpath = XPathFactory.newInstance().newXPath();

   // ==========================================================
   // Class methods
   // ==========================================================

   /**
    * Given a DOM object representing a flashcard deck, returns the list
    * of {@link Card} objects it contains
    * @param doc a DOM object that was parsed from a flashcard deck
    */
   static List<Card> getCardsFromXML(Document doc) throws SAXException,
         ApplicationException {

      // Create the empty list, which will be loaded and returned

      final List<Card> list = new ArrayList<Card>();
      try {

         // Get the list of card nodes in the DOM, using an XPath
         // expression

         NodeList cardNodes = (NodeList) xpath.evaluate(
               "//card",
               doc,
               XPathConstants.NODESET);
         if ((cardNodes == null) || (cardNodes.getLength() == 0)) {
            throw new EmptyDeckException("Empty deck - no cards found");
         }

         // Pre-compile XPath expressions that will retrieve question
         // text and answer text

         XPathExpression exprQuestion = xpath.compile("question/text()");
         XPathExpression exprAnswer = xpath.compile("answer/text()");

         // Loop through the card nodes

         int index = 0;
         for (int n = cardNodes.getLength(); index < n; ++index) {
            Element elemCard = (Element) cardNodes.item(index);

            // Parse the question text

            String question = exprQuestion.evaluate(elemCard).trim();
            if (question.equals("")) {
               int cardNumber = index + 1;
               String template = "No question found on card {0} in deck";
               Object[] args = { Integer.valueOf(cardNumber) };

               String errmsg = MessageFormat.format(template, args);
               throw new InvalidCardException(errmsg);
            }

            // Parse the answer text

            String answer = exprAnswer.evaluate(elemCard).trim();
            if (answer.equals("")) {
               int cardNumber = index + 1;
               String template = "No answer found on card {0} in deck";
               Object[] args = { Integer.valueOf(cardNumber) };

               String errmsg = MessageFormat.format(template, args);
               throw new InvalidCardException(errmsg);
            }

            // Create a card object from the question and answer, and
            // add it to the card list

            Card card = new CardImpl(question, answer);
            list.add(card);
         }
      }
      catch (XPathExpressionException e) {
         throw new SAXException(e);
      }
      return list;
   }

   /**
    * Given a DOM object, returns the text in the &lt;title&gt; element
    * @param doc a DOM object parsed from a flashcard file
    */
   static String getTitleFromXML(Document doc) throws SAXException {
      String title = "Untitled";
      try {
         Element elemTitle = (Element) xpath.evaluate(
               "/cards/title",
               doc,
               XPathConstants.NODE);
         if (elemTitle != null)
            title = (String) xpath.evaluate(
                  "text()",
                  elemTitle,
                  XPathConstants.STRING);
      }
      catch (XPathExpressionException e) {
         throw new SAXException(e);
      }
      return title;
   }

   // ==========================================================
   // Instance variables
   // ==========================================================

   private final List<Card> cards;
   private final String title;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new XML deck from an already-parsed XML file
    * @param doc a DOM that was parsed from an XML flashcard file
    */
   public DeckImpl(Document doc) throws SAXException, ApplicationException {
      this.cards = getCardsFromXML(doc);
      this.title = getTitleFromXML(doc);
   }

   /**
    * Creates a new XMLDeck by parsing an XML flashcard file.
    * @param file
    * @throws SAXException
    * @throws ApplicationException
    */
   public DeckImpl(File file) throws SAXException, ApplicationException {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = null;
      Document doc = null;
      try {
         db = dbf.newDocumentBuilder();
         doc = db.parse(file);
      }
      catch (ParserConfigurationException e) {
         throw new SAXException(e);
      }
      catch (IOException e) {
         throw new SAXException(e);
      }
      this.cards = getCardsFromXML(doc);
      this.title = getTitleFromXML(doc);
   }

   // ==========================================================
   // Instance methods - implementation of Deck interface
   // ==========================================================

   /**
    * Returns the list of cards
    */
   public List<Card> getCards() {
      return this.cards;
   }

   /**
    * Returns the title of the deck
    */
   public String getTitle() {
      return this.title;
   }

   /**
    * Toggles each card in the deck so that it shows the answer rather
    * than the question (or vice versa)
    */
   public void toggle() {
      for (Iterator<Card> it = this.cards.iterator(); it.hasNext();) {
         CardImpl card = (CardImpl) it.next();
         card.toggle();
      }
   }
}
