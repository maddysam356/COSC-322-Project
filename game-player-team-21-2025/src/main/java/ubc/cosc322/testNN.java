// package ubc.cosc322;
// //basic test to see if the neural network works as expected
// public class testNN {
//     public static void main(String[] args) {
//         NN nn = new NN();
//         double[][] trainInputs = {
//             {10, 5, 2, 8, 3, 1}, 
//             {5, 2, 1, 7, 4, 3},
//             {9, 6, 3, 5, 2, 1}
//         };

//         double[][] expectedOutputs = {
//             {0.5, 0.2, 0.1, 0.8}, 
//             {0.3, 0.1, 0.2, 0.6},
//             {0.4, 0.25, 0.15, 0.7}
//         };

//         // before training
//         System.out.println("Before training:");
//         for (double[] input : trainInputs) {
//             double[] output = nn.predictHeuristicWeights(input);
//             System.out.println("Prediction: " + java.util.Arrays.toString(output));
//         }

//         //train
//         nn.train(trainInputs, expectedOutputs);

//         // after
//         System.out.println("\nAfter training:");
//         for (double[] input : trainInputs) {
//             double[] output = nn.predictHeuristicWeights(input);
//             System.out.println("Prediction: " + java.util.Arrays.toString(output));
//         }
//     }
// }
