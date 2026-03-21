package com.philhanna.flashcards.adapter.in.ui;

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
      File cwd = this.main.getLastDirectory();

      JFileChooser fc = new JFileChooser(cwd);
      fc.setFileFilter(new FileFilter() {
         @Override
         public boolean accept(File file) {
            return file.isDirectory() || file.getPath().endsWith(".db");
         }

         @Override
         public String getDescription() {
            return "Flashcards (*.db)";
         }
      });

      int rc = fc.showOpenDialog(this.main.getFrame());
      if (rc == JFileChooser.APPROVE_OPTION) {
         this.main.setFile(fc.getSelectedFile());
      }
   }
}
