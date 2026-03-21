package com.philhanna.flashcards.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A panel containing status information, including the card numbers,
 * statistics, and the review mode checkbox.
 * <p>
 * <img src="doc-files/StatusBar-1.png"/>
 * </p>
 * @see CardProgressPanel
 * @see CardStatsPanel
 * @see ReviewModeCheckbox
 * @see SessionPanel
 */
public class StatusBar {
   private JPanel panel;

   /**
    * Creates a new StatusBar
    * @param sc the session container
    */
   public StatusBar(SessionPanel sc) {
      this.panel = new JPanel();

      this.panel.setLayout(new BorderLayout(10, 5));
      this.panel.setBorder(BorderFactory.createLoweredBevelBorder());

      CardProgressPanel cnc = new CardProgressPanel(sc);
      this.panel.add(cnc.getComponent(), "West");

      CardStatsPanel csc = new CardStatsPanel(sc);
      this.panel.add(csc.getComponent(), "Center");

      ReviewModeCheckbox rmcc = new ReviewModeCheckbox(sc);
      this.panel.add(rmcc.getComponent(), "East");
   }

   /**
    * Returns the contained panel to the parent session container so
    * that it can be included in the main frame.
    */
   public Component getComponent() {
      return this.panel;
   }
}
