package com.philhanna.flashcards;

import java.io.File;

import org.junit.BeforeClass;

/**
 * Abstract base class for all unit tests
 */
public abstract class BaseTest {

   private static boolean firstTime = true;
   protected static File testdata;
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      if (!firstTime)
         return;
      firstTime = false;
      final File projectRoot = new File(".").getCanonicalFile();
      testdata = new File(projectRoot, "/testdata");
   }
}
