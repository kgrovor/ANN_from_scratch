public class Node {
    String actfunc;
    double input;
    double output;

    public Node(String activation){
        this.actfunc = activation;
    }
    public String toString(){
        //System.out.println("Input is " + input);
        return "Input is " + input;
    }
    double getSetOutput(){
        output = MyUtil.activation(input,actfunc);
        return output;
    }
}
