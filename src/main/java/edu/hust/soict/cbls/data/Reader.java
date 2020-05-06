package edu.hust.soict.cbls.data;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.datastructure.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Reader implements AutoCloseable{

    public static Input read(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(path)))){
            Input input = new Input();
            String[] meta = reader.readLine().split(" ");
            int n = Integer.valueOf(meta[0]);
            int m = Integer.valueOf(meta[1]);
            int k = Integer.valueOf(meta[2]);

            List<Passenger> passengers = new ArrayList<>();
            List<GetOff> getOffs = new ArrayList<>();
            for(int i = 0 ; i < n ; i ++){
                String[] pCoor = reader.readLine().split(" ");
                passengers.add(new Passenger(Double.valueOf(pCoor[0]), Double.valueOf(pCoor[1])));
                getOffs.add(new GetOff(Double.valueOf(pCoor[2]), Double.valueOf(pCoor[3])));
            }

            List<Commodity> commodities = new ArrayList<>();
            List<Deliver> delivers = new ArrayList<>();
            for(int i = 0 ; i < m ; i ++){
                String[] cCoor = reader.readLine().split(" ");
                commodities.add(new Commodity(Double.valueOf(cCoor[0]), Double.valueOf(cCoor[1]), Double.valueOf(cCoor[4])));
                delivers.add(new Deliver(Double.valueOf(cCoor[2]), Double.valueOf(cCoor[3])));
            }

            List<Taxi> taxies = new ArrayList<>();
            for(int i = 0 ; i < k ; i ++){
                String[] tData = reader.readLine().split(" ");
                taxies.add(new Taxi(Double.valueOf(tData[0]), Double.valueOf(tData[1]), Double.valueOf(tData[2])));
            }

            // Init input data
            input.setPassengers(passengers);
            input.setGetOff(getOffs);
            input.setCommodities(commodities);
            input.setDeliver(delivers);
            input.setTaxis(taxies);
            input.createDistanceMatrix();

            return input;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
