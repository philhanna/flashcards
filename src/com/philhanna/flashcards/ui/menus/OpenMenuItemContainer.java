package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the file menu that causes the open file dialog to be
 * displayed and a new session started
 */
public class OpenMenuItemContainer extends MenuItemContainer {

   /**
    * Creates a new OpenMenuItemContainer
    */
   public OpenMenuItemContainer(Main main) {
     super(main, MenuItems.OPEN.getMenuItem());
   }

   /**
    * Calls the doOpen() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doOpen()
    */
   public void actionPerformed(ActionEvent e) {
      main.doOpen();
   }
}
