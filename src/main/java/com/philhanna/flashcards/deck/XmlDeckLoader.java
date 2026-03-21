package com.philhanna.flashcards.deck;

import java.io.File;

import org.xml.sax.SAXException;

import com.philhanna.flashcards.ApplicationException;
import com.philhanna.flashcards.Deck;
import com.philhanna.flashcards.DeckLoader;

/**
 * Adapter that implements {@link DeckLoader} by parsing an XML file.
 */
public class XmlDeckLoader implements DeckLoader {

   @Override
   public Deck load(File file) throws SAXException, ApplicationException {
      return new DeckImpl(file);
   }
}
