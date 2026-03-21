package com.philhanna.flashcards.adapter.in.ui.menus;

import javax.swing.*;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * A container for the JMenuBar used by the user interface
 */
public class AppMenuBar {
   private JMenuBar mb;

   /**
    * Creates a new AppMenuBar
    * @param main
    */
   public AppMenuBar(Main main) {
      mb = new JMenuBar();
      mb.add(new FileMenu(main).getComponent());
      mb.add(new ViewMenu(main).getComponent());
      //mb.add(Box.createHorizontalGlue());
      mb.add(new HelpMenu(main).getComponent());
   }

   /**
    * Returns the completed menu bar to the main program.
    * @see com.philhanna.flashcards.adapter.in.ui.Main
    */
   public JMenuBar getComponent() {
      return this.mb;
   }
}
