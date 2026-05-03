package algorithms.search;
import algorithms.mazeGenerators.Position;

/**
 * Represents a searchable state inside a maze.
 * Each MazeState wraps a Position and uses its string form as the generic state id.
 */
public class MazeState extends AState{

    // Maze-specific location data used for calculating neighboring cells.
    private final Position position;

    /**
     * Creates a maze state for the given position.
     *
     * @param position the cell represented by this state.
     */
    public MazeState(Position position) {
        super(position.toString());
        this.position = position;
    }

    /**
     * @return the maze position represented by this state.
     */
    public Position getPosition(){
        return position;
    }
}
