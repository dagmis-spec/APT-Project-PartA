package algorithms.search;

/**
 * Defines the common behavior every search algorithm must provide.
 */
public interface ISearchingAlgorithm {
    /**
     * Runs the search algorithm on a searchable problem.
     *
     * @param searchable the problem to solve.
     * @return a solution path from the start state to the goal state.
     */
    Solution solve(ISearchable searchable);

    /**
     * @return the display name of the search algorithm.
     */
    String getName();

    /**
     * @return how many states were evaluated during the last search.
     */
    int getNumberOfNodesEvaluated();
}
