package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * MadZip class.
 *
 * @author David Friend
 */
public class MadZip {
  
  /**
   * Zips a given file.
   *
   * @param inFile the given file to zip
   * @param saveFile file to save the zip
   */
  public static void zip(File inFile, File saveFile) throws IOException {
    HuffTree tree = new HuffTree(inFile);
    BitSequence encoding = tree.encode(inFile);
    HuffmanSave hs = new HuffmanSave(encoding, tree.getFrequencies());
    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile));
    output.writeObject(hs);
    output.close();
  }
  
  /**
   * Unzips a given file.
   *
   * @param inFile the given file to unzip
   * @param saveFile file to save the unzip
   */
  public static void unzip(File inFile, File saveFile) throws IOException, ClassNotFoundException {
    ObjectInputStream input = new ObjectInputStream(new FileInputStream(inFile));
    HuffmanSave hs = (HuffmanSave) input.readObject();
    HuffTree tree = new HuffTree(hs.getFrequencies());
    tree.decode(hs.getEncoding(), saveFile);
    input.close();
  }
}
