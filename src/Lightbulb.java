public class Lightbulb {
    public int x_switch;
    public int y_switch;

    public String constraint;

    public boolean hello(boolean[] x, boolean[] y){
        if (constraint.charAt(0) == '1'){
            return x[x_switch] && y[y_switch];
        }
        if (constraint.charAt(1) == '1'){
            return x[x_switch] && y[y_switch] == false;
        }
        if (constraint.charAt(2) == '1'){
            return x[x_switch] == false && y[y_switch];
        }
        if (constraint.charAt(3) == '1'){
            return x[x_switch] == false && y[y_switch] == false;
        }
        return false; // else 0000
    }
}
