import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 */
public class FixedDepthPathLiteralTest {

    final LinkedList<String> path = new LinkedList<>();
    String expectedPathStr;
    FixedDepthPathLiteral fixedDepthPathLiteral;
    FixedDepthPathLiteral fixedDepthPathLiteral1;
    FixedDepthPathLiteral fixedDepthPathLiteral2;

    @Before
    public void setUp() {
        String item1 = "a", item2 = "b", item3 = "c";
        path.add(item1);
        expectedPathStr = String.format("\"%s\"", item1);
        path.add(item2);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item2);
        path.add(item3);
        expectedPathStr = String.format("%s -> \"%s\"", expectedPathStr, item3);

        fixedDepthPathLiteral = new FixedDepthPathLiteral(path);
        fixedDepthPathLiteral1 = new FixedDepthPathLiteral(path);
        fixedDepthPathLiteral2 = new FixedDepthPathLiteral(path);
    }

    @Test
    public final void TestGetPath() {
        // check get path from the same FixedDepthPathLiteral object twice will result in two different objects.
        LinkedList<String> path1 = fixedDepthPathLiteral.getPath();
        LinkedList<String> path2 = fixedDepthPathLiteral.getPath();
        // Their contents should be the same, not the same object.
        assertTrue("different FixedDepthPathLiteral objects have to return defensive copies of paths.",
                path1 != path2 && path1.equals(path2));
        // check against original path.
        // Their contents should be the same, not the same object.
        assertTrue("different FixedDepthPathLiteral objects have to return defensive copies of paths.",
                path != path1 && path.equals(path1));
        assertTrue("different FixedDepthPathLiteral objects have to return defensive copies of paths.",
                path != path2 && path.equals(path2));
    }


    @Test
    public final void testHashCode() {
        assertEquals(fixedDepthPathLiteral1.hashCode(), fixedDepthPathLiteral2.hashCode());
    }

    @Test
    public final void testToString() {
        assertTrue(expectedPathStr.equals(fixedDepthPathLiteral.toString()));
    }
}
