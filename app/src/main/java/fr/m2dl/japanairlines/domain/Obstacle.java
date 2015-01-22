package fr.m2dl.japanairlines.domain;

/**
 * Created by msoum on 22/01/15.
 */
public class Obstacle {

    int x;
    int y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVide(){
        return (this.getX()==-1);
    }
}
