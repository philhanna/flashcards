package com.philhanna.flashcards.adapter.in.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

import javax.swing.*;

import com.philhanna.flashcards.domain.*;
import com.philhanna.flashcards.port.out.DeckLoader;
import com.philhanna.flashcards.adapter.out.sqlite.SqliteDeckLoader;
import com.philhanna.flashcards.adapter.in.ui.menus.AppMenuBar;

/**
 * Entry point for flashcard program. This program creates the main application
 * frame and populates its menu bar and listeners. When a file is chosen from
 * the <code>File-&gt;Open</code> menu option , the program creates and starts a
 * <code>SessionPanel</code> for the file.
 *
 * @see OpenDialogHandler
 * @see SessionPanel
 */
public class Main {

   // ==========================================================
   // Class variables and constants
   // ==========================================================

   public static final String VERSION = "2.1.1";

   // ==========================================================
   // Class methods
   // ==========================================================

   /**
    * Entry point, which creates a <code>Main</code> object and displays its
    * frame. If a file name is specified on the command line, opens that file
    * first.
    */
   public static final void main(String[] args) {
      Main main = new Main();
      if (args.length > 0) {
         String fileName = args[0];
         main.setFile(new File(fileName));
      }
      main.getFrame().setVisible(true);
   }

   // ==========================================================
   // Instance variables
   // ==========================================================

