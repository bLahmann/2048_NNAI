public class SimulationThread extends Thread {

    private AI ai;
    private int numRuns;
    private double score;

    public SimulationThread(AI ai, int numRuns){
        this.ai = ai;
        this.numRuns = numRuns;
    }

    @Override
    public void run() {
        for (int i = 0; i < numRuns; i++){
            Board board = new Board();
            while (ai.doMove(board));
            score += board.getScore();
        }
        score /= numRuns;
    }

    public double getScore() {
        return score;
    }
}
