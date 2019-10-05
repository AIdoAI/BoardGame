import java.util.ArrayList;
import java.util.regex.Pattern;

public class Methods {
    public int depth;
    public int visited;
    private int draw = 10;

    public Methods(int depth){
        this.depth = depth;
        this.visited = 0;
    }

    
    
    public int heuristic(Board board) {
        char myColor = Character.toLowerCase(board.isNext());
        char opColor = 'w';
        if (myColor == 'w') {
            opColor = 'b';
        }
        boolean up = board.isUp(myColor);

        int utility = 0;
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board [0].length; j++) {
                if (board.board [i] [j] == myColor) {
                    if (up) {
                        utility += (board.board.length - i);
                    }
                    else {
                        utility += (i + 1);
                    }
                }
                else if (board.board [i] [j] == opColor) {
                    if (up) {
                        utility -= (i + 1);
                    }
                    else {
                        utility -= (board.board.length - i);
                    }
                }
                else if (Character.toLowerCase(board.board [i] [j]) == myColor) {
                    utility += (board.board.length * 2);
                }
                else if (Character.toLowerCase(board.board [i] [j]) == opColor) {
                    utility -= (board.board.length * 2);
                }
            }
        }

        return utility;
    }


    public int minValue(State state) {
        Board board = state.getBoard();
        ArrayList<Coordinate> movelist = board.moveList(board.isNext());

        if (movelist.isEmpty()) {
            return 1;
        }

        int min = Integer.MAX_VALUE;
        for (Coordinate move: movelist) {
            Board repboard = board.repBoard();
            repboard.makeMove(move);

            State next = new State(repboard, state.getDepth() + 1, state.getLastCapture() + 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                next = new State(repboard, state.getDepth() + 1, 0);
            }

            int utility = 0;
            if (next.getLastCapture() < draw) {
                utility = maxValue(next);
            }

            if (utility < min) {
                min = utility;
            }

            visited++;
        }

        return min;
    }

    public int maxValue(State state) {
        Board board = state.getBoard();
        ArrayList<Coordinate> movelist = board.moveList(board.isNext());

        if (movelist.isEmpty()) {
            return -1;
        }

        int max = Integer.MIN_VALUE;
        for (Coordinate move: movelist) {
            Board repBoard = board.repBoard();
            repBoard.makeMove(move);

            State next = new State(repBoard, state.getDepth() + 1, state.getLastCapture() + 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                next = new State(repBoard, state.getDepth() + 1, 0);
            }

            int utility = 0;
            if (next.getLastCapture() < draw) {
                utility = minValue(next);
            }

            if (utility > max) {
                max = utility;
            }

            visited++;
        }

        return max;
    }

    public Coordinate minimax(Board board)
    {
        visited = 0;
        ArrayList<Coordinate> moveList = board.moveList(board.isNext());

        int max = Integer.MIN_VALUE;
        int index = -1;
        int i = 0;
        for (Coordinate move: moveList)
        {
            Board repBoard = board.repBoard();
            repBoard.makeMove(move);

            State state = new State(repBoard, 1, 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches())
            {
                state = new State(repBoard, 1, 0);
            }

            int utility = minValue(state);

            if (utility > max)
            {
                max = utility;
                index = i;
            }

            visited++;
            i++;
        }

        Coordinate move = board.moveList(board.isNext()).get(index);
        board.makeMove(move);

        System.out.println("I’m thinking...");
        System.out.println(" visited " + visited + " states");
        System.out.println(" best move: " + board.board [move.getX()] [move.getY()] + ":" + move.getMove() + ", value: " + ((double) max));

        return move;
    }
    
    
    private int minValueforAB(State state, int alpha, int beta){
        Board board = state.getBoard();
        ArrayList<Coordinate> movelist = board.moveList(board.isNext());
        if (movelist.isEmpty()){
            return 1;
        }
            int min = Integer.MAX_VALUE;
            for (Coordinate move: movelist)
            {
                Board repBoard = board.repBoard();
                repBoard.makeMove(move);

                State next = new State(repBoard, state.getDepth() + 1, state.getLastCapture()+ 1);
                Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
                if (capture.matcher(move.getMove().toUpperCase()).matches())
                {
                    next = new State(repBoard, state.getDepth() + 1, 0);
                }

                int utility = 0;
                if (next.getLastCapture() < draw)
                {
                    utility = maxValueforAB(next, alpha, beta);
                }

                if (utility < min)
                {
                    min = utility;
                }

                if (utility <= alpha)
                {
                    return utility;
                }

                if (utility < beta)
                {
                    beta = utility;
                }

                visited++;
            }

            return min;

    }


    public int maxValueforAB(State state, int alpha, int beta) {
        Board board = state.getBoard();
        ArrayList<Coordinate> moveList = board.moveList(board.isNext());

        if (moveList.isEmpty()) {
            return -1;
        }

        int max = Integer.MIN_VALUE;
        for (Coordinate move: moveList) {
            Board repBoard = board.repBoard();
            repBoard.makeMove(move);

            State next = new State(repBoard, state.getDepth() + 1, state.getLastCapture() + 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                next = new State(repBoard, state.getDepth() + 1, 0);
            }

            int utility = 0;
            if (next.getLastCapture() < draw) {
                utility = minValueforAB(next, alpha, beta);
            }

            if (utility > max) {
                max = utility;
            }

            if (utility >= beta) {
                return utility;
            }

            if (utility > alpha) {
                alpha = utility;
            }

            visited++;
        }

        return max;
    }


    public Coordinate alphaBetaPruning(Board board) {
        visited = 0;
        ArrayList<Coordinate> moveList = board.moveList(board.isNext());

        int max = Integer.MIN_VALUE;
        int index = -1;
        int i = 0;
        for (Coordinate move: moveList) {
            Board copy = board.repBoard();
            copy.makeMove(move);

            State state = new State(copy, 1, 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                state = new State(copy, 1, 0);
            }

            int utility = minValueforAB(state, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (utility > max) {
                max = utility;
                index = i;
            }

            visited++;
            i++;
        }

        Coordinate move = board.moveList(board.isNext()).get(index);
        board.makeMove(move);

        System.out.println("I’m thinking...");
        System.out.println(" visited " + visited + " states");
        System.out.println(" best move: " + board.board [move.getX()] [move.getY()] + ":" + move.getMove() + ", value: " + ((double) max));

        return move;
    }

    public int minValueforH(State state, int alpha, int beta) {
        Board board = state.getBoard();
        ArrayList<Coordinate> moveList = board.moveList(board.isNext());

        if (moveList.isEmpty()) {
            return Integer.MAX_VALUE-1;
        }
        else if (state.getDepth() >= depth) {
            return heuristic(state.board);
        }

        int min = Integer.MAX_VALUE;
        for (Coordinate move: moveList) {
            Board repBoard = board.repBoard();
            repBoard.makeMove(move);

            State next = new State(repBoard, state.getDepth() + 1, state.getLastCapture() + 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                next = new State(repBoard, state.getDepth() + 1, 0);
            }

            int utility = 0;
            if (next.getLastCapture() < draw) {
                utility = maxValueforH(next, alpha, beta);
            }

            if (utility < min) {
                min = utility;
            }

            if (utility <= alpha){
                return utility;
            }

            if (utility < beta){
                beta = utility;
            }

            visited++;
        }

        return min;
    }

    public int maxValueforH(State state, int alpha, int beta) {
        Board board = state.getBoard();
        ArrayList<Coordinate> moveList = board.moveList(board.isNext());

        if (moveList.isEmpty()) {
            return Integer.MIN_VALUE + 1;
        }
        else if (state.getDepth() >= depth) {
            return heuristic(state.board);
        }

        int max = Integer.MIN_VALUE;
        for (Coordinate move: moveList) {
            Board repBoard = board.repBoard();
            repBoard.makeMove(move);

            State next = new State(repBoard, state.getDepth() + 1, state.getLastCapture() + 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                next = new State(repBoard, state.getDepth() + 1, 0);
            }

            int utility = 0;
            if (next.getLastCapture() < draw) {
                utility = minValueforH(next, alpha, beta);
            }

            if (utility > max) {
                max = utility;
            }

            if (utility >= beta){
                return utility;
            }

            if(utility > alpha){
                alpha = utility;
            }

            visited++;
        }

        return max;
    }

    public Coordinate hMinimax(Board board) {
        visited = 0;
        ArrayList<Coordinate> moveList = board.moveList(board.isNext());

        int max = Integer.MIN_VALUE;
        int index = -1;
        int i = 0;
        for (Coordinate move: moveList) {
            Board repBoard = board.repBoard();
            repBoard.makeMove(move);

            State state = new State(repBoard, 1, 1);
            Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
            if (capture.matcher(move.getMove().toUpperCase()).matches()) {
                state = new State(repBoard, 1, 0);
            }

            int utility = minValueforH(state, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (utility > max) {
                max = utility;
                index = i;
            }

            visited++;
            i++;
        }

        Coordinate move = board.moveList(board.isNext()).get(index);
        board.makeMove(move);

        System.out.println("I’m thinking...");
        System.out.println(" visited " + visited + " states");
        System.out.println(" best move: " + board.board [move.getX()] [move.getY()] + ":" + move.getMove() + ", value: " + ((double) max));

        return move;
    }

    public Coordinate random(Board board) {
        ArrayList<Coordinate> moves = board.moveList(board.isNext());

        int random = (int) Math.random() * moves.size();
        Coordinate move = moves.get(random);
        board.makeMove(move);

        System.out.println("I’m thinking...");
        System.out.println(" picked randomly");
        System.out.println(" picked move: " + board.board [move.getX()] [move.getY()] + ":" + move.getMove());

        return move;
    }

    public Coordinate selection(Board board, int select) {
        switch (select)
        {
            case 1:
                return minimax(board);
            case 2:
                return alphaBetaPruning(board);
            case 3:
                return hMinimax(board);
            case 4:
                return random(board);

            default:
                return null;
        }
    }

}


