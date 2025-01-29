package huffman;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * GUI Front-End for MadZip Compression Utilities.
 *
 * @author CS 240 Instructors
 * @version 4/2023
 */
public class MadZipApp {

  private JFrame frmMadzip;
  private JTextField textField;
  private JFileChooser fileChooser;
  private File currentFile;

  private JButton zipButton;
  private JButton unzipButton;
  private JTextArea textArea;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
    }

    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MadZipApp window = new MadZipApp();
          window.frmMadzip.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public MadZipApp() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frmMadzip = new JFrame();
    frmMadzip.setTitle("MadZip");
    frmMadzip.setBounds(100, 100, 450, 225);
    frmMadzip.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();
    frmMadzip.setJMenuBar(menuBar);

    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);

    JMenuItem mntmOpenFile = new JMenuItem("Open File to Zip...");
    mntmOpenFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          currentFile = openFile(null);
          if (currentFile != null) {
            zipButton.setEnabled(true);
            unzipButton.setEnabled(false);
          }
        } catch (IOException e1) {
          JOptionPane.showMessageDialog(frmMadzip, "MadZip has encountered an error!");
          e1.printStackTrace();
        }
      }
    });
    mnFile.add(mntmOpenFile);

    JMenuItem mntmOpenFileTo = new JMenuItem("Open File to Unzip...");
    mntmOpenFileTo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          FileNameExtensionFilter filter = new FileNameExtensionFilter("MadZip Files", "mz");
          currentFile = openFile(filter);
          if (currentFile != null) {
            unzipButton.setEnabled(true);
            zipButton.setEnabled(false);
          }
        } catch (IOException e1) {
          JOptionPane.showMessageDialog(frmMadzip, "MadZip has encountered an error!");
          e1.printStackTrace();
        }
      }
    });
    mnFile.add(mntmOpenFileTo);

    JMenu mnHelp = new JMenu("Help");
    menuBar.add(mnHelp);

    JMenuItem mntmHelp = new JMenuItem("Help...");
    mntmHelp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          openWebpage(new URI("https://youtu.be/2Q_ZzBGPdqE"));
        } catch (URISyntaxException e1) {
          e1.printStackTrace();
        }

      }
    });
    mnHelp.add(mntmHelp);
    frmMadzip.getContentPane()
        .setLayout(new BoxLayout(frmMadzip.getContentPane(), BoxLayout.Y_AXIS));

    JPanel panel = new JPanel();
    frmMadzip.getContentPane().add(panel);

    textField = new JTextField();
    textField.setEditable(false);
    panel.add(textField);
    textField.setColumns(30);

    JPanel buttonPanel = new JPanel();
    frmMadzip.getContentPane().add(buttonPanel);

    zipButton = new JButton(" Zip ");
    zipButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        try {
          zipFile();
        } catch (IOException e1) {
          JOptionPane.showMessageDialog(frmMadzip, "MadZip has encountered an error!");
          e1.printStackTrace();
        }

      }
    });
    zipButton.setEnabled(false);
    buttonPanel.add(zipButton);

    unzipButton = new JButton("Unzip");
    unzipButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {


        try {
          unzipFile();
        } catch (ClassNotFoundException | IOException e1) {
          JOptionPane.showMessageDialog(frmMadzip, "MadZip has encountered an error!");
          e1.printStackTrace();
        }


      }
    });
    unzipButton.setEnabled(false);
    buttonPanel.add(unzipButton);

    JPanel outputPanel = new JPanel();
    frmMadzip.getContentPane().add(outputPanel);

    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setColumns(30);
    textArea.setTabSize(4);
    textArea.setRows(4);
    outputPanel.add(textArea);

    fileChooser = new JFileChooser(new File("."));
  }

  // https://stackoverflow.com/a/10967469
  public static boolean openWebpage(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(uri);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static boolean openWebpage(URL url) {
    try {
      return openWebpage(url.toURI());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return false;
  }


  /**
   * Gets the filename extension.
   *
   * @param file the file itself
   * @return substring after the last dot
   */
  private String getExtension(File file) {
    int dotPos = file.getName().lastIndexOf('.');
    return file.getName().substring(dotPos + 1);
  }

  /**
   * Remove the final filename extension.
   *
   * @param file the file itself
   * @return The file with the extension removed.
   */
  private File removeExtension(File file) {
    int dotPos = file.getAbsolutePath().lastIndexOf('.');
    return new File(file.getAbsolutePath().substring(0, dotPos));
  }


  /**
   * Pop up a file dialog, and open the requested file.
   *
   * @throws IOException If there is a problem opening the file.
   */
  private File openFile(FileNameExtensionFilter filter) throws IOException {

    fileChooser.setFileFilter(filter);

    int returnVal = fileChooser.showOpenDialog(frmMadzip);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      currentFile = fileChooser.getSelectedFile();
      textField.setText(currentFile.getCanonicalPath());
      return fileChooser.getSelectedFile();

    }
    return null;
  }

  /**
   * Pop up a file dialog, and zip the requested file.
   *
   * @throws IOException If there is a problem zipping the file.
   */
  private void zipFile() throws IOException {

    JFileChooser fileChooser = new JFileChooser(new File("."));
    fileChooser.setSelectedFile(new File(currentFile.getAbsolutePath() + ".mz"));
    int returnVal = fileChooser.showSaveDialog(frmMadzip);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      File selected = fileChooser.getSelectedFile();
      if (selected.exists()) {
        int response = JOptionPane.showConfirmDialog(frmMadzip,
            selected.getName() + " exists. Are you sure you want to overwrite it?", "Confirm",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response != JOptionPane.YES_OPTION) {
          return;
        }
      }

      MadZip.zip(currentFile, selected);

      textArea.setText(null);
      long inSize = currentFile.length();
      long outSize = selected.length();
      double percent = 100.0 - ((float) outSize) / inSize * 100;
      String out = String.format(
          "%s created successfully!\nOriginal size: %d bytes\nZipped size: %d bytes\nSpace saving: %.2f%%",
          selected.getName(), inSize, outSize, percent);
      textArea.setText(out);
    }
  }

  /**
   * Pop up a file dialog, and unzip the requested file.
   *
   * @throws ClassNotFoundException If it is a badly structured .mz file.
   * @throws IOException If there is a problem unzipping the file.
   */
  private void unzipFile() throws ClassNotFoundException, IOException {

    JFileChooser fileChooser = new JFileChooser(new File("."));
    if (getExtension(currentFile).equals("mz")) {
      System.out.println(removeExtension(currentFile));
      fileChooser.setSelectedFile(removeExtension(currentFile));
    }
    int returnVal = fileChooser.showSaveDialog(frmMadzip);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File selected = fileChooser.getSelectedFile();
      if (selected.exists()) {
        int response = JOptionPane.showConfirmDialog(frmMadzip,
            selected.getName() + " exists. Are you sure you want to overwrite it?", "Confirm",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response != JOptionPane.YES_OPTION) {
          return;
        }
      }

      MadZip.unzip(currentFile, selected);

      textArea.setText(null);
      String out = String.format("%s created successfully!", selected.getName());
      textArea.setText(out);

    }
  }


}
