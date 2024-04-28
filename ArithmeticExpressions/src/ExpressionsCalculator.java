import java.util.*;

public class ExpressionsCalculator {
    private String expression;
    private int result;
    private Map<String, Integer> variables;
    public ExpressionsCalculator() {
        this.expression = "";
        this.result = 0;
        this.variables = new HashMap<>();
    }

    public String replaceVariables() {
        Scanner scanner = new Scanner(System.in);
        String[] terms = expression.split("[+\\-*/^()]");
        Set<String> variableSet = new HashSet<>(Arrays.asList(terms));
        List<String> uniqueVariables = new ArrayList<>(variableSet);
        for (String variable : uniqueVariables) {
            if (!isNumeric(variable) && !variable.isEmpty()) {
                if (!variables.containsKey(variable)) {
                    System.out.println("Ingrese el valor para '" + variable + "':");
                    Integer value = scanner.nextInt();
                    variables.put(variable, value);
                }
                expression = expression.replace(variable, Integer.toString(variables.get(variable)));
            }
        }
        return expression;
    }
/*
    public double evaluateExpression() {
        this.expression = this.expression.replaceAll("\\s+", "");
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        for (int i = 0; i < this.expression.length(); i++) {
            char character = this.expression.charAt(i);
            if (Character.isDigit(character)) {
                StringBuilder number = new StringBuilder();
                while (i < this.expression.length() && (Character.isDigit(this.expression.charAt(i)) || this.expression.charAt(i) == '.')) {
                    number.append(this.expression.charAt(i));
                    i++;
                }
                i--;
                operandStack.push(Double.parseDouble(number.toString()));
            } else if (character == '(') {
                operatorStack.push(character);
            } else if (character == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    applyOperation(operatorStack.pop(), operandStack);
                }
                operatorStack.pop();
            } else if (isOperator(character)) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(character)) {
                    applyOperation(operatorStack.pop(), operandStack);
                }
                operatorStack.push(character);
            } else {
                System.out.println("Error: El caracter " +character +" es invalido");
                System.exit(0);
            }
        }
        while (!operatorStack.isEmpty()) {
            applyOperation(operatorStack.pop(), operandStack);
        }
        return operandStack.pop();
    }
*/
    public double evaluateExpression() {
        this.expression = this.expression.replaceAll("\\s+", "");
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        try {
            for (int i = 0; i < this.expression.length(); i++) {
                char character = this.expression.charAt(i);
                if (Character.isDigit(character)) {
                    StringBuilder number = new StringBuilder();
                    while (i < this.expression.length() && (Character.isDigit(this.expression.charAt(i)) || this.expression.charAt(i) == '.')) {
                        number.append(this.expression.charAt(i));
                        i++;
                    }
                    i--;
                    operandStack.push(Double.parseDouble(number.toString()));
                } else if (character == '(') {
                    operatorStack.push(character);
                } else if (character == ')') {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                        applyOperation(operatorStack.pop(), operandStack);
                    }
                    if (operatorStack.isEmpty()) {
                        throw new IllegalArgumentException("Paréntesis desequilibrados");
                    }
                    operatorStack.pop();
                } else if (isOperator(character)) {
                    while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(character)) {
                        applyOperation(operatorStack.pop(), operandStack);
                    }
                    operatorStack.push(character);
                } else {
                    throw new IllegalArgumentException("El caracter '" + character + "' es inválido");
                }
            }
            while (!operatorStack.isEmpty()) {
                applyOperation(operatorStack.pop(), operandStack);
            }
            if (operandStack.size() != 1 || !operatorStack.isEmpty()) {
                throw new IllegalArgumentException("Expresión aritmética inválida");
            }
            return operandStack.pop();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inválido en la expresión");
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }


    public void applyOperation(char operator, Stack<Double> operandStack) {
        double b = operandStack.pop();
        double a = operandStack.pop();
        double result = switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) {
                    throw new IllegalArgumentException("División por cero");
                }
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Operador no válido: " + operator);
        };
        operandStack.push(result);
    }
    public void inputExpression(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la expresión:");
        this.expression = scanner.nextLine();
    }
    public void arithmeticExpressionsCalculator(){
        this.inputExpression();
        System.out.println(this.expression);
        this.expression = this.expression.replaceAll(" ", "");
        this.expression = replaceVariables();
        try {
            double result = evaluateExpression();
            System.out.println("Resultado: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}