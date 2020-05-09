package edu.hust.soict.cbls.algorithm.backtrack;

import edu.hust.soict.cbls.common.config.Properties;

public class Test {
	public static void main(String[] args) {
		Properties props = new Properties();
		BackTracking test = new BackTracking(props);
		test.run();
	}
}
