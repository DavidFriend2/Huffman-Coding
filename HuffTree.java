package huffman;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * HuffTree class.
 *
 * @author David Friend
 */
public class HuffTree {

  private HuffBaseNode root;
  private HashMap<Byte, Integer> frequencies;
  private HashMap<Byte, BitSequence> codes; // Maps from bytes to their Huffman codes.
  
  /**
   * Initializes the Huffman tree.
   *
   * @param inFile the given file to initialize frequencies
   */
  public HuffTree(File inFile) throws IOException {
    FileInputStream bi = new FileInputStream(inFile);
    int byt = bi.read();
    frequencies = new HashMap<>();
    while (byt != -1) {
      byte b = (byte) byt;
      System.out.print(b + " ");
      if (frequencies.containsKey(b)) {
        frequencies.put(b, frequencies.get(b) + 1);
      } else {
        frequencies.put(b, 1);
      }
      byt = bi.read();
    }
    bi.close();
    root = buildTree(frequencies);
    codes = new HashMap<>();
    makeCodes(root, new BitSequence());
  }

  /**
   * Initializes the Huffman tree.
   *
   * @param frequencies intitializes frequencies
   */
  public HuffTree(HashMap<Byte, Integer> frequencies) {
    this.frequencies = frequencies;
    root = buildTree(this.frequencies);
    codes = new HashMap<>();
    makeCodes(root, new BitSequence());
  }

  public HashMap<Byte, Integer> getFrequencies() {
    return new HashMap<>(frequencies);
  }

  /**
   * Encodes a file.
   *
   * @param inFile the given file to encode
   */
  public BitSequence encode(File inFile) throws IOException {
    BufferedInputStream bi = new BufferedInputStream(new FileInputStream(inFile));
    BitSequence bit = new BitSequence();
    int byt = bi.read();
    while (byt != -1) {
      byte b = (byte) byt;
      bit.appendBits(codes.get(b));
      byt = bi.read();
    }
    bi.close();
    return bit;
  }
  
  private void makeCodes(HuffBaseNode node, BitSequence code) {
    if (node == null) {
      return;
    }

    if (node instanceof HuffLeafNode) {
      if (node.equals(root)) {
        code.appendBit(0);
      }
      codes.put(((HuffLeafNode) node).getSymbol(), code);
      return;
    }
    
    HuffInternalNode in = (HuffInternalNode) node;
    BitSequence leftCode = new BitSequence(code);
    BitSequence rightCode = new BitSequence(code);
    leftCode.appendBit(0);
    rightCode.appendBit(1);
    makeCodes(in.getLeft(), leftCode);
    makeCodes(in.getRight(), rightCode);
  }

  /**
   * Decodes from an encoding.
   *
   * @param outFile file to save the decoding
   */
  public void decode(BitSequence encoding, File outFile) throws IOException {
    FileOutputStream saver = new FileOutputStream(outFile);
    Iterator<Integer> iterator = encoding.iterator();
    BitSequence bit = new BitSequence();
    while (iterator.hasNext()) {
      Integer code = iterator.next();
      bit.appendBit(code);
      if (codes.containsValue(bit)) {
        for (Byte byt : codes.keySet()) {
          if (bit.equals(codes.get(byt))) {
            saver.write(byt);
          }
        }
        bit = new BitSequence();
      }
    }
    saver.close();
  }

  private HuffBaseNode buildTree(HashMap<Byte, Integer> frequencies) {
    HuffBaseNode tmp1 = null;
    HuffBaseNode tmp2 = null;
    HuffBaseNode tmp3 = null;
    Byte symb = 0;
    PriorityQueue<HuffBaseNode> pq = new PriorityQueue<>();

    for (Byte symbol : frequencies.keySet()) {
      if (frequencies.keySet().size() == 1) {
        symb = symbol;
      }
      pq.add(new HuffLeafNode(symbol, frequencies.get(symbol)));
    }

    while (pq.size() > 1) { // While two items left
      tmp1 = pq.poll();
      tmp2 = pq.poll();
      tmp3 = new HuffInternalNode(tmp1, tmp2, tmp1.getWeight() + tmp2.getWeight());
      pq.add(tmp3); // Return new tree to heap
    }
    if (tmp3 == null) {
      if (frequencies.size() == 0) {
        return null;
      }
      return new HuffLeafNode(symb, frequencies.get(symb));
    }
    return tmp3; // Return the tree
  }

}
