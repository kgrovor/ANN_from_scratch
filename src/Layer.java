public class Layer {
    double weightNext[][];
    double weightPrev[][];
    int numberOfNodes;
    String actfunc;
    Node nodes[];

    public Layer(int numberOfNodes,Layer nextLayer, String actfunc){
        this.weightNext = new double[numberOfNodes][nextLayer.numberOfNodes];
        this.numberOfNodes = numberOfNodes;
        this.actfunc = actfunc;
        nodes = new Node[numberOfNodes];
        int i = 0,j;
        for(i=0; i<numberOfNodes; i++){
            nodes[i] = new Node(actfunc);
        }
        for(i=0; i<numberOfNodes; i++){
            for(j=0; j <nextLayer.numberOfNodes; j++ ){
                weightNext[i][j] = 1;
            }
        }
    }
    public Layer(int numberOfNodes, String actfunc){
        this.numberOfNodes = numberOfNodes;
        this.actfunc = actfunc;
        nodes = new Node[numberOfNodes];
        int i = 0;
        for(i=0; i<numberOfNodes; i++){
            nodes[i] = new Node(actfunc);
        }
    }

}
