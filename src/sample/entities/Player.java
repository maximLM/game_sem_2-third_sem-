package sample.entities;

public class Player {
    private String name;
    private int points;
    private int id;


    public Player(String name, int id) {
        this.name = name;
        this.points = 0;
        this.id = id;
    }

    public Player(String name) {
        this(name, -1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "PLAYER=" + name + "[" + points + "]";
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void incPoints() {
        ++points;
    }
}
