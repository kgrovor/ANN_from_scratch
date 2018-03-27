import java.io.IOException;
import java.util.*;

public class Backprop {

    double[][] input;
    int[][] target;
    double[][] inputValidation;
    int[][] targetValidation;
    double alpha = 0.05;
    double momentum = 0.9;
    NN network;

    public Backprop() {
        network = new NN(1, "sigmoid", "linear");   //Adding nodes
        double[] unused = {1.2, 1.1};
        int i;
        for (i = 0; i < 64; i++) {
            network.getLayers().get(0).addNode(new Node("linear"), unused);
        }
        for (i = 0; i < 11; i++) {
            network.getLayers().get(1).addNode(new Node("sigmoid"), generateRandom(network.getLayers().get(0).getNodes().size()));
        }
        for (i = 0; i < 10; i++) {

            network.getLayers().get(2).addNode(new Node("sigmoid"), generateRandom(network.getLayers().get(1).getNodes().size()));
        }

    }

    double[] generateRandom(int dim) {
        double[] arr = new double[dim];
        int i;
        //Random gen = new Random(55);
        for (i = 0; i < dim; i++) {
            arr[i] = (Math.random() - 0.5) * 2;
            //System.out.println(arr[i]);
            //arr[i] = 0.2;

        }

        return arr;
    }


    public static void main(String arg[]) throws IOException {

        Backprop handle = new Backprop();
        handle.parseInput();
        handle.parseValid();
        //System.out.println("Before starting average error is " + handle.avgErrorValidation());

        handle.train(3000);

        handle.test();


    }

    public void test() throws IOException {
        int correctClassifications = 0;
        String inp = MyUtil.readTextFile("test.txt");
        StringTokenizer st = new StringTokenizer(inp, "\n");
        double[][] vals = new double[st.countTokens()][64];
        int[] intTarget = new int[st.countTokens()];
        int len = st.countTokens();
        int i = 0;
        int j, k;
        while (st.hasMoreTokens()) {
            String[] sp = st.nextToken().split(",");
            for (j = 0; j < sp.length - 1; j++) {
                vals[i][j] = Double.parseDouble(sp[j]);
            }
            intTarget[i] = Integer.parseInt(sp[sp.length - 1].trim());
            i++;
        }

        for(i = 0; i<vals.length; i++){
            network.setInputs(vals[i]);
            for (j = 1; j < network.getLayers().size(); j++) {
                network.fwdPropogateTo(network.getLayers().get(j));
            }
            double[] outputs = new double[10];
            Layer outputLayer = network.getLayers().get(network.getLayers().size() - 1);
            for(j=0; j<outputs.length; j++){
                outputs[j] = outputLayer.getNodes().get(j).getOutput();
            }
            double max = -9999;
            int maxindex = -1;
            for(j=0; j<outputs.length; j++){
                if(max < outputs[j]){
                    max = outputs[j];
                    maxindex = j;
                }
            }

            if(maxindex == intTarget[i]){
                correctClassifications++;
            }

        }
        System.out.println("Num of correct classifications " + correctClassifications + " out of " + intTarget.length);
        System.out.println("The accuracy is " + ((double)correctClassifications/intTarget.length)*100 + "%");

    }

    public double avgErrorValidation() {
        int i, j, k;
        double totalError = 0;
        Layer outputLayer = network.getLayers().get(network.getLayers().size() - 1);
        for (i = 0; i < inputValidation.length; i++) {
            network.setInputs(inputValidation[i]);
            for (j = 1; j < network.getLayers().size(); j++) {
                network.fwdPropogateTo(network.getLayers().get(j));
            }

            for (j = 0; j < targetValidation[i].length; j++) {
                Node currNode = outputLayer.getNodes().get(j);
                totalError += Math.pow(currNode.getOutput() - targetValidation[i][j],2);
            }
        }
        return totalError / inputValidation.length;
    }

    public void parseValid() throws IOException {
        String inp = MyUtil.readTextFile("validation.txt");
        StringTokenizer st = new StringTokenizer(inp, "\n");
        double[][] vals = new double[st.countTokens()][64];
        int[] intTarget = new int[st.countTokens()];
        int len = st.countTokens();
        int i = 0;
        int j, k;
        while (st.hasMoreTokens()) {
            String[] sp = st.nextToken().split(",");
            for (j = 0; j < sp.length - 1; j++) {
                vals[i][j] = Double.parseDouble(sp[j]);
            }
            intTarget[i] = Integer.parseInt(sp[sp.length - 1].trim());
            i++;
        }
        int[][] tar = new int[len][10];
        for (i = 0; i < len; i++) {
            for (j = 0; j < 10; j++) {
                tar[i][j] = 0;
            }
            tar[i][intTarget[i]] = 1;
        }
        this.inputValidation = vals;
        this.targetValidation = tar;

    }


