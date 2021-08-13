# Tests for the RPN calculator

This folder/package contains tests based on TestFX for the RPN Calculator (currently only one test class).

As can be seen when launching, the app contains a list (top) showing the operands
(topmost operand at the bottom), a text field (below list, initially empty) for a new operand and
the buttons for digits, enter, decimal point, operations etc.

## What is tested

The tests simulate clicks on the buttons and checks that the underlying Calc object,
the list (a view of the Calc object's operand stack) and the text field are updated as expected.
E.g. if you click buttons `2 3 . 5` the string `23.5` should be shown,
while the list is not affected. If you then click `enter`, the text field should be emptied, the operand stack should have `23.5` at the top and the list should have `23.5` at the bottom 
(logically the top of the operand stack).

Below are the specific cases that are tested.

buttons to click `=>` text field content:

- `2 7` => `27`
- `2 7 .` => `27.`
- `2 7 . 5` => `27.5`
- `2 7 . 5 .` => `27.` (cut at decimal point)

buttons to click `=>` operand stack/list content (from the bottom):

- `2 7 . 5 enter"` => `27.5`
- `2 7 enter` => `27.0"`
- `2 enter 7 enter 5 enter` => `5.0 7.0 2.0` 
- `2 7 . enter` => `27.0` 
- `2 7 . 5 enter` => `27.5` 
- `2 enter 7 +` => `9.0` 
- `2 enter 7 -` => `-5.0` 
- `2 enter 7 *` => `14.0` 
- `6 enter 3 /` => `2.0` 
- `2 5 enter √` => `5.0` 
- `π` => `3.1415...` (the value of the `Math.PI` constant)
