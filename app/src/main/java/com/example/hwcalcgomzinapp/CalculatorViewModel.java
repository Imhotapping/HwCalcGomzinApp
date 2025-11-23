package com.example.hwcalcgomzinapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Stack;

public class CalculatorViewModel extends ViewModel {
    private MutableLiveData<String> displayValue = new MutableLiveData<>("0");
    private StringBuilder currentInput = new StringBuilder();
    private String pendingOperation = "";
    private double memory = 0;
    private boolean isNewInput = true;
    private double previousResult = 0;
    private boolean useFormulaMode = false;

    public LiveData<String> getDisplayValue() {
        return displayValue;
    }

    public void appendNumber(String number) {
        if (useFormulaMode) {
            // В режиме формулы просто добавляем цифры
            if (currentInput.length() == 0 || isNewInput) {
                currentInput.setLength(0);
                isNewInput = false;
            }
            currentInput.append(number);
            displayValue.setValue(currentInput.toString());
        } else {
            // Обычный режим (старая логика)
            if (isNewInput || currentInput.length() == 0) {
                currentInput.setLength(0);
                isNewInput = false;
            }

            if (number.equals("0") && currentInput.length() == 1 && currentInput.toString().equals("0")) {
                return;
            }

            currentInput.append(number);
            displayValue.setValue(currentInput.toString());
        }
    }

    public void setOperation(String operation) {
        if (useFormulaMode) {
            // В режиме формулы добавляем операцию к выражению
            if (currentInput.length() == 0) {
                // Если выражение пустое, начинаем с 0
                currentInput.append("0");
            }

            // Проверяем, не является ли последний символ операцией
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            if (isOperator(lastChar)) {
                // Заменяем последнюю операцию
                currentInput.setLength(currentInput.length() - 1);
            }

            currentInput.append(operation);
            displayValue.setValue(currentInput.toString());
            isNewInput = false;
        } else {
            // Обычный режим (старая логика)
            if (currentInput.length() == 0) return;

            if (!pendingOperation.isEmpty() && !isNewInput) {
                calculate();
            }

            pendingOperation = operation;
            previousResult = Double.parseDouble(currentInput.toString());
            isNewInput = true;
        }
    }

    public void calculate() {
        if (useFormulaMode) {
            // Режим формулы: вычисляем все выражение
            if (currentInput.length() == 0) return;

            try {
                double result = evaluateExpression(currentInput.toString());
                String resultStr = formatResult(result);
                displayValue.setValue(resultStr);
                currentInput.setLength(0);
                currentInput.append(resultStr);
                isNewInput = true;
            } catch (Exception e) {
                displayValue.setValue("Error");
                currentInput.setLength(0);
                isNewInput = true;
            }
        } else {
            // Обычный режим (старая логика)
            if (pendingOperation.isEmpty() || currentInput.length() == 0) return;

            double currentValue = Double.parseDouble(currentInput.toString());
            double result = previousResult;

            switch (pendingOperation) {
                case "+":
                    result += currentValue;
                    break;
                case "-":
                    result -= currentValue;
                    break;
                case "*":
                    result *= currentValue;
                    break;
                case "/":
                    if (currentValue != 0) {
                        result /= currentValue;
                    } else {
                        displayValue.setValue("Error");
                        clear();
                        return;
                    }
                    break;
            }

            String resultStr = formatResult(result);
            displayValue.setValue(resultStr);
            currentInput.setLength(0);
            currentInput.append(resultStr);
            pendingOperation = "";
            isNewInput = true;
            previousResult = result;
        }
    }

    // Вспомогательные методы для режима формулы
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private double evaluateExpression(String expression) {
        // Удаляем пробелы и добавляем разделители
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                // Собираем число
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (isOperator(c)) {
                // Обрабатываем операторы с учетом приоритета
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        // Применяем оставшиеся операции
        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b != 0) {
                    return a / b;
                } else {
                    throw new ArithmeticException("Division by zero");
                }
        }
        return 0;
    }

    private String formatResult(double result) {
        if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY || Double.isNaN(result)) {
            return "Error";
        }

        String resultStr = String.valueOf(result);
        if (resultStr.endsWith(".0")) {
            resultStr = resultStr.substring(0, resultStr.length() - 2);
        }
        return resultStr;
    }

    public void memoryAdd() {
        if (currentInput.length() > 0) {
            try {
                double value = useFormulaMode ?
                        evaluateExpression(currentInput.toString()) :
                        Double.parseDouble(currentInput.toString());
                memory += value;
            } catch (Exception e) {
                // Игнорируем ошибки при добавлении в память
            }
        }
    }

    public void memoryRecall() {
        if (memory != 0) {
            String memoryStr = formatResult(memory);
            if (useFormulaMode) {
                // В режиме формулы добавляем значение памяти к текущему выражению
                currentInput.append(memoryStr);
                displayValue.setValue(currentInput.toString());
                isNewInput = false;
            } else {
                // В обычном режиме заменяем текущее значение
                displayValue.setValue(memoryStr);
                currentInput.setLength(0);
                currentInput.append(memoryStr);
                isNewInput = true;
            }
        }
    }

    public void memoryClear() {
        memory = 0;
    }

    public void clear() {
        currentInput.setLength(0);
        displayValue.setValue("0");
        if (!useFormulaMode) {
            pendingOperation = "";
            previousResult = 0;
        }
        isNewInput = true;
    }

    public void setUseFormulaMode(boolean useFormulaMode) {
        if (this.useFormulaMode != useFormulaMode) {
            this.useFormulaMode = useFormulaMode;
            clear(); // Очищаем при смене режима
        }
    }

    public boolean isUseFormulaMode() {
        return useFormulaMode;
    }

    public void addDecimalPoint() {
        if (useFormulaMode) {
            // В режиме формулы просто добавляем точку
            if (isNewInput || currentInput.length() == 0) {
                currentInput.setLength(0);
                currentInput.append("0.");
                isNewInput = false;
            } else if (!currentInput.toString().contains(".") ||
                    hasOperatorBeforeLastNumber(currentInput.toString())) {
                currentInput.append(".");
            }
            displayValue.setValue(currentInput.toString());
        } else {
            // Обычный режим (старая логика)
            if (isNewInput) {
                currentInput.setLength(0);
                currentInput.append("0.");
                isNewInput = false;
            } else if (!currentInput.toString().contains(".")) {
                currentInput.append(".");
            }
            displayValue.setValue(currentInput.toString());
        }
    }

    private boolean hasOperatorBeforeLastNumber(String expression) {
        // Проверяем, есть ли оператор перед последним числом
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (isOperator(c)) {
                return true;
            }
        }
        return false;
    }
}