package com.philhanna.flashcards.adapter.in.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * The second menu. Constructed by the {@link AppMenuBar}.
 * @see ResetSizeMenuItem
 */
public class ViewMenu extends AbstractMenu {

   /**
    * Creates a new ViewMenu and adds menu items to it
    * @param main
    */
   public ViewMenu(Main main) {
      super(main, new JMenu("View"));
      menu.add(new ResetSizeMenuItem(main).getComponent());
   }

   /**
    * Returns the completed menu to the menu bar container
    * @see AppMenuBar
    */
   public JMenu getComponent() {
      return menu;
   }
}
