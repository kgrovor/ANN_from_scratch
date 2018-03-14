public class Connection {
    private Node source;
    private double weight;

    Connection(Node src,double weight){
        this.source = src;
        this.weight = weight;

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
