package edu.hust.soict.cbls.common.ea.ga.components;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.ea.ga.operator.Crossover;
import edu.hust.soict.cbls.common.ea.ga.operator.Mutation;
import edu.hust.soict.cbls.common.ea.ga.operator.NaturalSelection;
import edu.hust.soict.cbls.common.ea.ga.operator.SelectParents;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;

import java.security.InvalidParameterException;

import java.util.*;
import java.util.stream.Collectors;

public class Population<I extends Solution> {

    private static final Random rd = new Random(Const.SEED);

    private Properties props;
    private List<I> individuals;

    public Population(Properties props, Class<I> clazz) {
        this.props = props;
        this.individuals = new ArrayList<>();

        int popSize = props.getIntProperty(Const.GA_POPULATION_SIZE, 200);
        for(int i = 0 ; i < popSize ; i ++){
            individuals.add(Solution.getIndividual(props, clazz));
        }
    }

    public <C extends Crossover<I>, P extends SelectParents<I>> List<I> crossover(){
        List<C> operators = Crossover.getCrossover(props);
        P selectParent = SelectParents.getSelectParent(props);

        List<List<I>> parents = selectParent.execute(getPopulation());
        List<I> childs = new LinkedList<>();
        List<Double> probs = props.getCollection(Const.GA_CROSSOVER_PROBABILITY)
                .stream().mapToDouble(Double::valueOf).boxed().collect(Collectors.toList());

        if(probs.size() != operators.size())
            throw new InvalidParameterException("Number of crossover operators does not match the number of entry probabilities");

        IND:
        for (List<I> parent : parents) {
            if (rd.nextDouble() < probs.get(0)) {
                for (int j = 0; j < operators.size() - 1; j++) {
                    if (probs.size() == 1 || rd.nextDouble() < probs.get(j + 1)) {
                        childs.addAll(operators.get(j).execute(parent));
                        continue IND;
                    }
                }

                childs.addAll(operators.get(operators.size() - 1).execute(parent));
            }
        }

        return childs;
    }

    public <M extends Mutation<I>> List<I> mutation(List<I> childs){
        List<M> operators = Mutation.getMutation(props);

        List<Double> probs = props
                .getCollection(Const.GA_MUTATION_PROBABILITY)
                .stream().mapToDouble(Double::valueOf).boxed().collect(Collectors.toList());

        if(probs.size() != operators.size())
            throw new InvalidParameterException("Number of mutation operators does not match the number of entry probabilities");

        CHILD:
        for(int i = 0 ; i < childs.size() ; i ++){
            if(rd.nextDouble() < probs.get(0)){
                for(int j = 0 ; j < operators.size() - 1 ; j ++){
                    if(probs.size() == 1 || rd.nextDouble() < probs.get(j + 1)){
                        childs.set(i, operators.get(j).execute(childs.get(i)));
                        continue CHILD;
                    }
                }

                childs.set(i, operators.get(operators.size() - 1).execute(childs.get(i)));
            }
        }

        return childs;
    }

    public <N extends NaturalSelection<I>> void naturalSelection(){
        N operator = NaturalSelection.getNaturalSelection(props);
        individuals = operator.execute(individuals);
    }

    public List<I> getPopulation(){
        return individuals;
    }

    public void addAll(List<I> childs){
        this.individuals.addAll(childs);
    }

    public void sort(){
        Collections.sort(individuals);
    }

    public int size(){
        return individuals.size();
    }

    public I get(int index){
        return individuals.get(index);
    }

    public void set(int index, I ind){
        individuals.set(index, ind);
    }
}
