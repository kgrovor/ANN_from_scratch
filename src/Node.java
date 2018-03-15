import java.util.ArrayList;
import java.util.List;

public class Node {
    private String actfunc;
    private List<Connection> input;
    private double inputSum;
    private double output;
    private double error;

    public Node(String activation){
        this.actfunc = activation;
        input = new ArrayList<Connection>();
    }
    public void addInput(Connection c){
        input.add(c);
    }
    public List<Connection> getInputs(){
        return input;
    }
    public double generateOutput(){
        int i;
        double outp = 0;
        for(i=0; i<input.size(); i++){
            outp += input.get(i).getSrc().output*input.get(i).getWeight();
        }
        this.inputSum = outp;
        this.output = MyUtil.activation(outp,actfunc);
        return this.output;
    }
    public double getOutput(){
        return output;
    }
    public void setOutput(double outp){
        this.output = outp;
    }
    public void setError(double err){
        this.error = err;
    }
    public double getInputSum(){
        return inputSum;
    }

    public double getError() {
        return error;
    }
}
