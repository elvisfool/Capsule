package util;

import data.Tuple;

public class ArrayUtil {
    
    private ArrayUtil(){}
    
    /**
     * @param array
     * @return Tuple.x is index; Tuple.y is value
     */
    public static Tuple<Integer, Double> findMin(double[] array) {
        
        int minIdx = -1;
        double minValue = Double.POSITIVE_INFINITY;
        for(int i = 0; i < array.length; i++) {
            double v = array[i];
            if(v < minValue) {
                minIdx = i;
                minValue = v;
            }
        }
        return new Tuple<>(minIdx, minValue);
    }

    public static void swap(int[] array, int idxA, int idxB) {
        int temp = array[idxA];
        array[idxA] = array[idxB];
        array[idxB] = temp;
    }

    public static void swap(double[] array, int idxA, int idxB) {
        double temp = array[idxA];
        array[idxA] = array[idxB];
        array[idxB] = temp;
    }

    public static void swap(long[] array, int idxA, int idxB) {
        long temp = array[idxA];
        array[idxA] = array[idxB];
        array[idxB] = temp;
    }

    public static <T> void swap(T[] array, int idxA, int idxB) {
        T temp = array[idxA];
        array[idxA] = array[idxB];
        array[idxB] = temp;
    }
}
