package com.philhanna.flashcards.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.philhanna.flashcards.ui.menus.MenuItems;

/**
 * A container for a panel that has restart and close buttons
 */
public class StatisticsButtonContainer {
   
   private SessionContainer sc;
   private JPanel panel;
   private JButton btnRestart;
   private JButton btnClose;

   /**
    * Creates a new StatisticsButtonContainer
    * @param sc the session container
    */
   public StatisticsButtonContainer(SessionContainer sc) {
      this.sc = sc;
      this.panel = new JPanel();
      this.panel.add(createInnerPanel());
   }

   private Component createInnerPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1, 2, 10, 10));

      this.btnRestart = new JButton("Restart");
      this.btnRestart.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            StatisticsButtonContainer.this.sc.restart();
         }
      });
      panel.add(this.btnRestart);

      this.btnClose = new JButton("Close");
      this.btnClose.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            StatisticsButtonContainer.this.sc.close();
            MenuItems.EDIT.getMenuItem().setEnabled(false);
            MenuItems.RESTART.getMenuItem().setEnabled(false);
         }
      });
      panel.add(this.btnClose);

      return panel;
   }

   /**
    * Returns the button panel to the statistics panel container
    * @see StatisticsPanelContainer
    */
   public Component getComponent() {
      return this.panel;
   }
}
