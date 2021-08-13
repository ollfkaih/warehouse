package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

/**
 * TestFX App test
 */
public class AppTest extends ApplicationTest {

    private AppController controller;
    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("App.fxml"));
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public Parent getRootNode() {
        return root;
    }

    private String enterLabel = """
        E
        n
        t
        e
        r
        """.stripTrailing();

    private void click(String... labels) {
        for (var label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }

    private String getOperandString() {
        return ((Label) getRootNode().lookup("#operandView")).getText();
    }

    private ListView<Double> getOperandsView() {
        return (ListView<Double>) getRootNode().lookup("#operandsView");
    }

    private void checkView(double... operands) {
        for (int i = 0; i < operands.length; i++) {
            Assertions.assertEquals(operands[i], controller.getCalc().peekOperand(i), "Wrong value at #" + i + " of operand stack");
        }
        List<Double> viewItems = getOperandsView().getItems();
        for (int i = 0; i < operands.length; i++) {
            Assertions.assertEquals(operands[i], viewItems.get(viewItems.size() - i - 1), "Wrong value at #" + i + " of operands view");
        }
    }

    private void checkView(String operandString, double... operands) {
        Assertions.assertEquals(operandString, getOperandString());
        checkView(operands);
    }

    // see https://www.baeldung.com/parameterized-tests-junit-5
    // about @ParameterizedTest

    @ParameterizedTest
    @MethodSource
    public void testClicksOperand(String labels, String operandString) {
        for (var label : labels.split(" ")) {
            click(label);
        }
        checkView(operandString);
    }

    private static Stream<Arguments> testClicksOperand() {
        return Stream.of(
            Arguments.of("2 7", "27"),
            Arguments.of("2 7 .", "27."),
            Arguments.of("2 7 . 5", "27.5"),
            Arguments.of("2 7 . 5 .", "27.")
        );
    }
    
    @ParameterizedTest
    @MethodSource
    public void testClicksOperands(String labels, String operandsString) {
        for (var label : labels.split(" ")) {
            click(label.equals("\n") ? enterLabel : label);
        }
        checkView("", Stream.of(operandsString.split(" ")).mapToDouble(Double::valueOf).toArray());
    }

    private static Stream<Arguments> testClicksOperands() {
        return Stream.of(
            Arguments.of("2 7 . 5 \n", "27.5"),
            Arguments.of("2 7 \n", "27.0"),
            Arguments.of("2 \n 7 \n 5 \n", "5.0", "7.0", "2.0"),
            Arguments.of("2 7 . \n", "27.0"),
            Arguments.of("2 7 . 5 \n", "27.5"),
            Arguments.of("2 \n 7 +", "9.0"),
            Arguments.of("2 \n 7 -", "-5.0"),
            Arguments.of("2 \n 7 *", "14.0"),
            Arguments.of("6 \n 3 /", "2.0"),
            Arguments.of("2 5 \n √", "5.0")
        );
    }

    @Test
    public void testPi() {
        click("π");
        checkView("", Math.PI);
    }
}
