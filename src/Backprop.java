public class Backprop {

    int[][] input = {{1,1},{1,0},{0,1},{0,0}};
    int[] target = {1,0,0,0};
    Layer outputLayer;
    Layer hiddenLayer;
    Layer inputLayer;
    double alpha = 0.01;

    public static void main(String arg[]){
        Backprop handle = new Backprop();
        handle.setup();
        handle.train(1000);


    }

    public void setup(){
        outputLayer = new Layer(1,"sigmoid");
        hiddenLayer = new Layer(2,outputLayer,"sigmoid");
        inputLayer = new Layer(2,hiddenLayer,"identity");
        outputLayer.weightPrev = MyUtil.trasposeMatrix(hiddenLayer.weightNext);
        hiddenLayer.weightPrev = MyUtil.trasposeMatrix(inputLayer.weightNext);
    }

    public void train(int epochs){

        int i,k;
        for(k=0; k<epochs; k++) {
            for (i = 0; i < input.length; i++) {
                int j;
                for (j = 0; j < inputLayer.nodes.length; j++) {
                    inputLayer.nodes[j].output = input[i][j];
                }
                fowardFeed(inputLayer, hiddenLayer);
                fowardFeed(hiddenLayer, outputLayer);
                outputLayer.nodes[0].errorGrad = findOutputError(outputLayer, i);
                if(k%100 == 0) {
                    System.out.println("At epoch " + k + " of input " + i +" Error is " + outputLayer.nodes[0].errorGrad);
                }
                backPropError(outputLayer, hiddenLayer);
                backPropError(hiddenLayer, inputLayer);
                backPropError(hiddenLayer, inputLayer);
                clearValues();
            }
        }

    }
    public void clearValues(){
        inputLayer.clear();
        hiddenLayer.clear();
        inputLayer.clear();
    }

    public void backPropError(Layer from,Layer to){
        int i,j,k;
        for(i=0; i< to.nodes.length; i++){
            to.nodes[i].errorGrad = 0;
            for(j=0; j<from.nodes.length; j++){
                to.nodes[i].errorGrad =+ to.weightNext[i][j]*from.nodes[j].errorGrad;
            }
            System.out.println(to.nodes[i].errorGrad);
            to.nodes[i].errorGrad =  to.nodes[i].errorGrad*to.nodes[i].output*(1 - to.nodes[i].output);
            for(j = 0; j<from.nodes.length; j++){
                to.weightNext[i][j] += -alpha*to.nodes[i].errorGrad;
            }


        }
    }

    public double findOutputError(Layer layer, int targetno){
        return -2*(target[targetno] - layer.nodes[0].output);

    }

    public void fowardFeed(Layer src, Layer dest){
        int i,j;
        for(i = 0; i<dest.nodes.length; i++ ){
            for(j=0; j<src.nodes.length; j++){
                dest.nodes[i].input += src.nodes[j].output*src.weightNext[j][i];
                dest.nodes[i].getSetOutput();
            }
        }

    }
}
