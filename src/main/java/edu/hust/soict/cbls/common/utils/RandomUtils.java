package edu.hust.soict.cbls.common.utils;

import edu.hust.soict.cbls.common.config.Const;

import java.util.Random;

public class RandomUtils {

    public static final Random rd = new Random(Const.SEED);

    public static double randUniform(double min, double max){
        if(min > max)
            throw new IllegalArgumentException("Min value can not be greater than max value");
        return min + (max - min)*rd.nextDouble();
    }

    public static double randNormal(double mean, double std, double min, double max, boolean marginIfOutOfRange){
        if(min > max)
            throw new IllegalArgumentException("Min value can not be greater than max value");
        double val = rd.nextGaussian() * std + mean;
        if(marginIfOutOfRange){
            val = val < min ? min : (val > max ? max : val);
        } else{
            val = min + std*rd.nextDouble();
        }

        return val;
    }
}
