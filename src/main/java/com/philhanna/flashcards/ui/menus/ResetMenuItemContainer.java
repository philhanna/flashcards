package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the view menu that causes the default window size and position
 * to be restored
 */
public class ResetMenuItemContainer extends MenuItemContainer {

   /**
    * Creates a new ResetMenuItemContainer
    */
   public ResetMenuItemContainer(Main main) {
      super(main, MenuItems.RESET.getMenuItem());
   }

   /**
    * Calls the doResetSize() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doResetSize()
    */
   public void actionPerformed(ActionEvent e) {
      main.doResetSize();
   }
}
