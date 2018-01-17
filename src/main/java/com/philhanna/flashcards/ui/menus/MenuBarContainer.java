package com.philhanna.flashcards.ui.menus;

import javax.swing.*;

import com.philhanna.flashcards.ui.Main;

/**
 * A container for the JMenuBar used by the user interface
 */
public class MenuBarContainer {
   private JMenuBar mb;

   /**
    * Creates a new MenuBarContainer
    * @param main
    */
   public MenuBarContainer(Main main) {
      mb = new JMenuBar();
      mb.add(new FileMenuContainer(main).getComponent());
      mb.add(new ViewMenuContainer(main).getComponent());
      //mb.add(Box.createHorizontalGlue());
      mb.add(new HelpMenuContainer(main).getComponent());
   }

   /**
    * Returns the completed menu bar to the main program.
    * @see com.philhanna.flashcards.ui.Main
    */
   public JMenuBar getComponent() {
      return this.mb;
   }
}
