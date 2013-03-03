package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the file menu that brings up the current file in a text editor
 */
public class EditMenuItemContainer extends MenuItemContainer {

   /**
    * Creates a new RestartMenuItemContainer
    */
   public EditMenuItemContainer(Main main) {
      super(main, MenuItems.EDIT.getMenuItem());
      this.menuItem.setEnabled(false);
   }

   /**
    * Calls the doEdit() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doEdit()
    */
   public void actionPerformed(ActionEvent e) {
      main.doEdit();
   }
}
