package huffman;

/**
 * Abstract HuffBaseNode class.
 *
 * @author David Friend
 */
public abstract class HuffBaseNode implements Comparable<HuffBaseNode> {

  private int weight;

  public HuffBaseNode(int weight) {
    this.weight = weight;
  }

  public int getWeight() {
    return weight;
  }

  @Override
  public int compareTo(HuffBaseNode other) {
    if (getWeight() < other.getWeight()) {
      return -1;
    } else if (getWeight() > other.getWeight()) {
      return 1;
    } else {
      if (other instanceof HuffLeafNode && this instanceof HuffLeafNode) {
        HuffLeafNode thisNode = (HuffLeafNode) this;
        HuffLeafNode otherNode = (HuffLeafNode) other;
        Byte thisByte = thisNode.getSymbol();
        Byte otherByte = otherNode.getSymbol();
        return thisByte.compareTo(otherByte);
      }
      return 0;
    }

  }

}
