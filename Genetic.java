import java.security.SecureRandom;
import java.util.*;

public class Genetic{
    private ArrayList<Sudoku> population;
    private ArrayList<Integer> fitnessBounds;
    private final int populationSize = 50;
    private final int generations = 10000;

    private final int[][] fixed_board;

    public Genetic(int[][] fixed_board){
        population = new ArrayList<Sudoku>();
        this.fixed_board = fixed_board;
    }

    public Sudoku geneticAlgorithm(){
        
        initialisePopulation();
        Collections.sort(population);
        updateFitnessBounds();
        ArrayList<Sudoku> newPopulation;
        SecureRandom random = new SecureRandom();

        int s1, s2, index1, index2;
        int x = 0;
        while(!population.get(0).isTerminal() && x < generations){
            newPopulation = new ArrayList<Sudoku>();
            newPopulation.add(population.get(0));
            for(int i = 1; i < population.size(); ++i){
                s1 = random.nextInt(fitnessBounds.size());
                s2 = random.nextInt(fitnessBounds.size());
                //while(s1 == s2) s2 = random.nextInt(fitnessBounds.size());
                index1 = fitnessBounds.get(s1);
                index2 = fitnessBounds.get(s2);
                newPopulation.add(crossover(population.get(index1), population.get(index2)));
                // newPopulation.add(crossover(population.get(0), population.get(index1)));
            }
            population = newPopulation;
            Collections.sort(population);
            updateFitnessBounds();
            ++x;
        }
        
        return population.get(0);
    }

    public void initialisePopulation(){
        Sudoku first = new Sudoku(fixed_board);
        population.add(first);
        for(int i = 1; i < populationSize; ++i){
            population.add(new Sudoku(first));
        }
        updateFitnessBounds();
    }

    public Sudoku crossover(Sudoku a, Sudoku b){
        Sudoku child = new Sudoku(a, b);
        return child.mutate();
    }

    private void updateFitnessBounds(){
        double sum = sumFitness(population);
        int fitness;
        this.fitnessBounds = new ArrayList<Integer>();
        for(int i = 0; i < population.size(); ++i){
            fitness = population.get(i).getFitness();
            for(int j = 0; j < fitness; ++j){
                this.fitnessBounds.add(i);
            }
        }
    }

    private double sumFitness(ArrayList<Sudoku> list){
        double sum = 0;
        for(Sudoku s : list){
            sum += s.getFitness();
        }
        return sum;
    }
}