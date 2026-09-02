import java.util.Scanner; 
import java.util.ArrayList;

public class Main {
    
    public static double rootCalculator(double x, double power) {
         
        boolean keepLooping = true; 
        int base = 0; 
        
        while (keepLooping) {
            if (x <= Math.pow(base,power)) {
                keepLooping = false; 
            }
            if (x > Math.pow(base,power)) {
                base++;
            }
        }
        double lowerBound = base-1; 
        double upperBound = base; 
        double remainder = (x - Math.pow(lowerBound,power))/(Math.pow(upperBound,power) - Math.pow(lowerBound,power));
        
        return lowerBound + remainder; 
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double starter = 0; 
        double root = 0;
        while (starter >= 0) {
            System.out.println("Enter a number: ");
            starter = in.nextDouble();
            System.out.println("Enter a root: ");
            root = in.nextDouble();
            System.out.println("Estimate: " + rootCalculator(starter, root));
            System.out.println("Expected: " + Math.pow(starter, 1.0/root));
        }
        
    }
}