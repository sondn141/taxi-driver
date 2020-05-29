package edu.hust.soict.cbls.common.config;

import java.awt.Color;

public class Const {

    // RANDOM
    public static final int SEED = 5;

    // I/O
    public static final String INPUT_OBJECT = "INPUT_OBJECT";
    public static final String INPUT = "INPUT";
    public static final String OUTPUT = "OUTPUT";

    // SOLVER
    public static final String SOLVER_CLASS = "SOLVER_CLASS";

    // WORKER
    public static final String WORKER_THREAD_POOL_SIZE = "WORKER_THREAD_POOL_SIZE";

    //
    //                              ALGORITHMS
    //
    // 1. GA
    public static final String GA_GENERATION = "GA_GENERATION";
    public static final String GA_POPULATION_SIZE = "GA_POPULATION_SIZE";

    // Fitness function

    // Crossover operator
    public static final String GA_CROSSOVER_PROBABILITY = "GA_CROSSOVER_PROBABILITY";
    public static final String GA_CROSSOVER_OPERATOR_CLASS = "GA_CROSSOVER_OPERATOR_CLASS";

    // Mutation operator
    public static final String GA_MUTATION_PROBABILITY = "GA_MUTATION_PROBABILITY";
    public static final String GA_MUTATION_OPERATOR_CLASS = "GA_MUTATION_OPERATOR_CLASS";

    // Select parents operator
    public static final String GA_SELECT_PARENT_OPERATOR_CLASS = "GA_SELECT_PARENT_OPERATOR_CLASS";
    public static final String GA_SELECT_PARENT_NUM_OF_PAIRS = "GA_SELECT_PARENT_NUM_OF_PAIRS";
    public static final String GA_SELECT_PARENT_TOURNAMENT_SIZE = "GA_SELECT_PARENT_TOURNAMENT_SIZE";

    // Natural selection operator
    public static final String GA_NATURAL_SELECTION_OPERATOR_CLASS = "GA_NATURAL_SELECTION_OPERATOR_CLASS";
    
    // 2. LOCAL SEARCH
    public static final String LS_MAX_STABLE = "LS_MAX_STABLE";
    public static final String LS_MAX_ITER = "LS_MAX_ITER";
    public static final String LS_TIME_LIMIT = "LS_TIME_LIMIT";
    
    // 3. MIP
    public static final String MIP_BIG_M = "MIP_BIG_M";
    public static final String MIP_EPSILON = "MIP_EPSILON";

    //
    //                              VISUALIZATION
    //
    // 1. SINGLE INPUT, MULTI SOLVERS

    // 2. MULTI INPUT, MULTI SOLVERS

    // 3. MULTI INPUT, SINGLE SOLVER

    // 5. COMMON
    public static final String VISUALIZER_CLASS = "VISUALIZER_CLASS";
    public static final String VISUALIZATION_CHART_TITLE = "VISUALIZATION_CHART_TITLE";
    public static final String VISUALIZATION_INPUT_FILE = "VISUALIZATION_INPUT_FILE";
    public static final String VISUALIZATION_RESULT_FILE = "VISUALIZATION_RESULT_FILE";
    public static final String VISUALIZATION_IMAGE_FOLDER = "VISUALIZATION_IMAGE_FOLDER";
    public static final String VISUALIZATION_IMAGE_FILE_NAME = "VISUALIZATION_IMAGE_FILE_NAME";
    public static final String VISUALIZATION_FIGURE_WIDTH = "VISUALIZATION_FIGURE_WIDTH";
    public static final String VISUALIZATION_FIGURE_HEIGHT = "VISUALIZATION_FIGURE_HEIGHT";

    public static final Color[] COLORS = new Color[]{
            new Color(0, 0, 0),
            new Color(255,0,0),
            new Color(0, 255, 0),
            new Color(0, 0, 255),
            new Color(255, 255, 0),
            new Color(0, 255, 255),
            new Color(255, 0, 255),
            new Color(192, 192, 192),
            new Color(128, 128, 128),
            new Color(128, 0, 0),
            new Color(128, 128, 0),
            new Color(0, 128, 0),
            new Color(128, 0, 128),
            new Color(0, 128, 128),
            new Color(0, 0, 128),
            new Color(255, 255, 255),
    };

}
