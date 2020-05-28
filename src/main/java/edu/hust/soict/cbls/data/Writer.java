package edu.hust.soict.cbls.data;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Taxi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Writer {

    private static final Logger logger = LoggerFactory.getLogger(Writer.class);
    static final String OUTPUT_CLASS_SOLVER_SIGNED = "---> ";

    public static synchronized void write(Solution solution, String path, Class<?> solverClass, boolean append){
        List<List<Integer>> routes = solution.convert();
        StringBuilder strBuilder = new StringBuilder();
        if(solverClass != null)
            strBuilder.append(OUTPUT_CLASS_SOLVER_SIGNED).append(solverClass.getName()).append("\n");
        for(List<Integer> route : routes){
            if(route.get(0) != 0)
                route.add(0, 0);
            if(route.get(route.size() - 1) != 0)
                route.add(0);
            List<String> routeStr = route.stream().map(String::valueOf).collect(Collectors.toList());
            strBuilder.append(String.join(" ", routeStr)).append("\n");
        }
        strBuilder.append(solution.score()).append("\n");

        String data = strBuilder.toString();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path), append))){
            writer.write(data);
        }catch (IOException e){
            logger.error("Can not write solution to file\n" + data, e);
        }
    }

    public static void clear(String file){
        try{
            File f = new File(file);
            if (!f.exists())
                return;
            FileWriter w = new FileWriter(f);
            w.write("");
            w.flush();
        }catch (IOException e){
            logger.error("Can not clear file " + file);
        }
    }

    public static void write(Input input, String path){
        List<Passenger> passengers = input.getPassengers();
        List<Commodity> commodities = input.getCommodities();
        List<Taxi> taxies = input.getTaxis();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.join(" ",
                String.valueOf(passengers.size()), String.valueOf(commodities.size()), String.valueOf(taxies.size()))).append("\n");

        for(int i = 0 ; i < passengers.size() ; i ++){
            strBuilder.append(String.join(" ",
                    String.valueOf(passengers.get(i).getGetIn().getX()),
                    String.valueOf(passengers.get(i).getGetIn().getY()),
                    String.valueOf(passengers.get(i).getGetOff().getY()),
                    String.valueOf(passengers.get(i).getGetOff().getY()))).append("\n");
        }

        for(int i = 0 ; i < commodities.size() ; i ++){
            strBuilder.append(String.join(" ",
                    String.valueOf(commodities.get(i).getPickup().getX()),
                    String.valueOf(commodities.get(i).getPickup().getY()),
                    String.valueOf(commodities.get(i).getDeliver().getX()),
                    String.valueOf(commodities.get(i).getDeliver().getY()),
                    String.valueOf(commodities.get(i).getWeight()))).append("\n");
        }

        strBuilder.append(String.join(" ",
                String.valueOf(Taxi.station().getX()),
                String.valueOf(Taxi.station().getY()))).append("\n");
        for(Taxi t : taxies){
            strBuilder.append(t.getCap()).append("\n");
        }

        String data = strBuilder.toString();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)))){
            writer.write(data);
        } catch (IOException e) {
            logger.warn("Fail to save input to file:");
            logger.warn("\n" + data);
            throw new RuntimeException(e);
        }
    }
}
