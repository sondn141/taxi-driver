package edu.hust.soict.cbls.data;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.datastructure.Pair;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Point;
import edu.hust.soict.cbls.entity.Taxi;
import edu.hust.soict.cbls.algorithm.impl.MySolution;
import edu.hust.soict.cbls.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Reader{

    private static final Logger logger = LoggerFactory.getLogger(Reader.class);

    public static Input read(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(path)))){
            Input input = new Input();
            String[] meta = reader.readLine().split(" ");
            int n = Integer.valueOf(meta[0]);
            int m = Integer.valueOf(meta[1]);
            int k = Integer.valueOf(meta[2]);

            List<Passenger> passengers = new ArrayList<>();
            for(int i = 0 ; i < n ; i ++){
                String[] pCoor = reader.readLine().split(" ");
                passengers.add(new Passenger(
                        new Point(i + 1, Double.valueOf(pCoor[0]), Double.valueOf(pCoor[1])),
                        new Point(i + 1 + n + m, Double.valueOf(pCoor[2]), Double.valueOf(pCoor[3]))
                ));
            }

            List<Commodity> commodities = new ArrayList<>();
            for(int i = 0 ; i < m ; i ++){
                String[] cCoor = reader.readLine().split(" ");
                commodities.add(new Commodity(
                        new Point(i + n + 1, Double.valueOf(cCoor[0]), Double.valueOf(cCoor[1])),
                        new Point(i + 2*n + m + 1, Double.valueOf(cCoor[2]), Double.valueOf(cCoor[3])),
                        Double.valueOf(cCoor[4])
                ));
            }

            String[] station = reader.readLine().split(" ");
            Taxi.setStation(new Point(0, Double.valueOf(station[0]), Double.valueOf(station[1])));
            List<Taxi> taxies = new ArrayList<>();
            for(int i = 0 ; i < k ; i ++){
                double cap = Double.valueOf(reader.readLine());
                taxies.add(new Taxi(cap));
            }

            // Init input data
            input.setPassengers(passengers);
            input.setCommodities(commodities);
            input.setTaxies(taxies);

            return input;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Solution> readSolution(String resultFile){
        Map<String, Solution> mapSolution = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(resultFile)))){
            String line;
            String solverClazz = null;
            List<List<Integer>> sol = new LinkedList<>();
            while(!StringUtils.isNullOrEmpty(line = reader.readLine())){
                if(line.startsWith(Writer.OUTPUT_CLASS_SOLVER_SIGNED)){
                    sol.clear();
                    solverClazz = line.substring(Writer.OUTPUT_CLASS_SOLVER_SIGNED.length());
                } else{
                    String[] lineArr = line.split(" ");
                    if(lineArr.length > 1){
                        List<Integer> route = Arrays.stream(lineArr).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
                        sol.add(route);
                    } else{
                        MySolution s = new MySolution();
                        s.setSolution(sol);
                        s.setScore(Double.valueOf(line));
                        mapSolution.put(solverClazz, s);
                    }

                }
            }
        } catch (IOException e) {
            logger.error("Error while reading solution(s)", e);
        }

        return mapSolution;
    }

    public static Map<String, Map<String, Solution>> readMultiInputsAndSolutions(List<Pair<String, String>> inpSolPairs){
        Map<String, Map<String, Solution>> res = new HashMap<>();
        for(Pair<String, String> inpSol : inpSolPairs){
            String inputFile = inpSol.getK();
            String solFile = inpSol.getV();
            Map<String, Solution> mapSol = readSolution(solFile);
            res.put(inputFile, mapSol);
        }

        return res;
    }
}
