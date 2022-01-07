import java.util.Arrays;
import java.util.Random;

public class Program {

    private static final int POPULATION_SIZE = 10;
    private static final int GAMES_PER_AI = 128;
    private static final double CULLING_FRACTION = 0.8;
    private static final double MUTATE_CHANCE = 1.0;
    private static final double MUTATE_FRACTION = 0.05;

    private static final int[] HIDDEN_NODE_SIZES = new int[] {32, 32};

    public static void main(String ... args) throws Exception{

        Random random = new Random();

        // Generate the first batch of AIs
        AI[] ais = new AI[POPULATION_SIZE];
        Double[] scores = new Double[ais.length];
        for (int i = 0; i < ais.length; i++){
            NeuralNetwork nn = new NeuralNetwork(16, 4, HIDDEN_NODE_SIZES);
            ais[i] = new AI(nn);
        }


        int iteration = 0;
        while (true) {

            // Init the scores
            for (int i = 0; i < ais.length; i++){
                scores[i] = 0.0;
            }

            // Play the game
            SimulationThread[] threads = new SimulationThread[ais.length];
            for (int i = 0; i < ais.length; i++) {
                threads[i] = new SimulationThread(ais[i], GAMES_PER_AI);
                threads[i].start();
            }

            for (int i = 0; i < ais.length; i++){
                threads[i].join();
                scores[i] = threads[i].getScore();
            }


            // Sort the results
            ArrayIndexComparator comparator = new ArrayIndexComparator(scores);
            Integer[] indexes = comparator.createIndexArray();
            Arrays.sort(indexes, comparator);
            System.out.println(iteration + " " + Utils.mean(scores) + " " + scores[indexes[0]]);
            iteration++;

            // Breed the next generation
            int cutOffIndex = (int) Math.round(CULLING_FRACTION * POPULATION_SIZE);
            int counter = 0;
            for (int i = cutOffIndex; i < POPULATION_SIZE; i++) {
                int partnerIndex = random.nextInt(cutOffIndex - 1);
                ais[indexes[i]] = ais[indexes[counter]].breed(ais[indexes[partnerIndex]]);
                if (random.nextDouble() < MUTATE_CHANCE){
                    ais[indexes[i]].mutate(MUTATE_FRACTION);
                }
                counter = Math.floorMod(counter + 1, cutOffIndex);
            }

        }




    }

}
