import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class Backprop {

    double[][] input;
    int[][] target;
    double alpha = 0.0001;
    NN network;

    public Backprop(){
        network = new NN(1,"sigmoid","linear");   //Adding nodes
        double[] unused = {1.2,1.1};
        int i;
        for(i=0; i< 64; i++){
            network.getLayers().get(0).addNode(new Node("linear"),unused);
        }
        for(i=0; i<5; i++){
            network.getLayers().get(1).addNode(new Node("sigmoid"),generateRandom(network.getLayers().get(0).getNodes().size()));
        }
        for(i=0; i<10; i++){

            network.getLayers().get(2).addNode(new Node("sigmoid"),generateRandom(network.getLayers().get(1).getNodes().size()));
        }

    }

    double[] generateRandom(int dim){
        double[] arr = new double[dim];
        int i;
        //Random gen = new Random(55);
        for(i=0; i< dim; i++){
            arr[i] = (Math.random() * 1) - 1;
            System.out.println(arr[i]);
            //arr[i] = 0.2;

        }

        return arr;
    }



    public static void main(String arg[]) throws IOException {

        Backprop handle = new Backprop();
        handle.parseInput();
        handle.MBGD(3000,1);


    }

    public void parseInput() throws IOException {
        String inp = MyUtil.readTextFile("train.txt");
        StringTokenizer st = new StringTokenizer(inp,"\n");
        double[][] vals = new double[st.countTokens()][64];
        int[] intTarget = new int[st.countTokens()];
        int len = st.countTokens();
        int i = 0;
        int j,k;
        while(st.hasMoreTokens()){
            String[] sp = st.nextToken().split(",");
           for(j=0; j<sp.length -1; j++) {
               vals[i][j] = Double.parseDouble(sp[j]);
           }
               intTarget[i] = Integer.parseInt(sp[sp.length - 1].trim());
            i++;
        }
        int[][] tar = new int[len][10];
        for(i=0; i<len; i++){
            for(j=0; j<10;j ++){
                tar[i][j] = 0;
            }
            tar[i][intTarget[i]] = 1;
        }
        this.input = vals;
        this.target = tar;

    }


    public double giveManualInput(double[] inp){
        network.setInputs(inp);
        int k;
        for(k=1; k<network.getLayers().size(); k++){
            network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
        }
        //System.out.println(network.getLayers().get(2).getNodes().get(0).getInputs().get(0).getWeight());
        return network.getLayers().get(2).getNodes().get(0).getOutput();


    }


   /* public void SGD(int epochs,int decay){
        int i,j,k,l;
        double outputError = 0;
        double prevOutputError = 0;

        for(i=0; i <= epochs; i++){
            for(j=0; j< input.length; j++){
                List<Node> inputNodes = network.getLayers().get(0).getNodes();
                *//*for(l=0; l< input[j].length; l++){
                    inputNodes.get(l).setOutput(input[j][l]);               //Set output of 1st layer as input values
                    //System.out.println(" input " + inputNodes.get(l).getOutput());
                }*//*
                network.setInputs(input[j]);

                for(k=1; k<network.getLayers().size(); k++){
                    network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
                }
                prevOutputError = outputError;
                outputError = backPropogate(target);
            }
            alpha = alpha/(1 + i*decay)
            if(Math.abs(outputError - prevOutputError) < Math.pow(10,-6)){
                System.out.println("Error threshold");
                System.out.println(outputError + " " + prevOutputError);
                break;
            }
            if(i%30 == 0){
                //System.out.println(outputError);
                System.out.println("At iteration "+ i + "the error is " + (outputError));
                System.out.println(network.getLayers().get(1).getNodes().get(1).getError());
            }

        }
    }
*/
    /*public double backPropogate(int[][] targetV){
        Layer outputLayer = network.getLayers().get(network.getLayers().size()-1);
        int i,j,k,l;
        double totError = 0;
        List<Node> outputNodes = outputLayer.getNodes();
        for(i=0; i<outputLayer.getNodes().size(); i++){
            //double indError = outputNodes.get(i).getOutput()*(1- outputNodes.get(i).getOutput())*(outputNodes.get(i).getOutput() - targetV[i]);
            double indError = (outputNodes.get(i).getOutput() - targetV[i]);  // ADD SGM*(1-SGM)
            //System.out.println(indError);
            outputNodes.get(i).setError(indError);  // Output layer errors
            totError += indError;
        }
        //System.out.println(totError + "ddnndnnd");
        // Update last set of weights
        *//*for(i=0; i<outputNodes.size(); i++){
            List<Connection> conn = outputNodes.get(i).getInputs();  //conn - List of inputs
            for(j=0; j<conn.size(); j++){
                double dW = alpha*outputNodes.get(i).getError()*conn.get(j).getSrc().getOutput();
                conn.get(j).setWeight(conn.get(j).getWeight() - dW);
            }
        }*//*
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

                sum = sum*currNode.getOutput()*(1-currNode.getOutput());   // HARDCODED SIGMOID FOR HIDDEN
                currNode.setError(sum);
            }

                updateWeights(currLayer);
        }
        return totError;  //Error at output layer
    }*/

    public void updateWeights(Layer currentLayer){
        List<Node> currNodes = currentLayer.getNodes();
        int i,j,k;
        for(i=0; i<currNodes.size(); i++){
            List<Connection> conn = currNodes.get(i).getInputs();
            for(j=0; j< conn.size(); j++){
                double dW = alpha*currNodes.get(i).getError()*conn.get(j).getSrc().getOutput();
                conn.get(j).setWeight(conn.get(j).getWeight() - dW);
                //System.out.println("Weight " + j + i + "is " + conn.get(j).getWeight() + "it used to be " + (conn.get(j).getWeight() + dW));
            }
        }
    }

    public void backpropog(double[] errorOut){
        int i,j,k;
        double grad =0;
        Layer outputLayer = network.getLayers().get(network.getLayers().size()-1);
        Layer hiddenLayer = network.getLayers().get(network.getLayers().size()-2);

        for(i=0; i<hiddenLayer.getNodes().size(); i++){
            for(j=0; j<outputLayer.getNodes().size(); j++){
                List<Connection> conn = outputLayer.getNodes().get(j).getInputs();
                for(k=0; k< conn.size(); k++){
                    if(conn.get(k).getSrc() == hiddenLayer.getNodes().get(i)){
                        grad = grad + conn.get(k).getWeight()*errorOut[j];
                        break;
                    }
                }
            }
            Node currNode = hiddenLayer.getNodes().get(i);
            currNode.setError(grad*(currNode.getOutputSum()/100)*(1-(currNode.getOutputSum()/100)));
            currNode.resetOutputSum();   //RESETTING OUTPUTSUM
            grad = 0;
        }
        updateWeights(outputLayer);
        updateWeights(hiddenLayer);

    }

    public void MBGD(int epochs,int decayfactor){  // Find target sum
        int i,j,k;
        double errorGrad[] = new double[network.getLayers().get(network.getLayers().size()-1).getNodes().size()];
        for(i=0; i<epochs; i++){
            for(j=0; j<input.length; j++){
                if(j%100 == 0){

                    backpropog(errorGrad);
                    double total =0;

                     for(k=0; k<errorGrad.length; k++){   //Reset
                         total += errorGrad[k];
                         errorGrad[k] = 0;
                     }
                    System.out.println("Error at iteration j is " + total);

                }
                network.setInputs(input[j]);
                for(k=1; k<network.getLayers().size(); k++){
                    network.fwdPropogateTo(network.getLayers().get(k));   // Propogate forward
                }
                for(k=0; k< target[k].length; k++){
                    double currOutput = network.getLayers().get(network.getLayers().size()-1).getNodes().get(k).getOutput();
                    errorGrad[k] += (currOutput - target[j][k])*currOutput*(1-currOutput);

                }

            }
        }
    }

}