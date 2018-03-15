import java.util.*;
public class Layer {
    private Layer prevLayer;
    private Layer nextLayer;
    private List<Node> nodes;
    private Node bias;

    public Layer(){
        nodes = new ArrayList<Node>();
        prevLayer = null;
    }

    public Layer(Layer prevLayer){
        this();
        this.prevLayer = prevLayer;
    }
    public Layer(Layer prevLayer, Node bias){
        this(prevLayer);
        this.bias = bias;
        nodes.add(bias);
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public int hasBias(){
        if(bias == null){
            return 0;
        }
        return 1;
    }

    public Boolean hasPrev(){
        if(prevLayer!=null)
            return true;
        return false;
    }

    public void addNode(Node node,double weights[]){
        nodes.add(node);

        if(prevLayer!=null){
            if(prevLayer.getNodes().size() != weights.length){
                throw new IllegalArgumentException("Weight size mismatch");
            }
            else{
                List<Node> prev = prevLayer.getNodes();
                int i;
                for(i=0; i<prev.size(); i++){
                    node.addInput(new Connection(prev.get(i),weights[i]));    //i+1th layer will have array of inputs, the connection themselves can call getSource to find sources
                }

            }
        }
    }

    public void calcOutputError(double[] target){
        int i;
        for(i=0; i<nodes.size(); i++){
            Node curr = nodes.get(i);
            curr.setError(curr.getOutput() - target[i]);
        }
    }

}


