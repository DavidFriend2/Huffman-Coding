package huffman;

/**
 * HuffInternalNode class.
 *
 * @author David Friend
 */
public class HuffInternalNode extends HuffBaseNode {

  private HuffBaseNode left;
  private HuffBaseNode right;

  /**
   * Initializes the Internal Node.
   *
   * @param left HuffNode to the left of this internal node
   * @param right HuffNode to the right of this internal node
   * @param weight the weight of the node
   */
  public HuffInternalNode(HuffBaseNode left, HuffBaseNode right, int weight) {
    super(weight);
    this.left = left;
    this.right = right;
  }

  public HuffBaseNode getLeft() {
    return left;
  }

  public HuffBaseNode getRight() {
    return right;
  }

}
