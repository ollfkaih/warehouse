package app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Calc {
    
    private List<Double> operandStack = new ArrayList<>();

    public Calc(Double... operands) {
        operandStack.addAll(List.of(operands));
    }

    public int getOperandCount() {
        return operandStack.size();
    }

    public void pushOperand(int n, double d) {
        operandStack.add(operandStack.size() - n, d);
    }

    public void pushOperand(double d) {
        pushOperand(0, d);
    }

    public double peekOperand(int n) {
        return operandStack.get(operandStack.size() - n - 1);
    }

    public double peekOperand() {
        return peekOperand(0);
    }

    public double popOperand(int n) {
        return operandStack.remove(operandStack.size() - n - 1);
    }

    public double popOperand() {
        return popOperand(0);
    }

    public double performOperation(UnaryOperator<Double> op) {
        var op1 = popOperand();
        var result = op.apply(op1);
        pushOperand(result);
        return result;
    }

    public double performOperation(BinaryOperator<Double> op) {
        var op1 = popOperand();
        var op2 = popOperand();
        var result = op.apply(op1, op2);
        pushOperand(result);
        return result;
    }

    public void swap() {
        var op1 = popOperand();
        var op2 = popOperand();
        pushOperand(op1);
        pushOperand(op2);
    }

    public void dup() {
        pushOperand(peekOperand());
    }
}