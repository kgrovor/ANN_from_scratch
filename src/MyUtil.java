import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyUtil {
    public static double activation(double input,String func){
        if(func.equals("sigmoid")){
            return 1/(1+Math.exp(-input));
        }
        else return input;
    }
    public static String readTextFile(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName).toAbsolutePath()));
        return content;
    }
}

