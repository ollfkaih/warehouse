package app;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;

public class AppController {

    private Calc calc;

    public AppController() {
        calc = new Calc(0.0, 0.0, 0.0);
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
        updateOperandsView();
    }

    @FXML
    private ListView<Double> operandsView;

    @FXML
    private Label operandView;

    @FXML
    void initialize() {
        updateOperandsView();
    }

    private int minOperandCount = 2;

    private void updateOperandsView() {
        while (calc.getOperandCount() < minOperandCount) {
            calc.pushOperand(calc.getOperandCount(), 0.0);
        }
        List<Double> operands = operandsView.getItems();
        operands.clear();
        for (int i = 0; i < minOperandCount; i++) {
            operands.add(calc.peekOperand(minOperandCount - i - 1));
        }
    }

    private String getOperandString() {
        return operandView.getText();
    }

    private boolean hasOperand() {
        return ! getOperandString().isBlank();
    }

    private double getOperand() {
        return Double.valueOf(operandView.getText());
    }
    
    private void setOperand(String operandString) {
        operandView.setText(operandString);
    }

    private void setOperand(double d) {
        setOperand(Double.toString(d));
    }

    private void clearOperand() {
        setOperand("");
    }    

    @FXML
    void handleEnter() {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        } else {
            calc.dup();
        }    
        clearOperand();
        updateOperandsView();
    }    

    private void appendToOperand(String s) {
        setOperand(operandView.getText() + s);
    }

    @FXML
    void handleDigit(ActionEvent ae) {
        if (ae.getSource() instanceof Labeled l) {
            appendToOperand(l.getText());
        }
    }

    @FXML
    void handlePoint() {
        var operandString = getOperandString();
        if (operandString.contains(".")) {
            setOperand(operandString.substring(0, operandString.indexOf(".") + 1));
        } else {
            appendToOperand(".");
        }
    }

    @FXML
    void handleClear() {
        clearOperand();
    }

    private void withOperand(Runnable proc) {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
            clearOperand();
        }
        proc.run();
        updateOperandsView();
    }

    private void performOperation(UnaryOperator<Double> op) {
        withOperand(() -> calc.performOperation(op));
    }

    private void performOperation(boolean swap, BinaryOperator<Double> op) {
        withOperand(() -> {
            if (swap) {
                calc.swap();
            }
            calc.performOperation(op);
        });
    }

    @FXML
    void handleOpAdd() {
        performOperation(false, (op1, op2) -> op1 + op2);
    }

    @FXML
    void handleOpSub() {
        performOperation(true, (op1, op2) -> op1 - op2);
    }

    @FXML
    void handleOpMult() {
        performOperation(false, (op1, op2) -> op1 * op2);
    }

    @FXML
    void handleOpDiv() {
        performOperation(true, (op1, op2) -> op1 / op2);
    }

    @FXML
    void handleOpSquareRoot() {
        performOperation(op1 -> Math.sqrt(op1));
    }

    @FXML
    void handlePi() {
        withOperand(() -> calc.pushOperand(Math.PI));
    }

    public static void main(String[] args) {
        System.out.println("\u221A");
    }
}