    public void parseInput() throws IOException {
        String inp = MyUtil.readTextFile("train.txt");
        StringTokenizer st = new StringTokenizer(inp, "\n");
        double[][] vals = new double[st.countTokens()][64];
        int[] intTarget = new int[st.countTokens()];
        int len = st.countTokens();
        int i = 0;
        int j, k;
        while (st.hasMoreTokens()) {
            String[] sp = st.nextToken().split(",");
            for (j = 0; j < sp.length - 1; j++) {
                vals[i][j] = Double.parseDouble(sp[j]);
            }
            intTarget[i] = Integer.parseInt(sp[sp.length - 1].trim());
            i++;
        }
        int[][] tar = new int[len][10];
        for (i = 0; i < len; i++) {
            for (j = 0; j < 10; j++) {
                tar[i][j] = 0;
            }
            tar[i][intTarget[i]] = 1;
        }
        this.input = vals;
        this.target = tar;

    }


    public double giveManualInput(double[] inp) {
        network.setInputs(inp);
        int k;
        for (k = 1; k < network.getLayers().size(); k++) {
            network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
        }
        //System.out.println(network.getLayers().get(2).getNodes().get(0).getInputs().get(0).getWeight());
        return network.getLayers().get(2).getNodes().get(0).getOutput();


    }


    public void updateWeights(Layer currentLayer) {
        List<Node> currNodes = currentLayer.getNodes();
        int i, j, k;
        for (i = 0; i < currNodes.size(); i++) {
            List<Connection> conn = currNodes.get(i).getInputs();
            for (j = 0; j < conn.size(); j++) {
                double dW = alpha * currNodes.get(i).getError() * conn.get(j).getSrc().getOutput();
                conn.get(j).setWeight(conn.get(j).getWeight() - dW);
                //System.out.println("Weight " + j + i + "is " + conn.get(j).getWeight() + "it used to be " + (conn.get(j).getWeight() + dW));
                //System.out.println(dW + " " + currNodes.get(i).getError() + " " + conn.get(j).getSrc().getOutput());


            }
            //System.out.println(currNodes.get(i).getError());
        }
    }

    public void backpropog(double[] errorOut) {
        int i, j, k;
        double grad = 0;
        Layer outputLayer = network.getLayers().get(network.getLayers().size() - 1);
        Layer hiddenLayer = network.getLayers().get(network.getLayers().size() - 2);


        for (i = 0; i < hiddenLayer.getNodes().size(); i++) {
            for (j = 0; j < outputLayer.getNodes().size(); j++) {
                List<Connection> conn = outputLayer.getNodes().get(j).getInputs();
                for (k = 0; k < conn.size(); k++) {
                    if (conn.get(k).getSrc() == hiddenLayer.getNodes().get(i)) {
                        grad = grad + conn.get(k).getWeight() * errorOut[j];
                        break;
                    }
                }
            }
            Node currNode = hiddenLayer.getNodes().get(i);
            currNode.setError(grad * (currNode.getOutputSum() / 100) * (1 - (currNode.getOutputSum() / 100)));
            currNode.resetOutputSum();   //RESETTING OUTPUTSUM
            grad = 0;
        }
        updateWeights(outputLayer);
        updateWeights(hiddenLayer);

    }

    public void MBGD(int epochs, int decayfactor) {  // Find target sum
        int i, j, k;
        double total = 0;
        double errorGrad[] = new double[network.getLayers().get(network.getLayers().size() - 1).getNodes().size()];
        for (i = 0; i <= epochs; i++) {
            for (j = 0; j < input.length; j++) {
/*                if(j%100 == 0){

                    backpropog(errorGrad);
                     total =0;

                     for(k=0; k<errorGrad.length; k++){   //Reset
                         total += errorGrad[k];
                         errorGrad[k] = 0;
                     }


                }*/
                network.setInputs(input[j]);
                for (k = 1; k < network.getLayers().size(); k++) {
                    network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
                }
                for (k = 0; k < target[k].length; k++) {
                    double currOutput = network.getLayers().get(network.getLayers().size() - 1).getNodes().get(k).getOutput();
                    errorGrad[k] += (currOutput - target[j][k]) * currOutput * (1 - currOutput);

                }
                backpropog(errorGrad);
                total = 0;

                for (k = 0; k < errorGrad.length; k++) {   //Reset
                    total += errorGrad[k];
                    errorGrad[k] = 0;

                }
            }
                System.out.println("Error at epoch " + i + " on training is " + total);
                System.out.println("Validation error is " + avgErrorValidation());
            }
        }

