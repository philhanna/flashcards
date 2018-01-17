package com.philhanna.flashcards.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A panel containing status information, including the card numbers,
 * statistics, and the review mode checkbox.
 * <p>
 * <img src="doc-files/StatusPanelContainer-1.png"/>
 * </p>
 * @see CardNumbersContainer
 * @see CardStatisticsContainer
 * @see ReviewModeCheckboxContainer
 * @see SessionContainer
 */
public class StatusPanelContainer {
   private JPanel panel;

   /**
    * Creates a new StatusPanelContainer
    * @param sc the session container
    */
   public StatusPanelContainer(SessionContainer sc) {
      this.panel = new JPanel();

      this.panel.setLayout(new BorderLayout(10, 5));
      this.panel.setBorder(BorderFactory.createLoweredBevelBorder());

      CardNumbersContainer cnc = new CardNumbersContainer(sc);
      this.panel.add(cnc.getComponent(), "West");

      CardStatisticsContainer csc = new CardStatisticsContainer(sc);
      this.panel.add(csc.getComponent(), "Center");

      ReviewModeCheckboxContainer rmcc = new ReviewModeCheckboxContainer(sc);
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
