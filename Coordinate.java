import java.util.*;

public class Coordinate
{
    public int x;
    public int y;
    public int distance;
    public boolean king;   
    public Queue<Coordinate> ords;   
    public String move; 

    public Coordinate(int x, int y, int distance, boolean king) 
    {
        this.x = x;
        this.y = y;
        this.distance = distance;
        this.king = king;
        this.ords = new LinkedList<Coordinate>();
        this.move = "";
    }

    //Getters and setters
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getDistance()
    {
        return distance;
    }

    public boolean getKing()
    {
        return king;
    }

    public Queue<Coordinate> getords()
    {
        return ords;
    }

    public String getMove()
    {
        return move;
    }

    public void setMove(String move)
    {
        this.move = move;
    }

    @Override
    public boolean equals(Object o) {
        Coordinate other = (Coordinate) o;
        if ((this.x == other.x) && (this.y == other.y) && (this.king == other.king))
        {
            return true;
        }
        return false;
    }

    public String toString()
    {
        return move;
    }

}