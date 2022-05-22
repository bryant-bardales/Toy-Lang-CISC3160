import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

public class ToyLangEval {

    private String s;
    private int currIdx;
    private char inputToken;
    private HashMap<String, Integer> hMap = new HashMap<String, Integer>(); // HashMap (key, value)

    // Method starts reading the file, line-by-line, using a while loop.
    public void startRead(Scanner fileText) {
        // Checks if the input scanner has another line to be read.
        while (fileText.hasNextLine()) {
            ToyLangEval(fileText.nextLine());
            declareVar();
        }
    }

    // Checks if the string ends with a semicolon.
    // If so, this method obtains the next element of the string.
    void nxtToken() {
        char c;
        if (!s.endsWith(";"))
            throw new RuntimeException("Missing ';' token exptected (1)");
        c = s.charAt(currIdx++);

        inputToken = c;
    }

    // Checks that the assignment ends with a semicolon.
    int eval() {
        int x = addOrSubtract();
        if (inputToken == ';') {
            return x;
        } else {
            throw new RuntimeException("Missing ';' token expected (2)");
        }
    }

    // Checks for a closing parenthesis.
    void checkParenth(char token) {
        if (inputToken == token) {
            nxtToken();
        } else {
            throw new RuntimeException("Missing Parenthesis");
        }
    }

    void ToyLangEval(String s) {
        this.s = s.replaceAll("\\s", ""); // Removes all empty spaces inside the string.
        currIdx = 0;
        nxtToken();
    }

    // Initializes the variable and the value assigned to the variable
    void declareVar() {

        String var = iD(); // Gets the sB.toString(), the variable
        int valueOrOperand = eval(); // Gets the value or the operand
        hMap.put(var, valueOrOperand); // Stores the var and operand into hashmap
        System.out.println("Output:");
        System.out.println(var + " = " + valueOrOperand);

    }

    // In the case the operator is addition or subtraction
    int addOrSubtract() {
        int x = multiplyOrDivide();
        while (inputToken == '+' || inputToken == '-') {
            char op = inputToken;
            nxtToken();
            int y = multiplyOrDivide();
            x = compute(op, x, y);
        }
        return x;
    }

    // In the case the operator is multiplication or division
    int multiplyOrDivide() {
        int x = factor();
        while (inputToken == '*' || inputToken == '/') {
            char op = inputToken;
            nxtToken();
            int y = factor();
            x = compute(op, x, y);
        }
        return x;
    }

    // The function will run if there is more than one line meaning more than one
    // assignment declaration
    int factor() {
        int x = 0;
        String temp = String.valueOf(inputToken);

        if (hMap.containsKey(temp)) {
            x = hMap.get(temp).intValue();
            nxtToken();
            return x;
        } else if (inputToken == '(') {
            nxtToken();
            x = addOrSubtract();
            checkParenth(')');
            return x;
        } else if (inputToken == '-') {
            nxtToken();
            x = factor();
            return -x;
        } else if (inputToken == '+') {
            nxtToken();
            x = factor();
            return x;
        } else if (inputToken == '0') {
            nxtToken();
            if (Character.isDigit(inputToken))
                throw new RuntimeException("Invalid value");
            return 0;
        }
        temp = "";

        while (Character.isDigit(inputToken)) {
            temp += inputToken;
            nxtToken();
        }

        return Integer.parseInt(temp);

    }

    // Identifies the initialization of a variable and returns the value of the
    // variable
    String iD() {
        StringBuilder sB = new StringBuilder();

        if (Character.isLetter(inputToken))
            sB.append(inputToken);
        else
            throw new RuntimeException("Invalid variable name");
        nxtToken(); // Gets the next token after the variable which should be (=)

        while (Character.isLetter(inputToken) || inputToken == '_' || Character.isDigit(inputToken)) {
            sB.append(inputToken);
            nxtToken();
        }
        if (inputToken != '=')
            throw new RuntimeException("Not an valid assignment statement");
        nxtToken(); // Moves the token after the (=), over to the operand
        return sB.toString(); // Returns the first variable to string 'var'
    }

    // Computes the operation
    static int compute(char op, int x, int y) {
        int z = 0;
        switch (op) {
            case '+':
                z = x + y;
                break;
            case '-':
                z = x - y;
                break;
            case '*':
                z = x * y;
                break;
            case '/':
                z = x / y;
                break;
        }
        return z;
    }

    public static void main(String[] args) {

        try {
            // Creates a scanner object and takes the file the user provides into the
            // scanner-object
            Scanner scn = new Scanner(
                    new FileInputStream("/Users/bryantbardales/Desktop/GitHub/Toy-Lang-CISC3160/input4.txt"));

            // Creates an object for the Evaluator class
            ToyLangEval Evaluator = new ToyLangEval();
            Evaluator.startRead(scn);

        } // Exception for when file is not entered by user or the file doesn't exist
        catch (Exception exp) {
            System.out.println("Error, file not found " + exp);
        }
    }
}