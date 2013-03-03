package com.philhanna.flashcards.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

/**
 * Handles the prompt for a new card deck and the invocation of a new
 * session with that deck
 */
public class OpenDialogHandler {
   private Main main;

   /**
    * Creates a new <code>OpenDialogHandler</code>
    * @param main
    */
   public OpenDialogHandler(Main main) {
      this.main = main;
   }

   /**
    * Prompts for the card deck to be opened and starts a new session
    * with the one selected.
    */
   public void run() {

      // Get the path to the directory containing the last deck used

      File cwd = this.main.getLastDirectory();

      // Create a file chooser initialized to this directory, and look
      // for .flc or .xml files

      JFileChooser fc = new JFileChooser(cwd);
      FileFilter filter = new FileFilter() {

         @Override
         public boolean accept(File file) {
            return file.isDirectory()
                  || file.getPath().endsWith(".flc")
                  || file.getPath().endsWith(".xml");
         }

         @Override
         public String getDescription() {
            return "Flashcards (*.flc, *.xml)";
         }
      };
      fc.setFileFilter(filter);

      // Display the dialog and check the return code. If a file was
      // selected,
      // start a new sessions with that as the file.

      int rc = fc.showOpenDialog(this.main.getFrame());
      switch (rc) {
         case 0:
            File file = fc.getSelectedFile();
            this.main.setFile(file, false);
      }
   }
}
