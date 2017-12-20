import java.util.Iterator;
import java.util.LinkedList;

/**
 # This is going to be the key for path set HashMap.
 * Why do we need this, why not just use a concatenated path string for key?
 * 1. Can't just use concatenated string for path, ambiguity can occur. For example:
 *      FixedDepthPathLiteral 1: "a", "b -> c", "d"; Concatenation of string: a -> b -> c -> d
 *      FixedDepthPathLiteral 2: "a -> b", "c", "d"; Concatenation of string: a -> b -> c -> d
 *
 * 2. Even we enclose them in double quotes, there's no guarantee ambiguity won't happen.
 *      -- example pending....
 *      (One benefit of this, though, is that this makes the the printout more distinguishable.
 *       So we keep this.)
 *      FixedDepthPathLiteral 1: "a", "b -> c", "d"; Concatenation of string: ["a" -> "b -> c" -> "d"]
 *      FixedDepthPathLiteral 2: "a -> b", "c", "d"; Concatenation of string: ["a -> b" -> "c" -> "d"]
 *
 * 3. Also, each page should be its own object (nodes in the path) anyway -- that's just conceptually correct.
 */

/**
 * This will be keys for HashMap. So make it immutable.
 */
final class FixedDepthPathLiteral {
    private final LinkedList<String> path;
    private final String pathStr;

    public FixedDepthPathLiteral(LinkedList<String> path) {
        this.path = path;
        this.pathStr = this.strGen();
    }

    private String strGen() {
        String pathStr = "";
        Iterator itPath = path.iterator();
        while (itPath.hasNext()) {
            if (pathStr.length() == 0) {
                pathStr = String.format("\"%s\"", itPath.next().toString());
            } else {
                pathStr = String.format("%s -> \"%s\"", pathStr, itPath.next().toString());
            }
        }
        return pathStr;
    }

    // for immutability, return a new object.
    public LinkedList<String> getPath() {
        return new LinkedList<>(path);
    }

    // override default hashcode and equals methods.
    @Override
    public int hashCode() {
        return this.pathStr.hashCode();
    }  // primitive data type, pass by value.

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FixedDepthPathLiteral))
            return false;
        if (obj == this)
            return true;

        FixedDepthPathLiteral other = (FixedDepthPathLiteral) obj;

        // We probably can use pathStr, but this is safe.
        return this.path.equals(other.path);
    }

    @Override
    public String toString() {
        return this.pathStr;
    }  // String is naturally immutable.
}
