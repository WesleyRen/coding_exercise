import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 */
public class FixedDepthPathTest {

    final static int DEPTH = 3;
    final LinkedList<String> path = new LinkedList<>();
    String expectedPathStr;
    FixedDepthPathLiteral fixedDepthPathLiteral1;
    FixedDepthPathLiteral fixedDepthPathLiteral2;
    FixedDepthPath fixedDepthPath;
    FixedDepthPath fixedDepthPath1;
    FixedDepthPath fixedDepthPath2;

    @Before
    public void setUp() {
        String item1 = "a", item2 = "b", item3 = "c";
        path.add(item1);
        expectedPathStr = String.format("\"%s\"", item1);
        path.add(item2);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item2);
        path.add(item3);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item3);

        fixedDepthPath = new FixedDepthPath(DEPTH, path);
        // for fixedDepthPathLiteralTest.
        fixedDepthPathLiteral1 = fixedDepthPath.getFixedDepthPathLiteral();
        fixedDepthPathLiteral2 = fixedDepthPath.getFixedDepthPathLiteral();

        fixedDepthPath1 = new FixedDepthPath(DEPTH, path);
        fixedDepthPath2 = new FixedDepthPath(DEPTH, path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorException() {
        FixedDepthPath fixedDepthPathException = new FixedDepthPath(DEPTH + 1, path);
    }

    @Test
    public final void testFrequency() {
        assertTrue("frequency should be initialized to 1", fixedDepthPath1.getFrequency() == 1);
        assertTrue("frequency should be initialized to 1", fixedDepthPath2.getFrequency() == 1);

        int n = 10;
        for (int i = 0; i < n; i++) {
            assertTrue("path frequency should be " + (i + 1), fixedDepthPath.getFrequency() == (i + 1));
            fixedDepthPath.increaseFreq();
        }
    }

    @Test
    public final void testFixedDepthPathLiteral() {
        assertTrue("different FixedDepthPath objects have to return defensive copies of FixedDepthPathLiteral.",
                fixedDepthPathLiteral1 != fixedDepthPathLiteral2 && fixedDepthPathLiteral1.equals(fixedDepthPathLiteral2));

    }

    @Test
    public final void testCompareTo() {
        assertEquals("same frequencies should return 0.", fixedDepthPath1.compareTo(fixedDepthPath2), 0);
        fixedDepthPath1.increaseFreq();
        assertEquals("larger frequency should return a negative value.", fixedDepthPath1.compareTo(fixedDepthPath2), -1);
        assertEquals("smaller frequency should return a negative value.", fixedDepthPath2.compareTo(fixedDepthPath1), 1);
    }

    @Test
    public final void testToString() {
        assertTrue(expectedPathStr.equals(fixedDepthPath.toString()));
    }

}
