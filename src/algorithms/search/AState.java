package algorithms.search;

/**
 * Base class for a state in a search problem.
 * Concrete states, such as MazeState, add problem-specific data.
 */
public abstract class AState {
    // Unique textual representation of the state.
    private final String state;

    // Cost of reaching this state. Mainly useful for cost-based algorithms.
    private double cost;

    // Previous state in the discovered path, used for reconstructing a Solution.
    private AState cameFrom;

    /**
     * Creates a state with a unique textual representation.
     *
     * @param state the value used to identify and print this state.
     */
    public AState(String state) {
        this.state = state;
        this.cost = 0;
        this.cameFrom = null;
    }

    /**
     * @return the textual representation of this state.
     */
    public String getState() {
        return state;
    }

    /**
     * @return the cost assigned to this state.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost assigned to this state.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the previous state on the path to this state.
     */
    public AState getCameFrom() {
        return cameFrom;
    }

    /**
     * Sets the previous state on the path to this state.
     */
    public void setCameFrom(AState cameFrom) {
        this.cameFrom = cameFrom;
    }

    /**
     * States are considered equal if their textual representations are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AState)) {
            return false;
        }
        AState other = (AState) obj;
        return state.equals(other.state);
    }

    /**
     * Uses the same value as equals so states work correctly in hash-based collections.
     */
    @Override
    public int hashCode() {
        return state.hashCode();
    }

    /**
     * @return the printable representation of the state.
     */
    @Override
    public String toString() {
        return state;
    }
}
