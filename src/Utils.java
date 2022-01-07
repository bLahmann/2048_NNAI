public class Utils {

    public static double mean(Double[] array){

        double sum = 0.0;
        for (double value : array){
            sum += value;
        }
        return sum / array.length;

    }
}
