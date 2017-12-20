import java.util.LinkedList;

/**
 * The reason I keep path depth setting in SessionPath class is to
 * reduce memory consumption with a sliding path window for user sessions.
 * The thinking is that user session path history is a lot greater than path depth we're looking for.
 *
 * On the other hand we can keep the depth setting in TopNDepthPath and keep all the user session history.
 * That approach gives more flexibility on investigating different path depth without re-read session history files.
 *
 * This is a trade off -- depends on what our priority is.
 */
class SessionPath {
    private int nDepth;
    private LinkedList<String> path = new LinkedList<>();

    public SessionPath(int nDepth) {
        if (nDepth < 1) {
            throw new IllegalArgumentException("FixedDepthPathLiteral depth has to be greater than 0.");
        }
        this.nDepth = nDepth;
        this.path = new LinkedList<>();
    }

    // return an FixedDepthPath for logging.
    public FixedDepthPath addPage(String page) {
        // if the path has reached its defined nDepth, let's make the queue length stay the same.
        if (path.size() == nDepth) {
            path.remove();
        }
        path.add(page);
        // we have enough pages for an FixedDepthPath.
        if (path.size() == nDepth) {
            return new FixedDepthPath(nDepth, path);
        }
        return null;
    }

}
