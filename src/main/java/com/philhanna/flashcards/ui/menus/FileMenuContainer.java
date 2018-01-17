package com.philhanna.flashcards.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.ui.Main;

/**
 * The first menu. Constructed by the {@link MenuBarContainer}.
 * @see OpenMenuItemContainer
 * @see RestartMenuItemContainer
 * @see ExitMenuItemContainer
 */
public class FileMenuContainer extends MenuContainer {

   /**
    * Creates a new FileMenuContainer and adds menu items to it
    * @param main
    */
   public FileMenuContainer(Main main) {
      super(main, new JMenu("File"));
      menu.add(new OpenMenuItemContainer(main).getComponent());
      menu.add(new EditMenuItemContainer(main).getComponent());
      menu.add(new RestartMenuItemContainer(main).getComponent());
      menu.addSeparator();
      menu.add(new ExitMenuItemContainer(main).getComponent());
   }

   /**
    * Returns the completed menu to the menu bar container
    * @see MenuBarContainer
    */
   public JMenu getComponent() {
      return menu;
   }
}
