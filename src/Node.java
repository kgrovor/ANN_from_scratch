import java.util.List;

public class Node {
    String actfunc;
    List<Connection> input;
    double output;
    double error;

    public Node(String activation){
        this.actfunc = activation;
    }/*
    public String toString(){
        //System.out.println("Input is " + input);
        return "Input is " + input;
    }
    double getSetOutput(){
        output = MyUtil.activation(input,actfunc);
        return output;
    }*/
    public void addInput(Connection c){
        input.add(c);
    }
    public List<Connection> getInputs(){
        return input;
    }
    public double getOutput(){
        int i;
        double outp = 0;
        for(i=0; i<input.size(); i++){
            outp += input.get(i).getSrc().output*input.get(i).getWeight();
        }
        this.output = MyUtil.activation(outp,actfunc);
        return this.output;
    }
}
