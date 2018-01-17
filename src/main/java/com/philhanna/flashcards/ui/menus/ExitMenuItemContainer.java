package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the file menu that causes application to exit
 */
public class ExitMenuItemContainer extends MenuItemContainer {

   /**
    * Creates a new ExitMenuItemContainer
    */
   public ExitMenuItemContainer(Main main) {
      super(main, MenuItems.EXIT.getMenuItem());
   }

   /**
    * Calls the doExit() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doExit()
    */
   public void actionPerformed(ActionEvent e) {
      main.doExit();
   }
}
