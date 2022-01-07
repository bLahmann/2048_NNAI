import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {

    private List<Double[][]> weights     = new ArrayList<>();
    private List<Double[]> biases        = new ArrayList<>();

    public NeuralNetwork() {
    }

    public NeuralNetwork(int inputLayerSize, int outputLayerSize, int ... hiddenLayerSizes) {

        List<Integer> nodeLayerSizes = new ArrayList<>();
        nodeLayerSizes.add(inputLayerSize);
        for (int i = 0; i < hiddenLayerSizes.length; i++){
            weights.add(new Double[hiddenLayerSizes[i]][nodeLayerSizes.get(i)]);
            biases.add(new Double[hiddenLayerSizes[i]]);
            nodeLayerSizes.add(hiddenLayerSizes[i]);
        }
        weights.add(new Double[outputLayerSize][nodeLayerSizes.get(nodeLayerSizes.size()-1)]);
        biases.add(new Double[outputLayerSize]);
        randomizeWeights();

    }

    public Double[] propagate(Double[] inputNodes){

        Double[] nodes = inputNodes;
        for (int i = 0; i < weights.size(); i++) {

            Double[][] weightMatrix = weights.get(i);
            Double[] biasArray = biases.get(i);
            Double[] newNodes = new Double[weightMatrix.length];
            for (int j = 0; j < newNodes.length; j++){
                newNodes[j] = 0.0;
                for (int k = 0; k < weightMatrix[j].length; k++){
                    newNodes[j] += nodes[k] * weightMatrix[j][k];
                }
                newNodes[j] += biasArray[j];
                newNodes[j] = 1.0 / (1.0 + Math.exp(-newNodes[j]));     // Sigmoid
            }
            nodes = newNodes;
        }

        return nodes;
    }

    private void randomizeWeights(){

        Random random = new Random();
        for (Double[][] matrix : weights){
            for (int i = 0; i < matrix.length; i++){
                for (int j = 0; j < matrix[i].length; j++){
                    matrix[i][j] = random.nextGaussian();
                }
            }
        }

        for (Double[] array : biases){
            for (int i = 0; i < array.length; i++){
                array[i] = random.nextGaussian();
            }
        }
    }

    public List<Double[][]> getWeights() {
        return weights;
    }

    public List<Double[]> getBiases() {
        return biases;
    }

    public void setWeights(List<Double[][]> weights) {
        this.weights = weights;
    }

    public void setBiases(List<Double[]> biases) {
        this.biases = biases;
    }
}
