package com.gmail.sedgeAlgo.ch3;

/**
 * Created by rayanral on 5/30/16.
 */
public class Evaluate {

    public static void main(String[] args) {
        Stack<String> operations = new Stack<>();
        Stack<Double> vals = new Stack<>();

        //read operation
        int i = 0;
        while(i < args.length) {
            String input = args[i];
            i++;
            switch (input) {
                case "(":
                    break;
                case "+":
                    operations.push("+");
                    break;
                case "-":
                    operations.push("-");
                    break;
                case "/":
                    operations.push("/");
                    break;
                case "*":
                    operations.push("*");
                    break;
                case "sqrt":
                    operations.push("sqrt");
                    break;
                case ")":
                    String operation = operations.pop();
                    double val1 = vals.pop();
                    if (operation.equals("+")) vals.push(val1 + vals.pop());
                    if (operation.equals("-")) vals.push(val1 - vals.pop());
                    if (operation.equals("/")) vals.push(val1 / vals.pop());
                    if (operation.equals("sqrt")) vals.push(Math.sqrt(val1));
                    break;
                default:
                    vals.push(Double.parseDouble(input));
                    break;
            }
        }
        System.out.println(vals.pop());
    }

}

class UnmatchingParenthesesException extends RuntimeException {
}


class Parentheses {

    public static void main(String[] args) {
        Stack<String> parent = new Stack<>();
        int i = 0;
        while(i < args.length) {
            String input = args[i];
            if (input.equals("{") || input.equals("(") || input.equals("[")) parent.push(input);
            else {
                String stored = parent.pop();
                boolean figure = input.equals("}") && stored.equals("{");
                boolean round = input.equals(")") && stored.equals("(");
                boolean square = input.equals("]") && stored.equals("[");
                if(!(figure || round || square)) throw new UnmatchingParenthesesException();
            }
            i++;
        }
        System.out.println("Correct!");
    }

}