package com.philhanna.flashcards.adapter.in.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * The third menu. Constructed by the {@link AppMenuBar}.
 * @see AboutMenuItem
 */
public class HelpMenu extends AbstractMenu {

   /**
    * Creates a new HelpMenu and adds menu items to it
    * @param main
    */
   public HelpMenu(Main main) {
      super(main, new JMenu("Help"));
      menu.add(new AboutMenuItem(main).getComponent());
   }

   /**
    * Returns the completed menu to the menu bar container
    * @see AppMenuBar
    */
   public JMenu getComponent() {
      return menu;
   }
}
