package app;

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
    public void testPeekOperand() {
        Calc calc = new Calc(1.0, 3.14);
        Assertions.assertEquals(3.14, calc.peekOperand());
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Calc().peekOperand());
    }

    @Test
    public void testPeekOperandN() {
        Calc calc = new Calc(1.0, 3.14);
        Assertions.assertEquals(3.14, calc.peekOperand(0));
        Assertions.assertEquals(1.0, calc.peekOperand(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.peekOperand(2));
    }

    @Test
    public void testPopOperand() {
        Calc calc = new Calc(1.0, 3.14);
        Assertions.assertEquals(3.14, calc.popOperand());
        checkCalc(calc, 1.0);
        Assertions.assertEquals(1.0, calc.popOperand());
        checkCalc(calc);
    }

    @Test
    public void testPopOperand_emptyStack() {
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc().popOperand());
    }

    @Test
    public void testPerformOperation1() {
        Calc calc = new Calc(1.0);
        Assertions.assertEquals(-1.0, calc.performOperation(n -> -n));
        checkCalc(calc, -1.0);
    }

    @Test
    public void testPerformOperation1_emptyOperandStack() {
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc().performOperation(n -> -n));
    }


    @Test
    public void testPerformOperation2() {
        Calc calc = new Calc(1.0, 3.0);
        Assertions.assertEquals(-2.0, calc.performOperation((n1, n2) -> n1 - n2));
        checkCalc(calc, -2.0);
    }

    @Test
    public void testPerformOperation2_lessThanTwoOperands() {
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc(1.0).performOperation((n1, n2) -> n1 - n2));
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc().performOperation((n1, n2) -> n1 - n2));
    }

    @Test
    public void testSwap() {
        Calc calc = new Calc(1.0, 3.14);
        checkCalc(calc, 3.14, 1.0);
        calc.swap();
        checkCalc(calc, 1.0, 3.14);
        calc.swap();
        checkCalc(calc, 3.14, 1.0);
    }

    @Test
    public void testSwap_lessThanTwoOperands() {
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc(1.0).swap());
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc().swap());
    }

    @Test
    public void testDup() {
        Calc calc = new Calc(1.0, 3.14);
        Assertions.assertEquals(3.14, calc.popOperand());
        checkCalc(calc, 1.0);
        Assertions.assertEquals(1.0, calc.popOperand());
        checkCalc(calc);
    }

    @Test
    public void testDup_emptyOperandStack() {
        Assertions.assertThrows(IllegalStateException.class, () -> new Calc().dup());
    }
}
