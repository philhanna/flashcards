package com.philhanna.flashcards.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.ui.Main;

/**
 * The second menu. Constructed by the {@link MenuBarContainer}.
 * @see ToggleMenuItemContainer
 * @see ResetMenuItemContainer
 */
public class ViewMenuContainer extends MenuContainer {

   /**
    * Creates a new ViewMenuContainer and adds menu items to it
    * @param main
    */
   public ViewMenuContainer(Main main) {
      super(main, new JMenu("View"));
      menu.add(new ToggleMenuItemContainer(main).getComponent());
      menu.add(new ResetMenuItemContainer(main).getComponent());
   }

   /**
    * Returns the completed menu to the menu bar container
    * @see MenuBarContainer
    */
   public JMenu getComponent() {
      return menu;
   }
}
