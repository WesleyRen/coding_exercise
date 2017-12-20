import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * "N" is the setting for the depth of the paths we're interested in.
 * For example: 2: / -> subscribers
 * For example: 3: / -> subscribers -> email_address
 */

/**
 * Processing logic:
 * 1. For each entry input, we create a UserSessionPath object, with the path depth we're inspecting.
 * 2. If a path in a UserSessionPath reaches the path depth, we add/update our path set for the depth.
 * 3. After we processed everything, we sort.
 * 4. Output top N. Tie in sorting is not handled at this time. (Top rank instead of simple order?)
 */
public class TopNDepthPath {
    protected Map<FixedDepthPathLiteral, FixedDepthPath> nPathSet = new HashMap<>();

    /*
    1. Relaxed scope for unit test purpose - should be private.
    2. Returns the Map being updated, for unit test.
    */
    protected static Map<FixedDepthPathLiteral, FixedDepthPath>
                updateNPathSet(FixedDepthPath fixedDepthPath, Map<FixedDepthPathLiteral, FixedDepthPath> nPathSet) {
        // System.out.printf("fixedDepthPath: %s, hashCode: %d.%n", fixedDepthPath.toString(), fixedDepthPath.hashCode());
        FixedDepthPathLiteral pathKey = fixedDepthPath.getFixedDepthPathLiteral();
        if (nPathSet.containsKey(pathKey)) {
            nPathSet.get(pathKey).increaseFreq();
        } else {
            nPathSet.put(pathKey, fixedDepthPath);
        }
        return nPathSet;
    }

    public FixedDepthPath addLineToUserSessionPath(String line, UserSessionPath userSessionPath) {
        String[] entryInputs = line.split("\\s+");
        // System.out.printf("%n%n now processing user id: %s, page %s.%n", entryInputs[0],entryInputs[1]);
        /*
         * a userSessionPath has path depth we're inspecting.
         * If it reaches that depth, it returns a FixedDepthPath object, so we can update the path set.
         */
        FixedDepthPath fixedDepthPath = userSessionPath.add(entryInputs[0], entryInputs[1]);
        if (fixedDepthPath != null) {
            updateNPathSet(fixedDepthPath, this.nPathSet);
        }
        return fixedDepthPath;
    }

    public ArrayList<FixedDepthPath> getTopNDepthPath(int k) {
        ArrayList<FixedDepthPath> fixedDepthPathList = this.nPathSet.values().stream().collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(fixedDepthPathList);
        if (k < fixedDepthPathList.size()) {
            fixedDepthPathList.subList(k, fixedDepthPathList.size()).clear();
        }
        return fixedDepthPathList;
    }

    public static void main(String[] args) {
        String argumentMsg = String.format("%n--Input should be:%n--  <path_depth_to_find:int> <top_N_to_show:int> <input_file_name:string>");
        if (args.length < 3) {
            throw new IllegalArgumentException(argumentMsg);
        }
        int pathDepth = Integer.parseInt(args[0]);
        int showN = Integer.parseInt(args[1]);
        String inputFileName = args[2];
        UserSessionPath userSessionPath = new UserSessionPath(pathDepth);

        TopNDepthPath topNDepthPath = new TopNDepthPath();
        try (Stream<String> stream = Files.lines(Paths.get(inputFileName))) {
            stream.forEach(line -> topNDepthPath.addLineToUserSessionPath(line, userSessionPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("%n printing all paths. (really?):%n");
        for (FixedDepthPathLiteral key : topNDepthPath.nPathSet.keySet()) {
            System.out.printf("%s : %d%n", topNDepthPath.nPathSet.get(key).toString(), topNDepthPath.nPathSet.get(key).getFrequency());
        }

        System.out.printf("%n printing top %d path by frequency:%n", showN);
        topNDepthPath.getTopNDepthPath(showN).stream().forEach(p -> System.out.printf("%s : %d%n", p.toString(), p.getFrequency()));
    }
}
