package fr.m2dl.japanairlines.services;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import fr.m2dl.japanairlines.domain.Obstacle;

/**
 * Created by mfaure on 22/01/15.
 */
public class RandomGenerator {

    public static ArrayList<Obstacle> generate(){
        ArrayList<Obstacle> ret = new ArrayList<>();

        Random r = new Random();
        for(int i =0; i<3; ++i){
            ret.add(new Obstacle(r.nextInt(3),i*3));
            ret.add(new Obstacle(-1,-1));
            ret.add(new Obstacle(-1,-1));
        }
        return ret;
    }
}
