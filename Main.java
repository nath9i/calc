import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InputOperandException, InputExpressionException, CalcRomansException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение с двумя операндами и одним оператором (+, -, *, /):");
        String input = scan.nextLine();
        scan.close();
        System.out.println();
        System.out.println("Результат операции:");
        System.out.println(calc(input));
}

    public static String calc(String input) throws InputExpressionException, InputOperandException, CalcRomansException {
        Expression expressionData = getExpressionData(input);
        int output = calculator(expressionData);
        if (expressionData.isRoman){;
            RomanNumber romanValue = RomanNumber.getRomanByValue(output);
            if (romanValue == null) { throw new CalcRomansException("Ошибка операции: значение в римской системе счисления не найдено");}
            return romanValue.toString();
        } else { return String.valueOf(output); }
    }

    static Expression getExpressionData(String input) throws InputExpressionException, InputOperandException {
        String[] operandsS = input.split("[-+/*]");
        if (operandsS.length != 2) {throw new InputExpressionException("Ошибка ввода: недопустимый формат выражения");}

        Operand a = getOperand(operandsS[0]);
        Operand b = getOperand(operandsS[1]);
        if (a.isRoman != b.isRoman) {throw new InputExpressionException("Ошибка ввода: использование различных систем счисления");}

        Expression exp = new Expression();
        exp.isRoman = a.isRoman;
        exp.operation = getOperator(input);
        exp.a = a.value;
        exp.b = b.value;

        return exp;
    }

    static Operand getOperand(String input) throws InputOperandException{
        Operand a = new Operand();
        a.isRoman = false;
        try {
            a.value = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            try {
                RomanNumber romanValue = RomanNumber.valueOf(input.trim());
                a.isRoman = true;
                a.value = romanValue.value;
            } catch (IllegalArgumentException e1) {
                throw new InputOperandException();
            }
        }
        if (a.value < 1 || a.value > 10) { throw new InputOperandException();}
        return a;
    }

    static char getOperator(String input) {
        if (input.contains("+")) { return '+';}
        else if (input.contains("-")) { return '-';}
        else if (input.contains("*")) { return '*';}
        else { return '/';}
    }

    static int calculator(Expression data) throws CalcRomansException{
        switch (data.operation) {
            case '+':
                data.a += data.b;
                break;
            case '-':
                data.a -= data.b;
                break;
            case '*':
                data.a *= data.b;
                break;
            case '/':
                data.a /= data.b;
                break;
        }
        if (data.a < 1 && data.isRoman) { throw new CalcRomansException("Ошибка операции: в римской системе счисления нет чисел меньше 1"); }
        return data.a;
    }
}

class Operand{
    boolean isRoman;
    int value;
}

class Expression{
    boolean isRoman;
    char operation;
    int a, b;
}

class InputExpressionException extends Exception {
    InputExpressionException(String description){
        super (description);
    }
}

class InputOperandException extends Exception {
    InputOperandException(){
        super ("Ошибка ввода: по крайней мере один из операндов не удовлетворяет условиям задачи");
    }
}

class CalcRomansException extends Exception {
    CalcRomansException(String description) {
        super (description);
    }
}