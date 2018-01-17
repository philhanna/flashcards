package com.philhanna.flashcards.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.ui.Main;

/**
 * The third menu. Constructed by the {@link MenuBarContainer}.
 * @see AboutMenuItemContainer
 */
public class HelpMenuContainer extends MenuContainer {

   /**
    * Creates a new HelpMenuContainer and adds menu items to it
    * @param main
    */
   public HelpMenuContainer(Main main) {
      super(main, new JMenu("Help"));
      menu.add(new AboutMenuItemContainer(main).getComponent());
   }

   /**
    * Returns the completed menu to the menu bar container
    * @see MenuBarContainer
    */
   public JMenu getComponent() {
      return menu;
   }
}
