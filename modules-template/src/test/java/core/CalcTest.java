package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalcTest {

    private static void checkCalc(Calc calc, double... operands) {
        Assertions.assertEquals(operands.length, calc.getOperandCount(), "Wrong operand count");
        for (int i = 0; i < operands.length; i++) {
            Assertions.assertEquals(operands[i], calc.peekOperand(i), "Wrong value at #" + i + " of operand stack");
        }
    }

    @Test
    public void testCalc() {
        checkCalc(new Calc());
        checkCalc(new Calc(1.0), 1.0);
        checkCalc(new Calc(3.14, 1.0), 1.0, 3.14);
    }

    @Test
    public void testPushOperand() {
        Calc calc = new Calc();
        calc.pushOperand(1.0);
        checkCalc(calc, 1.0);
        calc.pushOperand(3.14);
        checkCalc(calc, 3.14, 1.0);
    }

    @Test
    public void testPopOperand() {
        Calc calc = new Calc(1.0, 3.14);
        Assertions.assertEquals(3.14, calc.popOperand());
        checkCalc(calc, 1.0);
        Assertions.assertEquals(1.0, calc.popOperand());
        checkCalc(calc);
    }
}
