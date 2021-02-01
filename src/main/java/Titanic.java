/**
 * @author nlo
 * @version 2.0
 * Java v14
 *
 * This exercise examines the passenger manifest data for the 1912 RMS Titanic disaster, in order to predict who will
 * survive based on information such as age, gender, social status, cabin location, etc.
 *
 * The passenger manifest data is contained in three CSV files located in /DATA_FILES. These contain the training and
 * testing data linked by passenger ID.
 *
 * This class is the main driver for the program and handles the setup, configuration and execution of the machine
 * learning model, in this case, an artificial neural network (ANN). This class hands off data parsing, cleaning, and
 * replacement to the class TitanicDataCleanser.java.
 */

import java.math.RoundingMode;
import java.util.Locale;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class Titanic {
    /**
     * This is the main driver for the program. It is responsible for handing of data preparation duties to the
     * TitanicDataCleanser.java class, as well as setup, configuration and running of the Encog Framework machine
     * learning model. This exercise uses binary classifiers (one-hot encoding) for most inputs, using either a 0 or 1.
     * @param  args  string array from the command line arguments
     */
    public static void main(String[] args) {
        TitanicDataCleanser tdc = new TitanicDataCleanser();
        double[][][] prepped_training_data = tdc.prepTrainingData();
        double[][][] prepped_testing_data = tdc.prepTestingData();

        double[][] training_input_array = new double[prepped_training_data[0].length][prepped_training_data[0][0].length];
        double[][] training_ideal_array = new double[prepped_training_data[0].length][1];
        for(int i = 0; i < training_input_array.length; i++) {
            for(int j = 0; j < training_input_array[i].length; j++)
                training_input_array[i][j] = prepped_training_data[0][i][j];
            training_ideal_array[i][0] = prepped_training_data[0][i][0];
        }

        double[][] testing_input_array = new double[prepped_testing_data[1].length][prepped_testing_data[1][0].length];
        double[][] testing_ideal_array = new double[prepped_training_data[0].length][1];

        for(int i = 0; i < testing_input_array.length; i++) {
            for(int j = 0; j < testing_input_array[i].length; j++)
                testing_input_array[i][j] = prepped_testing_data[0][i][j];
            testing_ideal_array[i][0] = prepped_testing_data[1][i][0];
        }

        /* SETUP NEURAL NETORK */
        MLDataSet training_set = new BasicMLDataSet(training_input_array, training_ideal_array);
        final FoldedDataSet folded = new FoldedDataSet(training_set);

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(18));
        network.addLayer(new BasicLayer(5));
        network.addLayer(new BasicLayer(1));
        network.getStructure().finalizeStructure();
        MLTrain train = new ResilientPropagation(network, folded);
        network.reset();

        final CrossValidationKFold trainFolded = new CrossValidationKFold(train, 6);

        int epoch = 1;
        do {
            trainFolded.iteration();
            System.out.println("Epoch: "+epoch+"     Training Error: "+trainFolded.getError());
            epoch++;
        }while(trainFolded.getError() > 0.1);

        /* BEGIN TESTING NETWORK */
        double hit_count = 0;
        double miss_count = 0;
        MLDataSet testing_set = new BasicMLDataSet(testing_input_array, testing_ideal_array);
        for(MLDataPair pair : testing_set) {
            MLData output = network.compute(pair.getInput());
            DecimalFormat formatter = new DecimalFormat("#.############");
            String roundedActual = formatter.format(output.getData(0));

            System.out.print("Ideal Output: "+pair.getIdeal().getData(0) + "     Actual Output: " + roundedActual);
            int spacing = 19 - roundedActual.length();
            System.out.print(" ".repeat(spacing) + "Result: ");
            if((pair.getIdeal().getData()[0] == 0.0) && (output.getData(0) < 0.5)) {
                hit_count++; System.out.println("Hit");
            }
            else if((pair.getIdeal().getData()[0] == 1.0) && (output.getData(0) >= 0.5)) {
                hit_count++; System.out.println("Hit");
            }
            else {
                miss_count++; System.out.println("Miss");
            }
        }

        double miss_hit_total = hit_count + miss_count;
        double percentage_hit = hit_count / miss_hit_total;
        DecimalFormat formatter = new DecimalFormat("#.##");
        String roundedPercentage = formatter.format(percentage_hit * 100);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Hit Count: "+hit_count+"    Miss Count: "+miss_count+"    Total: "+miss_hit_total+"   Percentage: " + roundedPercentage + "%");
    }
}
