import org.junit.Test;

import static org.junit.Assert.*;

/**
 */
public class UserSessionPathTest {
    final static int DEPTH = 3;
    final static String userIdA = "U1", userIdB = "U2";
    UserSessionPath userSessionPath = new UserSessionPath(DEPTH);
    @Test
    public void testAdd() {
        String itemA1 = "a", itemA2 = "b", itemA3 = "c";
        String itemB1 = "x", itemB2 = "y", itemB3 = "z";
        // use two user sessions to ensure that the paths are kept separately for them.
        FixedDepthPath fixedDepthPath1 = userSessionPath.add(userIdA, itemA1);
        FixedDepthPath fixedDepthPath2 = userSessionPath.add(userIdB, itemB1);
        assertNull("when sessionPath's path depth is less than the required depth, addPage should return null.",
                fixedDepthPath1);
        assertNull("when sessionPath's path depth is less than the required depth, addPage should return null.",
                fixedDepthPath2);

        fixedDepthPath1 = userSessionPath.add(userIdA, itemA2);
        fixedDepthPath2 = userSessionPath.add(userIdB, itemB2);
        assertNull("when sessionPath's path depth is less than the required depth, addPage should return null.",
                fixedDepthPath1);
        assertNull("when sessionPath's path depth is less than the required depth, addPage should return null.",
                fixedDepthPath2);

        fixedDepthPath1 = userSessionPath.add(userIdA, itemA3);
        fixedDepthPath2 = userSessionPath.add(userIdB, itemB3);
        assertNotNull("when sessionPath's path depth reaches required depth, addPage should return a FixedDepthPath object.",
                fixedDepthPath1);
        assertNotNull("when sessionPath's path depth reaches required depth, addPage should return a FixedDepthPath object.",
                fixedDepthPath2);

        // check if the path strings are OK.
        String expectedPathStrA = String.format("\"%s\"", itemA1);
        expectedPathStrA = String.format("%s -> \"%s\"", expectedPathStrA, itemA2);
        expectedPathStrA = String.format("%s -> \"%s\"", expectedPathStrA, itemA3);

        String expectedPathStrB = String.format("\"%s\"", itemB1);
        expectedPathStrB = String.format("%s -> \"%s\"", expectedPathStrB, itemB2);
        expectedPathStrB = String.format("%s -> \"%s\"", expectedPathStrB, itemB3);

        assertTrue(expectedPathStrA.equals(fixedDepthPath1.toString()));
        assertTrue(expectedPathStrB.equals(fixedDepthPath2.toString()));

    }
}