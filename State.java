public class State {
    public Board board;
    private int depth;
    private int lastCapture;

    public State(Board board, int depth, int lastCapture){
        this.board = board;
        this.depth = depth;
        this.lastCapture = lastCapture;
    }

    public Board getBoard(){
        return board;
    }

    public int getDepth(){
        return depth;
    }

    public int getLastCapture(){
        return lastCapture;
    }
}
