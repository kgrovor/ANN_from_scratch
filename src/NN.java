import java.util.ArrayList;
import java.util.List;

public class NN {
    private List<Layer> layers;
    public NN(int numHidden,String actHidden, String actOutput){
        layers = new ArrayList<Layer>();
        layers.add(new Layer()); // Layer 0 - input layer
        int i;
        for(i=1; i<numHidden; i++){
            layers.add(new Layer(layers.get(i-1),new Node("sigmoid")));
        }
        layers.add(new Layer(layers.get(i-1),new Node("linear")));
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setInputs(double[] inputs){
        List<Node> targetNodes = layers.get(0).getNodes();
        if(inputs.length != targetNodes.size() - layers.get(0).hasBias()){
            throw new IllegalArgumentException("Input size mismatch");
        }
        else{
            int i;
            for(i=layers.get(0).hasBias(); i< targetNodes.size() - layers.get(0).hasBias(); i++){
                targetNodes.get(i).setOutput(inputs[i-layers.get(0).hasBias()]);
            }
        }

    }

    public void fwdPropogateTo(Layer dst){
        int i,j;
        if(dst.hasPrev()) {                             // Not Input layer
            List<Node> dstNodes = dst.getNodes();
            for(i=0; i< dstNodes.size(); i++){
                dstNodes.get(i).generateOutput();
            }
        }
        else{
            throw new IllegalArgumentException("Can't propogate to input layer");
        }
    }

   /* public void bckPropogate(Layer nplus1, Layer n){
        int i;
        double[] outputCalc = new double[n.getNodes().size()];
        for(i=0; i<nplus1.getNodes().size(); i++){
            List<Connection> inputsToNode = nplus1.getNodes().get(i).getInputs();
        }
    }*/


}
