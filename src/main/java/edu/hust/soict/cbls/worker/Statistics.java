package edu.hust.soict.cbls.worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static java.util.stream.Collectors.joining;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;
import edu.hust.soict.cbls.common.utils.StringUtils;
import edu.hust.soict.cbls.data.Writer;

public class Statistics {
    private Properties props;
    private File output;

    private List<Solver> solvers;

    private static final Logger logger = LoggerFactory.getLogger(RecursiveWorker.class);

    public Statistics(Properties props) throws IOException {
        this.props = props;
        this.output = new File(props.getProperty(Const.OUTPUT));
        
        if(StringUtils.validPath(output.getAbsolutePath()))
            throw new RuntimeException("Invalid output path");
    }
    
    private class Stats {
    	private ArrayList<Integer> time;
    	private ArrayList<Double> res;
    	
    	public Stats() {
    		time = new ArrayList<Integer>();
    		res = new ArrayList<Double>();
    	}
    	
    	public void add(int t, double r) {
    		time.add(t);
    		res.add(r);
    	}
    	
    	public double avg() {
    		return res.stream().mapToDouble(val -> val).average().orElse(0.0);
    	}
    	
    	public double min() {
    		return res.stream().mapToDouble(val -> val).min().orElse(0.0);
    	}
    	
    	public double max() {
    		return res.stream().mapToDouble(val -> val).max().orElse(0.0);
    	}
    	
    	public double std() {
    		if (res.size() <= 1)
    			return 0.0;
    		double sum = 0.0, avg = avg();
    		for (Double r: res)
    			sum += Math.pow(r - avg, 2);
    		return Math.sqrt(sum / (res.size() - 1));
    	}
    	
    	public double time() {
    		return time.stream().mapToDouble(val -> val).average().orElse(0.0);
    	}
    }

    private void processFile(File file, String stFolder) throws InterruptedException, IOException {
    	HashMap<String, Stats> entries = new HashMap<>();
    	Scanner scanner = new Scanner(file);
    	while (scanner.hasNextLine()) {
    	   String line = scanner.nextLine();
    	   if (line.equals(""))
    		   continue;
    	   String[] parts = line.split(" ");
    	   String[] keyParts = parts[1].split("\\.");
    	   String key = keyParts[keyParts.length-1];
    	   int t = Integer.parseInt(parts[3]);
    	   
    	   line = scanner.nextLine();
    	   while (line.split(" ")[0].equals("0"))
    		   line = scanner.nextLine();
    	   double r = Double.parseDouble(line.strip());
    	   
    	   if (!entries.containsKey(key))
    		   entries.put(key, new Stats());
    	   entries.get(key).add(t, r);
    	}
    	
    	for (HashMap.Entry<String, Stats> entry: entries.entrySet()) {
    		Stats st = entry.getValue();
        	List<Double> infos =  Arrays.asList(st.min(), st.max(), st.avg(), st.std(), st.time());
        	String s = infos.stream().map(inf -> String.format("%.2f", inf)).collect(joining(","));
        	s = file.getName() + "," + s + "\n";
        	FileWriter fw = new FileWriter(new File(stFolder + "/" + entry.getKey() + ".csv"), true);
        	fw.write(s);
        	fw.close();
    	}
    	
    }

    public void work() throws InterruptedException, IOException {
        for (File f: output.listFiles())
        	processFile(f, "stats");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();
        Statistics stats = new Statistics(props);

        stats.work();
    }
}
