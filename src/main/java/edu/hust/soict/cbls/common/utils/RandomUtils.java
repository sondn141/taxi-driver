package edu.hust.soict.cbls.common.utils;

import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.entity.Point;

import java.util.LinkedList;
import java.util.List;
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

    public static List<Double> randUniforms(int n, double min, double max){
        List<Double> uniforms = new LinkedList<>();
        for(int i = 0 ; i < n ; i ++)
            uniforms.add(randUniform(min, max));

        return uniforms;
    }

    public static List<Double> randNormals(int n, double mean, double std, double min, double max){
        List<Double> normals = new LinkedList<>();
        for(int i = 0 ; i < n ; i ++)
            normals.add(randNormal(mean, std, min, max, true));

        return normals;
    }

    public static class PointUtils{

        public static Point singlePoint(double min, double max){
            return new Point(randUniform(min, max), randUniform(min, max));
        }

        public static List<Point> uniformPoints(int n, double min, double max){
            List<Point> points = new LinkedList<>();
            for(int i = 0 ; i < n ; i ++){
                double x = randUniform(min, max);
                double y = randUniform(min, max);

                points.add(new Point(x, y));
            }

            return points;
        }

        public static List<Point> normalPoints(int n, double mean, double std, double min, double max){
            List<Point> points = new LinkedList<>();
            for(int i = 0 ; i < n ; i ++){
                double x = randNormal(mean, std, min, max, true);
                double y = randNormal(mean, std, min, max, true);

                points.add(new Point(x, y));
            }

            return points;
        }

        public static List<Point> gridPoints(int n, int numGrid, double maxX, double maxY){
            List<Point> points = new LinkedList<>();
            for(int i = 0 ; i < n ; i ++){
                // TODO: Add grid points
            }

            return points;
        }
    }
}
