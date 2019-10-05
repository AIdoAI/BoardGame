import java.util.*;
import java.util.regex.*;

public class Interact {

    public static String [] parseMove(String move) {
        move = move.toUpperCase();
        Pattern simple = Pattern.compile("\\p{Upper}++\\d++(-\\p{Upper}++\\d++)++");
        Pattern capture = Pattern.compile("\\p{Upper}++\\d++(X\\p{Upper}++\\d++)++");
        if (simple.matcher(move).matches()) {
            return move.split("-");
        }
        else if (capture.matcher(move).matches()) {
            return move.split("X");
        }
        else {
            return new String [0];
        }
    }

    public void chooseChunk(){
        System.out.println("Choose your game:");
        System.out.println("1. Small 4x4 Checkers");
        System.out.println("2. Standard 8x8 Checkers");
        System.out.print("Your choice? ");
    }

    public void methodChunk(){
        System.out.println("Choose your opponent:");
        System.out.println("1. An agent that uses MINIMAX");
        System.out.println("2. An agent that uses MINIMAX with alpha-beta pruning");
        System.out.println("3. An agent that uses H-MINIMAX with a fixed depth cutoff");
        System.out.println("4. An agent that plays randomly");
        System.out.print("Your choice? ");
    }

    public void interact(){
        Scanner scan = new Scanner(System.in);
        System.out.println("*******************************");
        System.out.println("Checkers by Ziyi Ai");

        //read choice for game size
        chooseChunk();
        String input = scan.nextLine();

        Pattern pattern = Pattern.compile("[1-2]");   
        while (!pattern.matcher(input).matches()) {
            System.out.println("\nInvalid Choice\n");
            chooseChunk();
            input = scan.nextLine();
        }

        int size = Integer.parseInt(input) * 4;

        //read choice for method
        methodChunk();
        input = scan.nextLine();

        pattern = Pattern.compile("[1-4]");

        while (!pattern.matcher(input).matches()) {
            methodChunk();
            input = scan.nextLine();
        }

        int opponent = Integer.parseInt(input);

        int depth = 3;
        if (opponent == 3) {
            System.out.print("Depth limit? ");
            input = scan.nextLine();

            pattern = Pattern.compile("\\d++");   
            while (!pattern.matcher(input).matches() && input.equals("0"))   
            {
                System.out.println("\nInvalid Choice\n");
                System.out.print("Depth limit? ");
                input = scan.nextLine();
            }

            depth = Integer.parseInt(input);  
        }
        
        System.out.print("Do you want to play BLACK (b) or WHITE (w)? ");
        input = scan.nextLine();

        pattern = Pattern.compile("[b,B,w,W]");  
        while (!pattern.matcher(input).matches()) {
            System.out.println("\nInvalid Choice\n");
            System.out.print("Do you want to play BLACK (b) or WHITE (w)? ");
            input = scan.nextLine();
        }
        
        Board board = new Board(size, (Character.toLowerCase(input.charAt(0)) == 'b'));
        Methods method = new Methods(depth);
        board.printBoard();

        //count play time for each player
        long playerTime = 0;
        long methodTime = 0;

        while(true) {
            char next = board.isNext();
            ArrayList <Coordinate> movelist = board.moveList(next);
            //game ending
            if (movelist.isEmpty()) {
                if (Character.toLowerCase(next) == 'w')
                {
                    System.out.println("Winner: BLACK");
                }
                else
                {
                    System.out.println("Winner: WHITE");
                }
                System.out.println("Total Time:");
                if (board.playerFirst)
                {
                    System.out.println("BLACK: " + ((playerTime * 1.0) / 1000) + " seconds");
                    System.out.println("WHITE: " + ((methodTime * 1.0) / 1000) + " seconds");
                }
                else
                {
                    System.out.println("BLACK: " + ((methodTime * 1.0) / 1000) + " seconds");
                    System.out.println("WHITE: " + ((playerTime * 1.0) / 1000) + " seconds");
                }
                break;
            }

            if (board.playerTurn)
            {
                System.out.print("Your move (? for help): ");
                long start = System.currentTimeMillis();
                input = scan.nextLine();
                String [] parse = parseMove(input);
                boolean invalid = true;
                Coordinate terminal = null;
                while (invalid) {
                    while (parse.length < 2 || input.equals("?")) {
                        if (input.equals("?")) {
                            System.out.println("\nAvaliable moves: " + movelist.toString() + "\n");
                        }
                        else {
                            System.out.println("\nInvalid Move\n");
                        }

                        System.out.print("Your move (? for help): ");
                        input = scan.nextLine();
                        parse = parseMove(input);
                    }

                    for (Coordinate move: movelist) {
                        String [] parses = parseMove(move.getMove());
                        if (parses.length != parse.length) {
                            continue;
                        }

                        for (int i = 0; i < parse.length; i++) {
                            if (parses [0].equals(parse [0]) && parses [1].equals(parse [1])) {
                                terminal = move;
                                board.makeMove(move);
                                invalid = false;
                                break;
                            }
                        }

                        if (!invalid) {
                            break;
                        }
                    }
                    parse = new String [0];
                }

                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                System.out.println("Elapsed time: " + ((elapsedTime * 1.0) / 1000) + " seconds");
                System.out.println(board.board[terminal.getX()] [terminal.getY()] + ":" + terminal.getMove());
                playerTime += elapsedTime;
            }
            else
            {
                long start = System.currentTimeMillis();
                Coordinate terminal = method.selection(board,opponent);
                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                System.out.println("Elapsed time: " + ((elapsedTime * 1.0) / 1000) + " seconds");
                System.out.println(board.board[terminal.getX()] [terminal.getY()] + ":" + terminal.getMove());
                methodTime += elapsedTime;
            }

            board.printBoard();
        }

        scan.close();
    }

}
