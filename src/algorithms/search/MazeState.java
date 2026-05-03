package algorithms.search;
import algorithms.mazeGenerators.Position;

public class MazeState extends AState{

    private final Position position;

    public MazeState(Position position) {
        super(position.toString());
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }
}
