package com.philhanna.flashcards.adapter.out.sqlite;

import java.io.File;

import com.philhanna.flashcards.domain.ApplicationException;
import com.philhanna.flashcards.domain.Deck;
import com.philhanna.flashcards.port.out.DeckLoader;

/**
 * Adapter that implements {@link DeckLoader} by reading a SQLite database file.
 */
public class SqliteDeckLoader implements DeckLoader {

   @Override
   public Deck load(File file) throws ApplicationException {
      return new SqliteDeck(file);
   }
}
