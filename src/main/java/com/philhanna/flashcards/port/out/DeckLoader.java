package com.philhanna.flashcards.port.out;

import java.io.File;

import org.xml.sax.SAXException;

import com.philhanna.flashcards.domain.ApplicationException;
import com.philhanna.flashcards.domain.Deck;

/**
 * Outbound port for loading a deck from a file.
 * The domain defines this interface; adapters (e.g. XML) implement it.
 */
public interface DeckLoader {

   /**
    * Loads a deck from the specified file
    * @param file the file to load
    * @return the loaded deck
    * @throws SAXException if the file cannot be parsed
    * @throws ApplicationException if the deck is invalid
    */
   Deck load(File file) throws SAXException, ApplicationException;
}
