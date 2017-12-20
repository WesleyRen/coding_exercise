import java.util.LinkedList;

/**
 */
final class FixedDepthPath implements Comparable<FixedDepthPath> {
    private FixedDepthPathLiteral fixedDepthPathLiteral;
    private long frequency;

    public FixedDepthPath(int nDepth, LinkedList<String> path) {
        if (nDepth != path.size()) {
            String errorStr = String.format("Input path size (%d) doesn't match the intended depth (%d).%n", path.size(), nDepth);
            throw new IllegalArgumentException(errorStr);
        }
        this.fixedDepthPathLiteral = new FixedDepthPathLiteral(path);
        this.frequency = 1;
    }

    public void increaseFreq() {
        frequency++;
    }

    public long getFrequency() {
        return frequency;
    }

    public FixedDepthPathLiteral getFixedDepthPathLiteral() {
        return new FixedDepthPathLiteral(this.fixedDepthPathLiteral.getPath());
    }

    @Override
    public int compareTo(FixedDepthPath o) {
        // return -1 * (this.frequency - o.frequency) // can't do this. "frequency" is long data type.
        // in descending order:
        long thisCompToOther = -1 * (this.frequency - o.frequency);
        return (thisCompToOther > 0) ? 1 : (thisCompToOther < 0) ? -1 : 0;
    }

    @Override
    public String toString() {
        return this.fixedDepthPathLiteral.toString();
    }

}
