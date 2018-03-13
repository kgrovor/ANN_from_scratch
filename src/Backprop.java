public class Backprop {
    public static void main(String arg[]){
        Layer outputLayer = new Layer(1,"sigmoid");
        Layer hiddenLayer = new Layer(2,outputLayer,"sigmoid");
        Layer inputLayer = new Layer(2,hiddenLayer,"identity");
        outputLayer.weightPrev = MyUtil.trasposeMatrix(hiddenLayer.weightNext);
        hiddenLayer.weightPrev = MyUtil.trasposeMatrix(inputLayer.weightNext);

        int[][] input = {{},{}}



    }
}
