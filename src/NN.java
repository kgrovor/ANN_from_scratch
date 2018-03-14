import java.util.ArrayList;
import java.util.List;

public class NN {
    private List<Layer> layers = new ArrayList<Layer>();
    public NN(int numHidden,String actHidden, String actOutput){
        layers.add(new Layer()); // Layer 0 - input layer
        int i;
        for(i=1; i<numHidden; i++){
            layers.add(new Layer(layers.get(i-1),new Node("sigmoid")));
        }
        layers.add(new Layer(layers.get(i),new Node("linear")));
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setInputs(double[] inputs){

    }
}
