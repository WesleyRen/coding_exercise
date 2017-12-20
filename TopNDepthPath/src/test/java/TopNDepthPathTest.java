import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.*;

/**
 */
public class TopNDepthPathTest {

    final static int DEPTH = 3;
    UserSessionPath userSessionPath = new UserSessionPath(DEPTH);
    FixedDepthPath fixedDepthPath;

    FixedDepthPath fixedDepthPath1;
    FixedDepthPath fixedDepthPath2;
    FixedDepthPath fixedDepthPath3;
    FixedDepthPath fixedDepthPath4;
    // DATA settings:
    final static String userIdA = "U1", userIdB = "U2", userIdC = "U3", userIdD = "U4";
    final static String ROOT = "/", SUB = "subscriber", FILTER = "filter", EXPORT = "export", CAT = "catalog", EDIT = "edit";
    String[] lines = new String[14];

    TopNDepthPath topNDepthPath = new TopNDepthPath();

    final LinkedList<String> path = new LinkedList<>();
    String expectedPathStr;
    String expectedTop1PathStr, expectedTop2PathStr;
    int expectedTop1PathFreq, expectedTop2PathFreq;

    @Before
    public void setUpForUpdateNPathSet() {
        String item1 = "a", item2 = "b", item3 = "c";
        path.add(item1);
        expectedPathStr = String.format("\"%s\"", item1);
        path.add(item2);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item2);
        path.add(item3);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item3);
    }


    @Before
    public void setUpForAddLineToUserSessionPath() {
        // order matters for testing -- we need to know that the paths are kept separately for each user.
        lines[0] = userIdA + " " + ROOT;
        lines[1] = userIdA + " " + SUB;

        lines[2] = userIdB + " " + ROOT;
        lines[3] = userIdB + " " + SUB;

        lines[4] = userIdA + " " + FILTER;
        lines[5] = userIdA + " " + EXPORT;

        lines[6] = userIdB + " " + FILTER;
        lines[7] = userIdB + " " + EXPORT;

        lines[8] = userIdC + " " + ROOT;
        lines[9] = userIdC + " " + CAT;
        lines[10] = userIdC + " " + EDIT;

        lines[11] = userIdD + " " + ROOT;
        lines[12] = userIdD + " " + SUB;
        lines[13] = userIdD + " " + FILTER;

        /*
        So, we're expecting the following for top 2 of the 3-depth paths.
            path string: "/" -> "subscribers" -> "filter"     ; frequency: 3
            path string: "subscribers" -> "filter" -> "export"; frequency: 2
        */

        expectedTop1PathStr = String.format("\"%s\" -> \"%s\" -> \"%s\"", ROOT, SUB, FILTER);
        expectedTop1PathFreq = 3;
        expectedTop2PathStr = String.format("\"%s\" -> \"%s\" -> \"%s\"", SUB, FILTER, EXPORT);
        expectedTop2PathFreq = 2;

        int i = 0;
        for (; i < 4; i++) {
            fixedDepthPath1 = topNDepthPath.addLineToUserSessionPath(lines[i], userSessionPath);
        }

        for (; i < 8; i++) {
            fixedDepthPath2 = topNDepthPath.addLineToUserSessionPath(lines[i], userSessionPath);
        }

        for (; i < 10; i++) {
            fixedDepthPath3 = topNDepthPath.addLineToUserSessionPath(lines[i], userSessionPath);
        }

        for (; i < lines.length; i++) {
            fixedDepthPath4 = topNDepthPath.addLineToUserSessionPath(lines[i], userSessionPath);
        }
    }

    @Test
    public void testUpdateNPathSet() throws Exception {
        fixedDepthPath = new FixedDepthPath(DEPTH, path);
        Map<FixedDepthPathLiteral, FixedDepthPath> nPathSet = new HashMap<>();
        TopNDepthPath.updateNPathSet(fixedDepthPath, nPathSet);
        assertTrue("frequency should be stay at 1", fixedDepthPath.getFrequency() == 1);
        assertEquals(1, nPathSet.size());

        nPathSet = TopNDepthPath.updateNPathSet(fixedDepthPath, nPathSet);
        assertTrue("frequency should've increased to 2", fixedDepthPath.getFrequency() == 2);
        assertEquals(1, nPathSet.size());
    }

    @Test
    public void testAddLineToUserSessionPath() throws Exception {
        // I understand fixedDepthPath1 will only have the one from the last in the "for" loop during
        // setup. It's kind of like sampling. Good enough.
        assertNull("when a user session's path depth is less than the required depth, add line should return null.",
                fixedDepthPath1);
        assertNotNull("when sessionPath's path depth reaches required depth, addPage should return a FixedDepthPath object.",
                fixedDepthPath2);
        assertNull("when a user session's path depth is less than the required depth, add line should return null.",
                fixedDepthPath3);
        assertNotNull("when sessionPath's path depth reaches required depth, addPage should return a FixedDepthPath object.",
                fixedDepthPath4);
    }

    @Test
    public void testGetTopNDepthPath() throws Exception {
        assertEquals(3, topNDepthPath.nPathSet.size());
        ArrayList<FixedDepthPath> fixedDepthPathList = topNDepthPath.getTopNDepthPath(2);
        assertEquals(2, fixedDepthPathList.size());
        /*
        Next step: checking the content of the top N paths. See setup for expected string value.
        */
        assertEquals(expectedTop1PathFreq, fixedDepthPathList.get(0).getFrequency());
        assertEquals(expectedTop2PathFreq, fixedDepthPathList.get(1).getFrequency());
        System.out.printf("#1 expected: [%s]; frequency: %d%n", expectedTop1PathStr, expectedTop1PathFreq);
        System.out.printf("#1    found: [%s]; frequency: %d%n",
                fixedDepthPathList.get(0).toString(), fixedDepthPathList.get(0).getFrequency());
        assertTrue(expectedTop1PathStr.equals(fixedDepthPathList.get(0).toString()));
        System.out.printf("#2 expected: [%s]; frequency: %d%n", expectedTop2PathStr, expectedTop2PathFreq);
        System.out.printf("#2    found: [%s]; frequency: %d%n",
                fixedDepthPathList.get(1).toString(), fixedDepthPathList.get(1).getFrequency());
        assertTrue(expectedTop2PathStr.equals(fixedDepthPathList.get(1).toString()));

    }
}