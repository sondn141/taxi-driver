package edu.hust.soict.cbls.common.utils;

public class CollectionUtils {
    public static boolean inRangeInclusive(int min, int max, int val){
        if(min > max)
            throw new IllegalArgumentException("Min value can not be greater than max value: " + min + " " + max);
        return val >= min && val <= max;
    }
}
