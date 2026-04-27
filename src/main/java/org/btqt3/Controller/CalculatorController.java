package org.btqt3.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CalculatorController {

    @FXML
    private TextField display;

    private double num1 = 0;
    private String operator = "";
    private boolean start = true;

    @FXML
    private void handleButton(javafx.event.ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        switch (value) {
            case "AC":
                display.setText("0");
                num1 = 0;
                operator = "";
                start = true;
                break;

            case "Del":
                String text = display.getText();
                if (text.length() > 1)
                    display.setText(text.substring(0, text.length() - 1));
                else
                    display.setText("0");
                break;

            case "+": case "-": case "*": case "/":
                num1 = Double.parseDouble(display.getText());
                operator = value;
                start = true;
                break;

            case "=":
                double num2 = Double.parseDouble(display.getText());
                double result = calculate(num1, num2, operator);
                display.setText(String.valueOf(result));
                start = true;
                break;

            default:
                if (start) {
                    display.setText(value);
                    start = false;
                } else {
                    display.setText(display.getText() + value);
                }
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b != 0 ? a / b : 0;
            default: return 0;
        }
    }
}