    public void train(int epochs) {

        double error;
        double prevError;
        double currError = 9999;
        for(int i=0; i<=epochs; i++){
            error = backpropagateabcd(i);

            System.out.println("Error for Epoch " + i + " is " + error);
            if(i%30 == 0){
                prevError = currError;
                currError = avgErrorValidation();
                System.out.println(" ---------------- \nValidation error is " + currError + "\n--------------");
                if(currError > prevError + Math.pow(10,-3)){
                    System.out.println("Validation error is increasing. Ending epochs!");
                    break;
                }
            }
        }
    }

    public double backpropagateabcd(int currentEpoch) {

        double error = 0;

        Map<Connection, Double> synapseNeuronDeltaMap = new HashMap<Connection, Double>();

        for (int i = 0; i < input.length; i++) {

            double[] currInput = input[i];
            int[] expectedOutput = target[i];

            List<Layer> layers = network.getLayers();

            network.setInputs(currInput);
            //double[] output = neuralNetwork.getOutput();

            for (int k = 1; k < network.getLayers().size(); k++) {
                network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
            }
            double[] listOfOutputs = new double[10];
            for (int k = 0; k < target[k].length; k++) {
                double currOutput = network.getLayers().get(network.getLayers().size() - 1).getNodes().get(k).getOutput();
                listOfOutputs[k] = currOutput;

            }

            //First step of the backpropagation algorithm. Backpropagate errors from the output layer all the way up
            //to the first hidden layer
            for (int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for (int k = 0; k < layer.getNodes().size(); k++) {
                    Node neuron = layer.getNodes().get(k);
                    double neuronError = 0;

                    if (j == layers.size()-1) { // IF output layer
                        //the order of output and expected determines the sign of the delta. if we have output - expected, we subtract the delta
                        //if we have expected - output we add the delta.
                        neuronError = listOfOutputs[k]*(1- listOfOutputs[k]) * (listOfOutputs[k] - expectedOutput[k]);
                    } else {
                        neuronError = neuron.getOutput()*(1 - neuron.getOutput());

                        double sum = 0;
                        List<Node> downstreamNeurons = layer.getNextLayer().getNodes();
                        for (Node downstreamNeuron : downstreamNeurons) {

                            int l = 0;
                            boolean found = false;
                            while (l < downstreamNeuron.getInputs().size() && !found) {
                                Connection synapse = downstreamNeuron.getInputs().get(l);

                                if (synapse.getSrc() == neuron) {
                                    sum += (synapse.getWeight() * downstreamNeuron.getError());
                                    found = true;
                                }

                                l++;
                            }
                        }

                        neuronError *= sum;
                    }

                    neuron.setError(neuronError);
                }
            }

            //Second step of the backpropagation algorithm. Using the errors calculated above, update the weights of the
            //network

            double newLearningRate = alpha/(1 + currentEpoch/5);
            //double newLearningRate = alpha;

            //System.out.println("Alpha" + newLearningRate);
            for(int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for(Node neuron : layer.getNodes()) {

                    for(Connection synapse : neuron.getInputs()) {

                        double delta = newLearningRate * neuron.getError() * synapse.getSrc().getOutput();

                        if(synapseNeuronDeltaMap.get(synapse) != null) {
                            double previousDelta = synapseNeuronDeltaMap.get(synapse);
                            delta += momentum * previousDelta;
                        }

                        synapseNeuronDeltaMap.put(synapse, delta);
                        synapse.setWeight(synapse.getWeight() - delta);
                    }
                }
            }

            for (int k = 1; k < network.getLayers().size(); k++) {
                network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
            }

            for (int k = 0; k < target[k].length; k++) {
                double currOutput = network.getLayers().get(network.getLayers().size() - 1).getNodes().get(k).getOutput();
                listOfOutputs[k] = currOutput;

            }

            error += findError(listOfOutputs, expectedOutput);
        }

        return error;
    }
    public double findError(double[] actual, int[] expected) {

        double sum = 0;

        if (actual.length != expected.length) {
            throw new IllegalArgumentException("Length mismatch in error function");
        }

        for (int i = 0; i < expected.length; i++) {
            sum += Math.pow(expected[i] - actual[i], 2);
        }

        return sum / 2;
    }

    }
