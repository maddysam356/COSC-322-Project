package ubc.cosc322;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

//this is not in use at the moment
public class NN {

    private MultiLayerNetwork model;

    public NN() {
        //https://deeplearning4j.konduit.ai/deeplearning4j/tutorials/quick-start
        //XAVIER => weight = U [-(1/sqrt(n)), 1/sqrt(n)]
        //https://machinelearningmastery.com/weight-initialization-for-deep-learning-neural-networks/#:~:text=each%20in%20turn.-,Xavier%20Weight%20Initialization,of%20inputs%20to%20the%20node.&text=We%20can%20implement%20this%20directly%20in%20Python
        //create neural network
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder() 
            .seed(19000413)  // seed
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT) //using gradient descent for simplicity
            .updater(new Adam(0.01))  // learning rate 0.01
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(6)   // 6 input features
                .nOut(16) // 1st hidden layer
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .build())
            .layer(1, new DenseLayer.Builder()
                .nIn(16)  // 1st hidden layer outputs
                .nOut(8)  // 2nd hidden layer
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .build())
            .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .nIn(8)   // 2nd hidden layer outputs
                .nOut(4)  // 4 heuristic weights
                .activation(Activation.IDENTITY) // Linear activation
                .weightInit(WeightInit.XAVIER)
                .build())
            .build();

        model = new MultiLayerNetwork(config);
        model.init();
    }

    public double[] predictHeuristicWeights(double[] inputFeatures) {
        //2d row vector
        INDArray input = Nd4j.create(inputFeatures).reshape(1, inputFeatures.length);
        // pass through network
        INDArray output = model.output(input);
    
        // convert back to 1d
        return output.toDoubleVector();
    }
    
    //used for testing
    public void train(double[][] inputs, double[][] targets) {
        INDArray inputArray = Nd4j.create(inputs);
        INDArray targetArray = Nd4j.create(targets);
        model.fit(inputArray, targetArray);
    }

    // save a NN model that is used
    public void saveModel(String filePath) throws Exception {
        model.save(new java.io.File(filePath));
    }

    //load a previous NN model
    public void loadModel(String filePath) throws Exception {
        model = MultiLayerNetwork.load(new java.io.File(filePath), true);
    }
}

