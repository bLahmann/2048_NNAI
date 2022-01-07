import java.util.Arrays;
import java.util.Random;

public class Program {

    private static final int POPULATION_SIZE = 100;
    private static final double CULLING_FRACTION = 0.5;
    private static final double MUTATE_CHANCE = 0.1;
    private static final double MUTATE_FRACTION = 0.2;

    private static final int[] HIDDEN_NODE_SIZES = new int[] {32, 32, 32};

    public static void main(String ... args){

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

            // Play the game
            for (int i = 0; i < ais.length; i++) {
                Board board = new Board();
                while (ais[i].doMove(board)) {
                }
                scores[i] = (double) board.getMaxTile();
            }

            // Sort the results
            ArrayIndexComparator comparator = new ArrayIndexComparator(scores);
            Integer[] indexes = comparator.createIndexArray();
            Arrays.sort(indexes, comparator);
            System.out.println(iteration + " " + Utils.mean(scores));
            iteration++;

            // Breed the next generation
            int cutOffIndex = (int) Math.round(CULLING_FRACTION * POPULATION_SIZE);
            int counter = 0;
            for (int i = cutOffIndex; i < POPULATION_SIZE; i++) {
                int partnerIndex = random.nextInt(cutOffIndex - 1);
                ais[i] = ais[indexes[counter]].breed(ais[indexes[partnerIndex]]);
                if (random.nextDouble() < MUTATE_CHANCE){
                    ais[i].mutate(MUTATE_FRACTION);
                }
                counter = Math.floorMod(counter + 1, cutOffIndex);
            }

        }




    }

}
