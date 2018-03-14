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
                    node.addInput(new Connection(prev.get(i),weights[i]));
                }

            }
        }
    }

}


