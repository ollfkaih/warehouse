package app;

import java.util.List;
import java.util.function.BinaryOperator;
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

    private void updateOperandsView() {
        List<Double> operands = operandsView.getItems();
        operands.clear();
        int elementCount = Math.min(calc.getOperandCount(), 3);
        for (int i = 0; i < elementCount; i++) {
            operands.add(calc.peekOperand(elementCount - i - 1));
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

    @FXML
    void handleEnter() {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        } else {
            calc.dup();
        }
        setOperand("");
        updateOperandsView();
    }

    private void appendToOperand(String s) {
        // TODO
    }

    @FXML
    void handleDigit(ActionEvent ae) {
        if (ae.getSource() instanceof Labeled l) {
            // TODO append button label to operand
        }
    }

    @FXML
    void handlePoint() {
        var operandString = getOperandString();
        if (operandString.contains(".")) {
            // TODO remove characters after point
        } else {
            // TODO append point
        }
    }

    @FXML
    void handleClear() {
        // TODO clear operand
    }

    @FXML
    void handleSwap() {
        // TODO clear operand
    }

    private void performOperation(UnaryOperator<Double> op) {
        // TODO
    }

    private void performOperation(boolean swap, BinaryOperator<Double> op) {
        if (hasOperand()) {
            // TODO push operand first
        }
        // TODO perform operation, but swap first if needed
    }

    @FXML
    void handleOpAdd() {
        // TODO
    }

    @FXML
    void handleOpSub() {
        // TODO
    }

    @FXML
    void handleOpMult() {
        // TODO
    }
}
