package edu.hust.soict.cbls.data;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.datastructure.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Reader{

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
                        new Point(Double.valueOf(pCoor[0]), Double.valueOf(pCoor[1])),
                        new Point(Double.valueOf(pCoor[2]), Double.valueOf(pCoor[3]))
                ));
            }

            List<Commodity> commodities = new ArrayList<>();
            for(int i = 0 ; i < m ; i ++){
                String[] cCoor = reader.readLine().split(" ");
                commodities.add(new Commodity(
                        new Point(Double.valueOf(cCoor[0]), Double.valueOf(cCoor[1])),
                        new Point(Double.valueOf(cCoor[2]), Double.valueOf(cCoor[3])),
                        Double.valueOf(cCoor[4])
                ));
            }

            String[] station = reader.readLine().split(" ");
            Taxi.setStation(new Point(Double.valueOf(station[0]), Double.valueOf(station[1])));
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
}
