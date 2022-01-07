import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AI {

    private NeuralNetwork nn;

    public AI(NeuralNetwork nn) {
        this.nn = nn;
    }

    public boolean doMove(Board board){

        int[][] tiles = board.getTiles();
        Double[] inputNodes = new Double[tiles.length * tiles.length];

        int counter = 0;
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles.length; j++){
                //inputNodes[counter] = Math.log(tiles[i][j]) / Math.log(2);
                inputNodes[counter] = (double) tiles[i][j];
                counter++;
            }
        }

        Double[] outputNodes = nn.propagate(inputNodes);
        ArrayIndexComparator comparator = new ArrayIndexComparator(outputNodes);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        for (int index : indexes){
            switch (index){
                case 0:
                    if (board.shiftUp())    return true;
                case 1:
                    if (board.shiftDown())  return true;
                case 2:
                    if (board.shiftLeft())  return true;
                case 3:
                    if (board.shiftRight()) return true;
            }
        }

        return false;

    }

    public AI breed(AI partner){
        return breed(partner, 0.5);
    }

    public AI breed(AI partner, double fraction){

        List<Double[][]> myWeights = this.getNeuralNetwork().getWeights();
        List<Double[]> myBiases = this.getNeuralNetwork().getBiases();

        List<Double[][]> partnerWeights = partner.getNeuralNetwork().getWeights();
        List<Double[]> partnerBiases = partner.getNeuralNetwork().getBiases();

        List<Double[][]> childWeights = new ArrayList<>();
        List<Double[]> childBiases = new ArrayList<>();


        for (int i = 0; i < myWeights.size(); i++){

            childWeights.add(new Double[myWeights.get(i).length][myWeights.get(i)[0].length]);
            childBiases.add(new Double[myBiases.get(i).length]);

            for (int j = 0; j < myWeights.get(i).length; j++){

                if (Math.random() < fraction){
                    childBiases.get(i)[j] = myBiases.get(i)[j];
                }else {
                    childBiases.get(i)[j] = partnerBiases.get(i)[j];
                }

                for (int k = 0; k < myWeights.get(i)[j].length; k++){
                    if (Math.random() < fraction) {
                        childWeights.get(i)[j][k] = myWeights.get(i)[j][k];
                    }else {
                        childWeights.get(i)[j][k] = partnerWeights.get(i)[j][k];
                    }
                }

            }
        }

        NeuralNetwork childNN = new NeuralNetwork();
        childNN.setWeights(childWeights);
        childNN.setBiases(childBiases);
        return new AI(childNN);


    }

    public void mutate(double fraction){

        Random random = new Random();
        List<Double[][]> myWeights = this.getNeuralNetwork().getWeights();
        List<Double[]> myBiases = this.getNeuralNetwork().getBiases();

        for (int i = 0; i < myWeights.size(); i++){
            for (int j = 0; j < myWeights.get(i).length; j++){

                if (Math.random() < fraction){
                    myBiases.get(i)[j] = random.nextGaussian();
                }

                for (int k = 0; k < myWeights.get(i)[j].length; k++){
                    if (Math.random() < fraction) {
                        myWeights.get(i)[j][k] = random.nextGaussian();
                    }
                }
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return breed(this);
    }

    public NeuralNetwork getNeuralNetwork() {
        return nn;
    }
}
