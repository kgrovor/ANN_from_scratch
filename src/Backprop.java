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
    boolean batchcomplete;


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
                if(currError > prevError ){ //+ Math.pow(10,-3)
                    System.out.println("Validation error is increasing. Ending epochs!");
                    break;
                }
            }
        }
    }

    public double backpropagateabcd(int currentEpoch) {

        double error = 0;

        Map<Connection, Double> connectionToDelta = new HashMap<Connection, Double>(); // Used to get previous delta value (For momentum calculation)

        for (int i = 0; i < input.length; i++) {
            if(i%100==0){
                batchcomplete = true;
            }
            else{
                batchcomplete = false;
            }

            double[] currInput = input[i];
            int[] expectedOutput = target[i];

            List<Layer> layers = network.getLayers();

            network.setInputs(currInput);   // Set outputs of input layer

            for (int k = 1; k < network.getLayers().size(); k++) {
                network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
            }
            double[] listOfOutputs = new double[10];
            for (int k = 0; k < target[k].length; k++) {
                double currOutput = network.getLayers().get(network.getLayers().size() - 1).getNodes().get(k).getOutput();
                listOfOutputs[k] = currOutput;

            }
            if(network.batchcomplete == true) {

                //First step of the backpropagation algorithm. Backpropagate errors from the output layer all the way up
                for (int j = layers.size() - 1; j > 0; j--) {
                    Layer layer = layers.get(j);

                    for (int k = 0; k < layer.getNodes().size(); k++) {  //For all nodes in the current layer
                        Node currNode = layer.getNodes().get(k);
                        double nodeError = 0;

                        if (j == layers.size() - 1) {         // IF output layer
                            nodeError = listOfOutputs[k] * (1 - listOfOutputs[k]) * (listOfOutputs[k] - expectedOutput[k]);
                        } else {
                            nodeError = currNode.getOutput() * (1 - currNode.getOutput());

                            double sum = 0;
                            List<Node> listOfNodes = layer.getNextLayer().getNodes(); //n+1th layer's nodes
                            for (Node cNode : listOfNodes) {                               // Since list of connections (inputs) is available in the n+1th later for the connections b/w nth and n+1th layer
                                int l = 0;
                                while (l < cNode.getInputs().size()) {
                                    Connection synapse = cNode.getInputs().get(l);

                                    if (synapse.getSrc() == currNode) {                    // Update nth node wrt all the n+1th nodes
                                        sum += (synapse.getWeight() * cNode.getError());
                                        break;
                                    }

                                    l++;
                                }
                            }

                            nodeError = nodeError * sum;
                        }

                        currNode.setError(nodeError);
                    }
                }

                //Second step of the backpropagation algorithm. Using the errors calculated above, update the weights of the network

                double newLearningRate = alpha / (1 + currentEpoch / 5);
                //double newLearningRate = alpha;

                //System.out.println("Alpha" + newLearningRate);
                for (int j = layers.size() - 1; j > 0; j--) {
                    Layer layer = layers.get(j);

                    for (Node cNode : layer.getNodes()) {
                        for (Connection connect : cNode.getInputs()) {          // Connection is a object with the weight of an individual connection b/w two nodes
                            double delta = newLearningRate * cNode.getError() * connect.getSrc().getOutput();   // Value of dW

                            if (connectionToDelta.get(connect) != null) {
                                double previousDelta = connectionToDelta.get(connect);
                                delta += momentum * previousDelta;
                            }

                            connectionToDelta.put(connect, delta);
                            connect.setWeight(connect.getWeight() - delta);
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

        return sum / 2000;
    }

    }
