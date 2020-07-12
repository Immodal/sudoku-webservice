package kychin.sudokuwebservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return randInt(min, max, rand);
    }

    /**
     * Generate a random Integer between min and max.
     * @param min Lower fence (inclusive)
     * @param max Upper fence (inclusive)
     * @param rand Random Object
     * @return Random Integer between min and max
     */
    public static int randInt(int min, int max, Random rand) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static List<Integer> range(int start, int end) {
        List<Integer> r = new ArrayList<>(end-start);
        for (int i=start; i<end; i++) {
            r.add(i);
        }
        return r;
    }
}
