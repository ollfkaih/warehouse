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

    /**
     * @return the number of operands on the stack
     */
    public int getOperandCount() {
        return operandStack.size();
    }

    /**
     * Pushes a new operand into the n'th place from the top.
     * @param n the place to push
     * @param d the new operand
     */
    public void pushOperand(int n, double d) {
        operandStack.add(operandStack.size() - n, d);
    }

    /**
     * Pushes a new operand onto top of the stack.
     * @param d the new operand
     */
    public void pushOperand(double d) {
        pushOperand(0, d);
    }

    /**
     * @param n the place (from the top) to peek
     * @return the n'th operand from the top
     */
    public double peekOperand(int n) {
        return operandStack.get(operandStack.size() - n - 1);
    }

    /**
     * @return the top operand
     */
    public double peekOperand() {
        return peekOperand(0);
    }

    /**
     * Removes and returns the n'th operand from the top.
     * @param n the place from the top to remove
     * @return the n'th operand from the top
     */
    public double popOperand(int n) {
        return operandStack.remove(operandStack.size() - n - 1);
    }

    /**
     * Removes and returns the top operand.
     * @return the top operand
     */
    public double popOperand() {
        return popOperand(0);
    }

    /**
     * Performs the provided operation in the top operand, and
     * replaces it with the result.
     * @param op the operation to perform
     * @return the result of performing the operation
     */
    public double performOperation(UnaryOperator<Double> op) {
        // TODO
        return 0.0;
    }

    /**
     * Performs the provided operation in the two topmost operands, and
     * replaces them with the result.
     * @param op the operation to perform
     * @return the result of performing the operation
     */
    public double performOperation(BinaryOperator<Double> op) {
        var op1 = popOperand();
        var op2 = popOperand();
        var result = op.apply(op1, op2);
        pushOperand(result);
        return result;
    }

    /**
     * Swaps the two topmost operands.
     */
    public void swap() {
        // TODO
    }

    /**
     * Duplicates the top operand.
     */
    public void dup() {
        // TODO
    }
}