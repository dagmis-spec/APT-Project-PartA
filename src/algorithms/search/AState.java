package algorithms.search;

public abstract class AState {
    private final String state;
    private double cost;
    private AState cameFrom;

    public AState(String state) {
        this.state = state;
        this.cost = 0;
        this.cameFrom = null;
    }

    public String getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public AState getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(AState cameFrom) {
        this.cameFrom = cameFrom;
    }

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

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return state;
    }
}
