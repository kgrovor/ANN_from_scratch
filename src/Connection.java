public class Connection {
    private Node source;
    private double weight;

    Connection(Node src,double weight){    // Connection source is always the i+1th layer
        this.source = src;
        this.weight = weight;

        if(Double.isNaN(weight)){
            throw new IllegalArgumentException("Weight is Nan");
        }

    }

    public double getWeight(){
        return weight;
    }
    public Node getSrc(){
        return source;
    }
    public void setWeight(double newW){
        this.weight = newW;
    }
}
