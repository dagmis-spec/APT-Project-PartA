package algorithms.search;

import java.util.ArrayList;

/**
 * Represents the path found by a search algorithm.
 */
public class Solution {
    private final ArrayList<AState> solutionPath;

    public Solution(ArrayList<AState> solutionPath) {
        this.solutionPath = solutionPath;
    }

    /**
     * @return the ordered path from the start state to the goal state.
     */
    public ArrayList<AState> getSolutionPath() {
        return solutionPath;
    }
}
