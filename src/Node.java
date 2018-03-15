import java.util.ArrayList;
import java.util.List;

public class Node {
    private String actfunc;
    private List<Connection> input;
    private double inputSum = 0;
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
        for(i=0; i<this.input.size(); i++){
            /*if(Double.isNaN(input.get(i).getSrc().getOutput()) || Double.isNaN(input.get(i).getWeight())){
                System.out.println("ERROR LIES IN " + i + "input is " + input.get(i).getSrc().getOutput() + "weight is " + input.get(i).getWeight());
            }*/
            outp += input.get(i).getSrc().getOutput() *input.get(i).getWeight();
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
