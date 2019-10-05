import java.util.*;

public class Board {
    public char[][] board;
    public boolean playerFirst;
    public boolean playerTurn;
    public int distance;
    public boolean moreMoves;

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setMoreMoves(boolean moreMoves) {
        this.moreMoves = moreMoves;
    }

    public Board(int size, boolean playerFirst) {
        board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i % 2 != j % 2) {
                    if (i < (size - 2) / 2) {
                        if (playerFirst) {
                            board[i][j] = 'w';
                        } else {
                            board[i][j] = 'b';
                        }
                    } else if (i >= size - (size - 2) / 2) {
                        if (playerFirst) {
                            board[i][j] = 'b';
                        } else {
                            board[i][j] = 'w';
                        }
                    } else {
                        board[i][j] = ' ';
                    }
                } else {
                    board[i][j] = ' ';
                }
            }
        }
        this.playerFirst = playerFirst;
        playerTurn = playerFirst;
    }

    public Board repBoard(){
        Board rep = new Board(board.length, playerFirst);
        rep.playerTurn = playerTurn;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board [i].length; j++)
            {
                rep.board [i] [j] = board [i] [j];
            }
        }

        return rep;
    }

    public char isNext() {
        if ((playerFirst && playerTurn) || (!playerFirst && !playerTurn)) {
            return 'b';
        } else {
            return 'w';
        }
    }

    public boolean isUp(char color) {
        if ((playerFirst && (color == 'b')) || (!playerFirst && (color == 'w'))) {
            return true;
        }
        return false;
    }

    private void printBorrder() {
        System.out.print(" +");
        for (int i = 0; i < board.length; i++) {
            System.out.print("-+");
        }
        System.out.println("");
    }

    public void printBoard() {
        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < board.length; i++) {
            System.out.print((i + 1) + " ");
        }
        System.out.println("");
        printBorrder();

        for (int i = 0; i < board.length; i++) {
            System.out.print(((char) (i + 'a')) + "|");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println("");
            printBorrder();
        }

        System.out.print("Next to play: ");
        if (isNext() == 'w') {
            System.out.println("WHITE");
        } else {
            System.out.println("BLACK");
        }
        System.out.println();
    }

    
    public boolean makeMove(Coordinate cord){
        char next = isNext();
        boolean up = isUp(next);
        Coordinate first = cord.getords().poll();
        while(!cord.getords().isEmpty()){
            Coordinate head = cord.getords().poll();
            board[first.getX()][first.getY()] = ' ';
            board [first.getX() + ((head.getX() - first.getX()) / 2)] [first.getY() + ((head.getY() - first.getY()) / 2)] = ' ';
            first = head;
        }
        board [first.getX()] [first.getY()] = ' ';
        board [first.getX() + ((cord.getX() - first.getX())/2)][first.getY() + ((cord.getY() - first.getY()) / 2)] = ' ';

        if (((up && cord.getX() == 0) || (!up && cord.getX() == board.length - 1)) || cord.getKing())   //Places piece on final coordinate
        {
            board [cord.getX()] [cord.getY()] = Character.toUpperCase(next);
        }
        else
        {
            board [cord.getX()] [cord.getY()] = Character.toLowerCase(next);
        }

        playerTurn = !playerTurn; 

        return false;
        
    }
    

    public void moveUp(char opColor, Coordinate coord, Queue<Coordinate> coordinates, ArrayList<Coordinate> movelist) {
        if ((coord.getX() - 1 >= 0) && (coord.getY() + 1 < board.length) && (coord.getDistance() == 0) &&
                ((board[coord.getX() - 1][coord.getY() + 1] == ' ') && (distance <= 1)))
        {
            setDistance(1);
            Coordinate c = new Coordinate(coord.getX() - 1, coord.getY() + 1, coord.getDistance() + 1, coord.getKing());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "-" + ((char) ((coord.getX() - 1) + 'A')) + (coord.getY() + 2));
            movelist.add(c);
            setMoreMoves(true);
        }
        else if ((coord.getX() - 2 >= 0) && (coord.getY() + 2 < board.length) &&    //Checks bounds
                ((Character.toLowerCase(board[coord.getX() - 1][coord.getY() + 1]) == opColor) && (board[coord.getX() - 2][coord.getY() + 2] == ' ')))
        {

            Coordinate c = new Coordinate(coord.getX() - 2, coord.getY() + 2, coord.getDistance() + 2, coord.getKing());
            c.getords().addAll(coord.getords());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "x" + ((char) (c.getX() + 'A')) + (c.getY() + 1));
            coordinates.add(c);
            setMoreMoves(true);
        }

        if ((coord.getX() - 1 >= 0) && (coord.getY() - 1 >= 0) && (coord.getDistance() == 0) && ((board[coord.getX() - 1][coord.getY() - 1] == ' ') && (distance <= 1)))
        {
            setDistance(1);
            Coordinate c = new Coordinate(coord.getX() - 1, coord.getY() - 1, coord.getDistance() + 1, coord.getKing());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "-" + ((char) ((coord.getX() - 1) + 'A')) + coord.getY());
            movelist.add(c);
            setMoreMoves(true);
        }
        else if ((coord.getX() - 2 >= 0) && (coord.getY() - 2 >= 0) &&    //Checks bounds
                ((Character.toLowerCase(board[coord.getX() - 1][coord.getY() - 1]) == opColor) && (board[coord.getX() - 2][coord.getY() - 2] == ' ')))
        {
            Coordinate c = new Coordinate(coord.getX() - 2, coord.getY() - 2, coord.getDistance() + 2, coord.getKing());
            c.getords().addAll(coord.getords());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "x" + ((char) (c.getX() + 'A')) + (c.getY() + 1));
            coordinates.add(c);
            setMoreMoves(true);
        }
    }

    public void moveDown(char opColor, Coordinate coord, Queue<Coordinate> coordinates, ArrayList<Coordinate> movelist) {
        if ((coord.getX() + 1 < board.length) && (coord.getY() - 1 >= 0) && (coord.getDistance() == 0) && ((board[coord.getX() + 1][coord.getY() - 1] == ' ') && (distance <= 1))) {
            setDistance(1);
            Coordinate c = new Coordinate(coord.getX() + 1, coord.getY() - 1, coord.getDistance() + 1, coord.getKing());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "-" + ((char) ((coord.getX() + 1) + 'A')) + coord.getY());
            movelist.add(c);
            setMoreMoves(true);
        }
        else if ((coord.getX() + 2 < board.length) && (coord.getY() - 2 >= 0) &&
                ((Character.toLowerCase(board[coord.getX() + 1][coord.getY() - 1]) == opColor) && (board[coord.getX() + 2][coord.getY() - 2] == ' '))) {

            Coordinate c = new Coordinate(coord.getX() + 2, coord.getY() - 2, coord.getDistance() + 2, coord.getKing());
            c.getords().addAll(coord.getords());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "x" + ((char) (c.getX() + 'A')) + (c.getY() + 1));
            coordinates.add(c);
            setMoreMoves(true);
        }

        if ((coord.getX() + 1 < board.length) && (coord.getY() + 1 < board.length) && (coord.getDistance() == 0) && ((board[coord.getX() + 1][coord.getY() + 1] == ' ') && (distance <= 1))) {
            setDistance(1);
            Coordinate c = new Coordinate(coord.getX() + 1, coord.getY() + 1, coord.getDistance() + 1, coord.getKing());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "-" + ((char) ((coord.getX() + 1) + 'A')) + (coord.getY() + 2));
            movelist.add(c);
            setMoreMoves(true);
        }
        else if ((coord.getX() + 2 < board.length) && (coord.getY() + 2 < board.length) && ((Character.toLowerCase(board[coord.getX() + 1][coord.getY() + 1]) == opColor) && (board[coord.getX() + 2][coord.getY() + 2] == ' '))) {
            Coordinate c = new Coordinate(coord.getX() + 2, coord.getY() + 2, coord.getDistance() + 2, coord.getKing());
            c.getords().addAll(coord.getords());
            c.getords().add(coord);
            c.setMove(coord.getMove() + "x" + ((char) (c.getX() + 'A')) + (c.getY() + 1));
            coordinates.add(c);
            setMoreMoves(true);
        }
    }

    public ArrayList<Coordinate> moveList(char color) {
        char myColor = Character.toLowerCase(color);
        char opColor = 'b';
        if (myColor == 'b') {
            opColor = 'w';
        }

        boolean up = isUp(myColor);
        ArrayList<Coordinate> movelist = new ArrayList<>();
        Queue<Coordinate> coordinates = new LinkedList<>();
        //deal with kings
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == myColor) {
                    Coordinate cords = new Coordinate(i, j, 0, false);
                    cords.setMove("" + ((char) (cords.getX() + 'A')) + (cords.getY() + 1));
                    coordinates.add(cords);
                }
                else if (Character.toLowerCase(board[i][j]) == myColor) {
                    Coordinate cords = new Coordinate(i, j, 0, true);
                    cords.setMove("" + ((char) (cords.getX() + 'A')) + (cords.getY() + 1));
                    coordinates.add(cords);
                }
            }
        }
        setDistance(1);
        while (!coordinates.isEmpty()) {
            Coordinate first = coordinates.poll();
            if (first.getKing()) {
                boolean skip = false;
                for (Coordinate seen : first.getords()) {
                    if (first.equals(seen)) {
                        skip = true;
                    }
                }

                if (skip) {
                    continue;
                }
            }
            setMoreMoves(false);
            if (up || first.getKing()) {
                moveUp(opColor, first, coordinates, movelist);
            }

            if (!up || first.getKing()) {
                moveDown(opColor, first, coordinates, movelist);
            }

            if (!moreMoves) {
                if (first.getDistance() > distance) {
                    setDistance(first.getDistance());
                    movelist.clear();
                    movelist.add(first);
                } else if (first.getDistance() == distance) {
                    movelist.add(first);
                }
            }

        }
        return movelist;
    }
}