   private final DeckLoader deckLoader = new SqliteDeckLoader();
   private JFrame frame;
   private File file;
   private SessionPanel sc;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new Main program
    */
   public Main() {
      try {
         UIManager.setLookAndFeel(Configuration.LOOK_AND_FEEL);
      }
      catch (Exception ignored) {}
      frame = new JFrame();
      frame.setTitle("Flashcards");
      frame.setBounds(getBounds());
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent event) {
            doExit();
         }
      });
      frame.setJMenuBar(new AppMenuBar(this).getComponent());
      URL iconURL = super.getClass().getResource(
            Configuration.CARD_ICON_FILE_NAME);
      frame.setIconImage(new ImageIcon(iconURL).getImage());
   }

   // ==========================================================
   // Internal methods
   // ==========================================================

   private Rectangle getBounds() {
      int x      = Integer.parseInt(Configuration.config.getProperty("x",      String.valueOf(Configuration.X)));
      int y      = Integer.parseInt(Configuration.config.getProperty("y",      String.valueOf(Configuration.Y)));
      int width  = Integer.parseInt(Configuration.config.getProperty("width",  String.valueOf(Configuration.WIDTH)));
      int height = Integer.parseInt(Configuration.config.getProperty("height", String.valueOf(Configuration.HEIGHT)));
      return new Rectangle(x, y, width, height);
   }

   private void saveWindowBounds() {
      Configuration.save("x",      String.valueOf(frame.getX()));
      Configuration.save("y",      String.valueOf(frame.getY()));
      Configuration.save("width",  String.valueOf(frame.getWidth()));
      Configuration.save("height", String.valueOf(frame.getHeight()));
   }

   private void startSession(File file) {
      try {
         sc = new SessionPanel(this, deckLoader, file);
         JPanel panel = this.sc.getComponent();
         frame.getContentPane().add(panel, "Center");
         frame.validate();
      }
      catch (ApplicationException e) {
         displayErrorMessage(e.getMessage());
      }
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Displays the specified error message in a message box
    */
   public void displayErrorMessage(String message) {
      JOptionPane.showMessageDialog(this.frame, message, "Error",
            JOptionPane.ERROR_MESSAGE);
   }

   /**
    * Handles the <code>Exit</code> menu option, which saves the current window
    * bounds to config.properties and then disposes of the frame.
    */
   public void doExit() {
      if (this.frame != null) {
         saveWindowBounds();
         this.frame.dispose();
      }
   }

   /**
    * Handles the <code>Open</code> menu option.
    */
   public void doOpen() {
      OpenDialogHandler od = new OpenDialogHandler(this);
      od.run();
   }

   /**
    * Handles the <code>Restart</code> menu option.
    */
   public void doRestart() {
      if (sc != null) {
         sc.restart();
      }
   }

   /**
    * Invokes an editor on the current file
    */
   public void doEdit() {
      String[] cmd = {
            Configuration.TEXT_EDITOR, file.toString()
      };
      try {
         Runtime.getRuntime().exec(cmd);
      }
      catch (IOException e) {
         displayErrorMessage(e.getMessage());
      }
   }

   /**
    * Resets the window to the default bounds from config.properties.
    */
   public void doResetSize() {
      Configuration.save("x",      String.valueOf(Configuration.X));
      Configuration.save("y",      String.valueOf(Configuration.Y));
      Configuration.save("width",  String.valueOf(Configuration.WIDTH));
      Configuration.save("height", String.valueOf(Configuration.HEIGHT));
      frame.setBounds(getBounds());
   }

   /**
    * Displays the help about box
    */
   public void doHelpAbout() {
      final String title = "About Flashcards";
      final Component message = createHelpAboutMessage();
      final URL iconURL = getClass().getResource(
            Configuration.CARD_ICON_FILE_NAME);
      final Icon icon = new ImageIcon(iconURL);
      final int type = JOptionPane.INFORMATION_MESSAGE;
      JOptionPane.showMessageDialog(this.frame, message, title, type, icon);
   }

   private Component createHelpAboutMessage() {

      Font baseFont = frame.getFont();

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(3, 1, 0, 10));

      JPanel pnl1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel lbl1 = new JLabel("Flashcards " + VERSION);
      lbl1.setFont(new Font(baseFont.getName(), baseFont.getStyle(),
            (int) (baseFont.getSize() * 1.5)));
      pnl1.add(lbl1);
      panel.add(pnl1);

      JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel lbl2 = new JLabel("A computer-based flash cards program");
      pnl2.add(lbl2);
      panel.add(pnl2);

      JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel lbl3 = new JLabel("Phil Hanna - ph1204@gmail.com");
      lbl3.setFont(new Font(baseFont.getName(), baseFont.getStyle(),
            (int) (baseFont.getSize() * 0.85)));
      pnl3.add(lbl3);
      panel.add(pnl3);

      return panel;
   }

   /**
    * Returns the current file
    */
   public File getFile() {
      return this.file;
   }

   /**
    * Returns the {@link javax.swing.JFrame}
    */
   public JFrame getFrame() {
      return this.frame;
   }

   /**
    * Returns the frame height
    */
   public int getHeight() {
      return frame.getHeight();
   }

   /**
    * Returns the path to the directory containing the last deck that was used
    */
   public File getLastDirectory() {
      try {
         String path = Configuration.config.getProperty("directory", ".");
         return new File(path).getCanonicalFile();
      }
      catch (IOException ignored) {
         return new File(".");
      }
   }

   /**
    * Returns the frame width
    */
   public int getWidth() {
      return frame.getWidth();
   }

   /**
    * Starts a new session with the specified file
    */
   public void setFile(File file) {
      this.file = file;
      Container content = this.frame.getContentPane();
      content.removeAll();
      content.setVisible(false);
      content.validate();
      content.setVisible(true);
      if (file == null) {
         setTitle(null);
         this.sc = null;
      }
      else {
         setLastDirectory(file);
         startSession(file);
      }
   }

   /**
    * Saves the directory of the given file to config.properties
    */
   public void setLastDirectory(final File file) {
      try {
         File canonical = file.getCanonicalFile();
         File dir = canonical.isDirectory() ? canonical : canonical.getParentFile();
         Configuration.save("directory", dir.toString());
      }
      catch (IOException ignored) {}
   }

   /**
    * Sets the screen title to the specified string
    */
   public void setTitle(final String title) {
      this.frame.setTitle(title == null ? "Flashcards" : title);
   }
}
