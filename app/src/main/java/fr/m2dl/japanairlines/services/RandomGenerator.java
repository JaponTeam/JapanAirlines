package fr.m2dl.japanairlines.services;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mfaure on 22/01/15.
 */
public class RandomGenerator {

    public static ArrayList<int[]> generate(){
        ArrayList<int[]> ret = new ArrayList<>();

        Random r = new Random();
        int i1;
        int i2;
        int[] tab = new int[2];
        for(int i =0; i<30; ++i){
            i1 = r.nextInt(3);
            i2 = r.nextInt(5);
            tab[0] = i1;
            tab[1] = i2;
            ret.add(tab);


            Log.d("",i1 + " ## " + i2);

            i1 = r.nextInt(3);
            i2 = -1;
            tab[0] = i1;
            tab[1] = i2;
            ret.add(tab);

            Log.d("",i1 + " ## " + i2);

            i1 = r.nextInt(3);
            i2 = -1;
            tab[0] = i1;
            tab[1] = i2;
            ret.add(tab);

            Log.d("",i1 + " ## " + i2);

        }
        return ret;
    }
}
