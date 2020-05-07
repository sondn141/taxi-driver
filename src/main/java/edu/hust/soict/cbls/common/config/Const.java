package edu.hust.soict.cbls.common.config;

public class Const {

    // RANDOM
    public static final int SEED = 5;

    // I/O
    public static final String INPUT_FILE_PATH = "INPUT_FILE_PATH";
    public static final String OUTPUT_FILE_PATH = "OUTPUT_FILE_PATH";

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

    // Natural selection operator
    public static final String GA_NATURAL_SELECTION_OPERATOR_CLASS = "GA_NATURAL_SELECTION_OPERATOR_CLASS";

}
