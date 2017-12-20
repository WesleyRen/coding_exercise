import java.util.HashMap;
import java.util.Map;

/**
 */
public class UserSessionPath {
    private int nDepth;
    private Map<String, SessionPath> userSessionPath;

    public UserSessionPath(int nDepth) {
        this.nDepth = nDepth;
        this.userSessionPath = new HashMap<>();
    }

    public FixedDepthPath add(String userId, String page) {
        // If we have an existing session, add the page. Otherwise create a new session entry.
        if (!userSessionPath.containsKey(userId)) {
            SessionPath sessionPath = new SessionPath(this.nDepth);
            userSessionPath.put(userId, sessionPath);
        }
        return userSessionPath.get(userId).addPage(page);
    }
}
