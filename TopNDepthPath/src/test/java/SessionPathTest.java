import org.junit.Test;

import static org.junit.Assert.*;

/**
 */
public class SessionPathTest {
    final static int DEPTH = 3;
    SessionPath sessionPath = new SessionPath(DEPTH);

    @Test
    public void testAddPage() {
        String item1 = "a", item2 = "b", item3 = "c";
        FixedDepthPath fixedDepthPath = sessionPath.addPage(item1);
        assertNull("when sessionPath's path depth is less than the required depth, addPage should return null.",
                fixedDepthPath);
        fixedDepthPath = sessionPath.addPage(item2);
        assertNull("when sessionPath's path depth is less than the required depth, addPage should return null.",
                fixedDepthPath);
        fixedDepthPath = sessionPath.addPage(item3);
        assertNotNull("when sessionPath's path depth reaches required depth, addPage should return a FixedDepthPath object.",
                fixedDepthPath);

        String expectedPathStr = String.format("\"%s\"", item1);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item2);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item3);

        assertTrue(expectedPathStr.equals(fixedDepthPath.toString()));
    }

}