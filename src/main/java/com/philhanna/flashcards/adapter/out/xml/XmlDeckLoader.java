package com.philhanna.flashcards.adapter.out.xml;

import java.io.File;

import org.xml.sax.SAXException;

import com.philhanna.flashcards.domain.ApplicationException;
import com.philhanna.flashcards.domain.Deck;
import com.philhanna.flashcards.port.out.DeckLoader;

/**
 * Adapter that implements {@link DeckLoader} by parsing an XML file.
 */
public class XmlDeckLoader implements DeckLoader {

   @Override
   public Deck load(File file) throws SAXException, ApplicationException {
      return new XmlDeck(file);
   }
}
