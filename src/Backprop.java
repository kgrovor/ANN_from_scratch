import java.util.List;
import java.util.Random;

public class Backprop {

    double[][] input = {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
    int[] target = {0, 1, 1, 0};
    double alpha = 0.01;
    NN network;

    public Backprop(){
        network = new NN(1,"sigmoid","linear");   //Adding nodes
        double[] unused = {1.2,1.1};
        network.getLayers().get(0).addNode(new Node("linear"),unused);
        network.getLayers().get(0).addNode(new Node("linear"),unused);
        //System.out.println("Layers 0 mdsm " + network.getLayers().get(0).hasBias());
        //System.out.println(network.getLayers().get(0).getNodes().size());
        network.getLayers().get(1).addNode(new Node("sigmoid"),generateRandom(network.getLayers().get(0).getNodes().size()));
        /*System.out.println(network.getLayers().get(0).getNodes().size());*/
        network.getLayers().get(1).addNode(new Node("sigmoid"),generateRandom(network.getLayers().get(0).getNodes().size()));

        network.getLayers().get(1).addNode(new Node("sigmoid"),generateRandom(network.getLayers().get(0).getNodes().size()));

        network.getLayers().get(1).addNode(new Node("sigmoid"),generateRandom(network.getLayers().get(0).getNodes().size()));
        //System.out.println(network.getLayers().get(0).getNodes().size());


        network.getLayers().get(2).addNode(new Node("linear"),generateRandom(network.getLayers().get(1).getNodes().size()));
    }

    double[] generateRandom(int dim){
        double[] arr = new double[dim];
        int i;
        //System.out.println("Reaaaadaaaay?");
        Random gen = new Random(55);
        for(i=0; i< dim; i++){
            arr[i] = Math.random()*Math.pow(-1,gen.nextInt(56));

        }

        return arr;
    }



    public static void main(String arg[]) {
        Backprop handle = new Backprop();
        handle.SGD(10000);
        double[] inp = {1,0};

        System.out.println(handle.giveManualInput(inp));

    }
    public double giveManualInput(double[] inp){
        network.setInputs(inp);
        int k;
        for(k=1; k<network.getLayers().size(); k++){
            network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
        }
        return network.getLayers().get(2).getNodes().get(0).getOutput();


    }

    public void SGD(int epochs){
        int i,j,k,l;
        double outputError = 0;
        double prevOutputError = 0;
        for(i=0; i <= epochs; i++){

            for(j=0; j< input.length; j++){
                List<Node> inputNodes = network.getLayers().get(0).getNodes();
                /*for(l=0; l< input[j].length; l++){
                    inputNodes.get(l).setOutput(input[j][l]);               //Set output of 1st layer as input values
                    //System.out.println(" input " + inputNodes.get(l).getOutput());
                }*/
                network.setInputs(input[j]);

                for(k=1; k<network.getLayers().size(); k++){
                    network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
                }
                prevOutputError = outputError;
                outputError = backPropogate(target);
            }
            /*if(outputError - prevOutputError < Math.pow(10,-8)){
                System.out.println("Error threshold");
                break;
            }*/
            if(true){
                //System.out.println(outputError);
                System.out.println("At iteration "+ i + "the error is " + (outputError - prevOutputError));
            }

        }
    }

    public double backPropogate(int[] targetV){
        Layer outputLayer = network.getLayers().get(network.getLayers().size()-1);
        int i,j,k,l;
        double totError = 0;
        List<Node> outputNodes = outputLayer.getNodes();
        for(i=0; i<outputLayer.getNodes().size(); i++){
            double indError = outputNodes.get(i).getOutput()*(1- outputNodes.get(i).getOutput())*(outputNodes.get(i).getOutput() - targetV[i]);
            //System.out.println(indError);
            outputNodes.get(i).setError(indError);  // Output layer errors
            totError += indError;
        }
        //System.out.println(totError + "ddnndnnd");
        // Update last set of weights
        /*for(i=0; i<outputNodes.size(); i++){
            List<Connection> conn = outputNodes.get(i).getInputs();  //conn - List of inputs
            for(j=0; j<conn.size(); j++){
                double dW = alpha*outputNodes.get(i).getError()*conn.get(j).getSrc().getOutput();
                conn.get(j).setWeight(conn.get(j).getWeight() - dW);
            }
        }*/
        updateWeights(outputLayer);



        for(i = network.getLayers().size()-2; i>0 ; i--){
            Layer currLayer = network.getLayers().get(i);
            Layer nextLayer = network.getLayers().get(i+1);

            for(j=0; j<currLayer.getNodes().size(); j++){
                Node currNode = currLayer.getNodes().get(j);
                double sum = 0;
                for(k=0; k<nextLayer.getNodes().size(); k++){
                    Node nextNode = nextLayer.getNodes().get(k);
                    for(l=0; l<nextNode.getInputs().size();l++){
                        if(nextNode.getInputs().get(l).getSrc() == currNode){
                            sum = sum + nextNode.getError()*nextNode.getInputs().get(l).getWeight();
                            break;
                        }
                    }
                }

                sum = sum + sum*currNode.getOutput()*(1-currNode.getOutput());   // HARDCODED SIGMOID FOR HIDDEN
                currNode.setError(sum);
            }

                updateWeights(currLayer);
        }
        return totError;
    }

    public void updateWeights(Layer currentLayer){
        List<Node> currNodes = currentLayer.getNodes();
        int i,j,k;
        for(i=0; i<currNodes.size(); i++){
            List<Connection> conn = currNodes.get(i).getInputs();
            for(j=0; j< conn.size(); j++){
                double dW = alpha*currNodes.get(i).getError()*conn.get(j).getSrc().getOutput();
                conn.get(j).setWeight(conn.get(j).getWeight() + dW);                                                    // WHY PLUS
            }
        }
    }

}