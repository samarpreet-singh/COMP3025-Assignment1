package ca.georgiancollege.comp3025_assignment1

import android.util.Log
import android.view.View
import ca.georgiancollege.comp3025_assignment1.databinding.ActivityMainBinding
import java.lang.IllegalArgumentException
import java.util.Stack

/*
File name: Calculator.kt
Author's name: Samarpreet Singh
StudentID: 200510621
Date: 2024-02-18
App Description: Calculator app functionality (part 2). The goal with this app is to build a fully functioning "simple" calculator in Kotlin.
Version History -> https://github.com/samarpreet-singh/COMP3025-Assignment1
 */
/**
 * This Calculator class is responsible for handling all the functionings of our calculator app
 */
class Calculator(binding: ActivityMainBinding)
{
    private var m_resultLabelValue: String
    private var m_binding: ActivityMainBinding // member variable underscore. In c++ this means it's a pointer and you cant use it unless the object you are pointing to is a pointer

    init
    {
        this.m_binding = binding
        this.m_resultLabelValue = "0" // initializing to 0 to make handling 0 edge cases easier

        initializeOnClickListeners()
    }

    // Since we use stored unicode values in strings.xml for button texts, we have to convert them to strings and assign them to vals to work with them
    private val unicodeAdd: String = this.m_binding.addButton.text.toString()
    private val unicodeSubtract: String = this.m_binding.subtractButton.text.toString()
    private val unicodeMultiply: String = this.m_binding.multiplyButton.text.toString()
    private val unicodeDivide: String = this.m_binding.divideButton.text.toString()
    private val unicodePercent: String = this.m_binding.percentButton.text.toString()

    // onClickListener initializer for all buttons
    private fun initializeOnClickListeners()
    {
        // operator buttons
        this.m_binding.multiplyButton.setOnClickListener { view -> processOperatorButtons(view) }
        this.m_binding.divideButton.setOnClickListener { view -> processOperatorButtons(view) }
        this.m_binding.addButton.setOnClickListener { view -> processOperatorButtons(view) }
        this.m_binding.subtractButton.setOnClickListener { view -> processOperatorButtons(view) }
        this.m_binding.percentButton.setOnClickListener { view -> processOperatorButtons(view) }

        // extra buttons
        this.m_binding.clearButton.setOnClickListener { view -> processExtraButtons(view) }
        this.m_binding.backspaceButton.setOnClickListener { view -> processExtraButtons(view) }
        this.m_binding.plusMinusButton.setOnClickListener { view -> processExtraButtons(view) }
        this.m_binding.equalsButton.setOnClickListener { view -> processExtraButtons(view) }

        // number buttons
        this.m_binding.zeroButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.oneButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.twoButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.threeButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.fourButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.fiveButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.sixButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.sevenButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.eightButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.nineButton.setOnClickListener { view -> processNumberButtons(view) }
        this.m_binding.decimalButton.setOnClickListener { view -> processNumberButtons(view) }
    }

    /**
     * Detects which operator button has been pressed through the buttons "tag",
     * and appends that to the resultTextView PROVIDED the expression does not already end with an operator,
     * in which case the previous operator is replaced by the new one.
     *
     * performCalculations() call to simulate the "Equals" button functionality for the piece of expression coming
     * before the latest operator button is clicked. Check performCalculations() javadoc for more information.
     *
     * @param view -> The operator button's view to be passed in
     */
    private fun processOperatorButtons(view: View)
    {
        // using the pressed button's tag to assign the specific unicode button's text to operator val
        val operator = when (view.tag.toString())
        {
            "add" -> unicodeAdd
            "multiply" -> unicodeMultiply
            "divide" -> unicodeDivide
            "subtract" -> unicodeSubtract
            "percent" -> unicodePercent
            else -> "" // else condition not necessary since user can only press one of the above buttons but still best practice
        }

        // empty check for following best practices
        if (operator.isNotEmpty())
        {
            // The Regex expression checks if the given string ENDS with an operator (unicode in this case since we are using unicode)
            if (this.m_resultLabelValue.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide]\\s*$")))
            {
                // if it ends with an operator already and the user pressed another operator, drop the last operator to replace it with the new one
                this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1)
            }

            // append the new operator to the resultLabelValue and then assign to resultTextView
            this.m_resultLabelValue += operator
            this.m_binding.resultTextView.text = this.m_resultLabelValue

            //print statement for debugging
            println("Operator: " + this.m_resultLabelValue)

            // calling performCalculations to evaluate the previous operation IN CASE it exists
            performCalculations()
        }
    }

    /**
     * Detects which "Extra" button has been pressed through the tag, and performs necessary operations for each case.
     *
     * @param view -> The "view" pressed button object.
     */
    private fun processExtraButtons(view: View)
    {
        when (view.tag.toString())
        {
            //when backspace is pressed,
            "backspace" ->
            {
                // delete the last character from the resultLabelView and assign the new value to the resultTextView
                this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1)
                this.m_binding.resultTextView.text = this.m_resultLabelValue

                // if the user backspaces all the way and the resultLabelView is completely empty now OR it is containing the "-" from a negative number,
                if (this.m_resultLabelValue.isEmpty() || this.m_resultLabelValue == "-")
                {
                    // set the resultLabelView to be 0 instead of an empty string being shown to the user
                    this.m_resultLabelValue = "0"
                    this.m_binding.resultTextView.text = "0"
                }

            }
            // when "C" (the clear button) is pressed,
            "clear" ->
            {
                // set the resultLabelValue and the resultTextView text to be 0
                this.m_resultLabelValue ="0"
                this.m_binding.resultTextView.text = "0"
            }
            // when "+/-" is pressed,
            "plus_minus" ->
            {
                // and the resultLabelValue isNotEmpty,
                if (this.m_resultLabelValue.isNotEmpty())
                {
                    // check if it already contains a minus sign (This does NOT check for the unicode - sign. Instead checks for the standard - sign from the keyboard)
                    // That's how it still works even when the minus operator has been pressed and there is already a unicode minus in the string
                    if (this.m_resultLabelValue.contains("-"))
                    {
                        // remove the already existing minus to change the expression to positive
                        this.m_resultLabelValue = this.m_resultLabelValue.removePrefix("-")
                    }
                    else
                    {
                        // otherwise negate the expression
                        this.m_resultLabelValue = "-" + this.m_resultLabelValue
                    }
                    this.m_binding.resultTextView.text = this.m_resultLabelValue
                }
            }
            "=" ->
            {
                // performCalculations code used to be a part of the equals case here. However, I extracted it into its own function when I realized that I could expand upon the same code to handle cases whenever user presses operator buttons and wants to evaluate the string right before the latest pressed operator button. Check performCalculations for more information.
                performCalculations()
            }
        }
    }

    /**
     * Responsible for handling all number button inputs and handling related edge cases.
     *
     * uses UnicodeOperatorsFormatter to convert unicode values for operator buttons into proper operator text (+, -, /, *) so compiler can understand it.
     *
     * @param view -> The pressed number button's view
     */
    private fun processNumberButtons(view: View)
    {
        // Check unicodeOperatorsFormatter for more information
        // uses unicodeOperatorsFormatter to get a "cleaned" string which can be understood by the compiler.
        val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue)

        // keeping track of the last element in the string is essential for handling edge cases as seen below.
        // unicodeOpertorsFormatter adds whitespaces before and after each operator, which is taken advantage of here to split the expression and get the last element
        val lastElement = if (cleanedExpression.isNotEmpty()) cleanedExpression.split(" ").last() else ""

        when (view.tag.toString())
        {
            // when 0 is pressed,
            "0" ->
            {
                // if the resultTextView (or resultLabelValue) is already 0, we do NOT want to append more 0s to the textView
                if (this.m_resultLabelValue == "0")
                {
                    this.m_resultLabelValue = "0"
                    this.m_binding.resultTextView.text = this.m_resultLabelValue
                }
                // if the last element in the string is 0 already, no more 0s should be added!
                // for example -> 9+5+0 in this case, the expression already ends with a 0 and we don't want more zeroes to be added to it!!
                else if (lastElement == "0")
                {
                    // handle the error and show it in logcat!
                    Log.e("Last Element Zero", "The last element in the expression cannot be multiple zeroes!")
                }
                // otherwise append the 0 to the resultLabelValue
                else
                {
                    this.m_resultLabelValue += view.tag.toString()
                }
            }
            // when the decimal is clicked,
            "." ->
            {
                // if the last element is already NOT a decimal, append the decimal to the resultLabelView
                if(!lastElement.contains("."))
                {
                    this.m_resultLabelValue += view.tag.toString()
                }
            }
            // if it is any other normal number that's entered
            else ->
            {
                // if the resultLabelView is already at 0, no need to append the new number, instead replace the 0
                if (this.m_resultLabelValue == "0")
                {
                    this.m_resultLabelValue = view.tag.toString()
                }
                // else if the last element is a zero, then drop that zero and replace it with the newly pressed number.
                // for example -> 9+6+0 now when a new number is pressed, let's say 3, it should take the place of 0 instead of being appended like 03
                // -> 9+6+3
                else if (lastElement == "0")
                {
                    this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1).plus(view.tag.toString())
                }
                // finally, for all other normal cases, just append the newly pressed number
                else
                {
                    this.m_resultLabelValue += view.tag.toString()
                }
            }
        }

        // set the resultLabelView to the resultTextView
        this.m_binding.resultTextView.text = this.m_resultLabelValue
        println("Number: " + this.m_resultLabelValue)
    }

    /* Evaluation function chain begins */

    /**
     * Handles the preparations for evaluation when the equals button is pressed to evaluate the whole string, AND when an operator button is pressed to evaluate
     * only the string before the pressed operator button.
     *
     * This function's code originally belonged to the "=" case in processExtraButtons but was extracted out here to work with evaluation in both of the above
     * mentioned cases.
     */
    private fun performCalculations(){
        // equals button will usually only be pressed when the proper full expression has been entered, example 9+3-2 and not 9+3-
        // if the resultLabelValue is not empty AND it does NOT end with an operator,
        if (this.m_resultLabelValue.isNotEmpty() &&
            !this.m_resultLabelValue.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide]\\s*$")))
        {
            val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue) // get the text string with the whitespaces and formatting

            // check evaluateExpression documentation for more information about it.
            val result = evaluateExpression(cleanedExpression)

            // if the result is an integer after calculations are performed, we need to format it accordingly and not display something like 10.0
            // check convertToIntIfRequired documentation
            this.m_resultLabelValue = convertToIntIfRequired(result)

            // assign the final result to the textView to show to the user
            this.m_binding.resultTextView.text = this.m_resultLabelValue
        }
        // this else if block is to keep evaluating user input as they go and press more operator buttons, just like in real calculators!
        // if the resultLabelView is not empty AND it contains at least ONE proper expression (like 9+6 followed by more characters and not 9+, which is made possible by the Regex),
        else if (this.m_resultLabelValue.isNotEmpty() &&
            this.m_resultLabelValue.matches(Regex(".*[0-9]+[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide][0-9]+.*")))
        {
            // get formatted string again
            val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue)

            // for debugging
            println("Realtime String before drop: " + cleanedExpression)

            // Drop the most recently passed operator because all operands for it have not been passed yet AND we only want to evaluate the string before it
            // dropping last 2 characters here because unicodeOperatorsFormatter adds whitespaces bbefore and after operators and have to drop the extra whitespace too.
            var realtimeEvaluationString = cleanedExpression.dropLast(2) // drop the most recently pressed operator for evaluating the string before that

            // For debugging
            println("Realtime String: " + realtimeEvaluationString)

            // evaluating the string before the latest operator was pressed
            val result = evaluateExpression(realtimeEvaluationString)

            // conversion to int if required
            realtimeEvaluationString = convertToIntIfRequired(result)

            // set the resultTextView with it
            this.m_binding.resultTextView.text = realtimeEvaluationString
        }
        // otherwise, when user just enters an operator and clicks equals, handle the error and display in logcat.
        // For example -> 9+ now this input is wrong because there it is ending in an operator
        else
        {
            // still have to fix wrongful exception trigger on first operator press
            Log.e("Operator Ending", "Invalid expression: Expression cannot end with an operator!")
        }
    }

    /**
     * Function to execute more functions for evaluating an expression.
     *
     * @param expression -> the expression to be evaluated
     * @return Double -> the evaluated result
     */
    private fun evaluateExpression(expression: String): Double {
        // for debugging
        println("Given Expression: " + expression)

        // infixToPostfix is using a custom implementation of the Shunting Yard Algorithm to convert the given expression into a postfix expression, check doc for more details.
        val postfixExpression = infixToPostfix(expression)

        // evaluatePostfix evaluates the generated postfix expression. Check doc for more details.
        val result = evaluatePostfix(postfixExpression)

        // For debugging
        println("Result: " + result)

        return result
    }

    /**
     * Custom implementation of the Shunting Yard Algorithm. Utilizes stacks and converts the given expression into a postfix expression.
     *
     * Given expression must have been cleaned up by unicodeOperatorsFormatter() for utilizing whitespace as delimiter.
     * For example ->
     * The infix expression 5+6-7*8/9
     * becomes 5 6 + 7 8 * 9 / -  and then evaluatePostfix() works with this postfix expression to evaluate. Check documentation of evaluatePostfix for more details.
     *
     * returns a List<String> which contains all elements of the infix expression converted into postfix.
     *
     * @param infixExpression -> Given infix expression string MUST BE unicodeFormatted through unicodeOperatorsFormatter.
     * @return List<String> -> postfix expression elements
     */
    private fun infixToPostfix(infixExpression: String): List<String>
    {
        // creating an empty mutable list of strings to store the postfix expression
        val output = mutableListOf<String>()

        // initializing an empty Stack of strings to hold operators for postfix expression creators
        val stack = Stack<String>()

        // .split returns a list of strings separated by the delimiter specified.
        // Whitespace is used as a delimiter since given expression must have undergone unicodeOperatorsFormatter()
        val splitInfixExpressionList = infixExpression.split(" ")

        // splitInfixExpressionList is looped through
        for (element in splitInfixExpressionList)
        {
            when
            {
                // When the element in the list at the current iteration is a number, just add it to the output postfix expression list.
                isNumeric(element) -> output.add(element)

                // if it is an operator,
                isOperator(element) -> {
                    // use a while loop to see if the stack is NOT empty (which means it has at least 1 operator) AND
                    // the the element in question IS an operator (This check is not required because the stack is only meant to hold operators but is best practice)
                    // AND the element has <= PRECEDENCE than the latest operator in the stack (helps in preserving operator precedence),
                    // (Precedence is checked by precedenceChecker(), see documentation for more details).
                    while (stack.isNotEmpty() && isOperator(stack.peek()) &&
                        (precedenceChecker(element) <= precedenceChecker(stack.peek()))
                    ) {
                        // pop the stack, which returns the popped element and gets added to the postfix output list.
                        // This is done because if the operator at the top of the stack has MORE precedence than the current element, we want it to be evaluated FIRST.
                        // that operator with more precedence gets added to the postfix output list and will get evaluated first by evaluatePostfix().
                        output.add(stack.pop())
                    }
                    // The above while loop will not execute when we find the FIRST operator in the string because the stack WILL BE empty at that time,
                    // hence that first element gets pushed to the stack via this line. If the current element being traversed is an operator, this line
                    // ensures that it WILL get added to the stack. The above while loop only works to add the operators with more precedence to the postfix
                    // output list so they get evaluated first.
                    stack.push(element)
                }
            }
        }

        // this step is very important as it ensures that any left over operators in the stack get added to the postfix output list.
        // this happens when there are no more numeric elements to be traversed.
        while (stack.isNotEmpty()) {
            output.add(stack.pop())
        }

        // For debugging
        println("Postfix output: " + output)
        return output

        /*
        Here is an example of how this custom implementation of the algorithm works.

        Given string -> 5+6-7*8/9

        1. First loop iteration ->
        5 is numeric so it gets added to output
        output = ["5"]
        stack = [~empty]

        2. Second loop iteration ->
        + is an operator and the stack is empty so the while loop does not trigger and + gets pushed to the stack
        output = ["5"]
        stack = ["+"]

        3. Third loop iteration ->
        6 is numeric so it gets added to output
        output = ["5", "6"]
        stack = ["+"]

        4. Fourth loop iteration ->
        - is an operator, the stack is NOT empty, and - and + have same precedence so the while loop triggers, and + gets popped and added to output.
        // - is then pushed to the stack.
        output = ["5", "6", "+"]
        stack = ["-"]

        5. Fifth loop iteration ->
        7 is numeric so it gets added to output
        output = ["5", "6", "+", "7"]
        stack = ["-"]

        6. Sixth loop iteration ->
        * is an operator, the stack is NOT empty, and * has MORE precedence than - so the while loop does NOT trigger and so * gets pushed to the stack.
        output = ["5", "6", "+", "7"]
        stack = ["*", "-"]  (Stacks are LIFO so * will get pushed to the TOP of the stack)

        7. Seventh loop iteration ->
        8 is numeric so it gets added to output
        output = ["5", "6", "+", "7", "8"]
        stack = ["*", "-"]

        8. Eighth loop iteration ->
        / is an operator, the stack is NOT empty, and / and * have same precedence so the while loop triggers, and * gets popped and added to output.
        // / is then pushed to the stack.
        output = ["5", "6", "+", "7", "8", "*"]
        stack = ["/", "-"]

        9. Ninth loop iteration ->
        9 is numeric so it gets added to output
        output = ["5", "6", "+", "7", "8", "*", "9']
        stack = ["/", "-"]

        All iterations are complete, but we still have / and - in the stack.
        So the while loop after the for loop is executed and all remaining elements are popped, and added to output
        output = ["5", "6", "+", "7", "8", "*", "9", "/", "-"]
        stack = [~empty]
         */
    }

    /**
     * This function is a custom implementation for evaluating postfix expressions.
     *
     * @param postfixExpression -> The given postfix expression (must have undergone infixToPostfix()) list of strings.
     * @return Double -> the result of the postfix expression evaluation.
     */
    private fun evaluatePostfix(postfixExpression: List<String>): Double
    {
        // (See example after return statement for proper understanding)
        // Initializing a stack of doubles
        val stack = Stack<Double>() // stack plays a different role here than infixToPostfix

        // traverse the given postfixExpression list
        for (element in postfixExpression)
        {
            // if the current iteration's element is numeric, push it to the stack.
            if (isNumeric(element))
            {
                // double conversion made because we want to give accurate results in case of a decimal number
                stack.push(element.toDouble())
            }
            // if it is an operator,
            else if (isOperator(element))
            {
                // initializing the right operand var and left operand val.
                var rightOperand: Double = 0.0

                // now if the element is NOT the percent operator (percent operator effectively multiplies the preceding value (left operand by 0.01, so no need for a right operand in that case!)
                if (element != "%")
                {
                    // pop the stack and assign the value to right operand
                    rightOperand = stack.pop()
                }

                // pop the stack and assign the value to left operand
                val leftOperand = stack.pop()

                // operation performed with leftOperand, element, and rightOperand. The value for right operand is 0.0 which is what we initialized it with in case the percent button has been clicked. Check performOperation for more details.
                val result = performOperation(leftOperand, element, rightOperand)

                //final obtained result is pushed to the stack
                stack.push(result)
            }
        }

        // final obtained result is returned. There should always be a final result in the stack but exception handled for best practice adherence.
        return if (stack.isNotEmpty()) stack.pop() else throw IllegalArgumentException("Invalid expression provided")

        /*
        Here's an example of how this custom implementation works ->

        Taking our generated postfix expression string example from infixToPostfix,
        we have -> 5 6 + 7 8 * 9 / -

        1. First loop iteration ->
        5 is numeric so it gets pushed to the stack of doubles
        stack = [5.0]

        2. Second loop iteration ->
        6 is numeric so it gets pushed to the stack of doubles
        stack = [6.0, 5.0]

        3. Third loop iteration ->
        + is an operator and is not %, so stack gets popped for both left and right operands and right operand gets assigned 6.0, whereas left operand gets assigned 5.0
        performOperation() (see documentation for more details) gets called, evaluates the result of the calculation with + operator, 6.0 left operand, and 5.0 right operand.
        The result val is 11 and is pushed to the stack after performOperation() returns.
        stack = [11.0]

        4. Fourth loop iteration ->
        7 is numeric so it gets pushed to the stack of doubles
        stack = [7.0, 11.0]

        5. Fifth loop iteration ->
        8 is numeric so it gets pushed to the stack of doubles
        stack = [8.0, 7.0, 11.0]

        6. Sixth loop iteration ->
        * is an operator and is not %, so stack gets popped for both left and right operands and right operand gets assigned 8.0, whereas left operand gets assigned 7.0
        performOperation() gets called, evaluates the result of the calculation with * operator, 7.0 left operand, and 8.0 right operand.
        The result val is 56 and is pushed to the stack after performOperation() returns.
        stack = [56.0, 11.0]

        7. Seventh loop iteration ->
        9 is numeric so it gets pushed to the stack of doubles
        stack = [9.0, 56.0, 11.0]

        8. Eighth loop iteration ->
        / is an operator and is not %, so stack gets popped for both left and right operands and right operand gets assigned 9.0, whereas left operand gets assigned 56.0
        performOperation() gets called, evaluates the result of the calculation with / operator, 56.0 left operand, and 9.0 right operand.
        The result val is 6.22222.. and is pushed to the stack after performOperation() returns.
        stack = [6.222..., 11.0]

        9. Ninth loop iteration ->
        - is an operator and is not %, so stack gets popped for both left and right operands and right operand gets assigned 6.222..., whereas left operand gets assigned 11.0
        performOperation() gets called, evaluates the result of the calculation with - operator, 11.0 left operand, and 6.222.. right operand.
        The result val is 4.777..8 and is pushed to the stack after performOperation() returns.
        stack = [4.777..8]

        All iterations are complete, and we exit out of the loop. The stack is NOT empty since it has 4.777..8 which is the result, so it is returned!

        If the percent button is clicked, we DO NOT need a right operand since % means effectively multiplying a value by 0.01. So 0.0 will be passed in as rightOperand to performOperation() in that case.
         */
    }

    /**
     * This function performs the actual calculation. It uses a when statement to dynamically perform the correct calculation based on the operator
     * and assign the result to the result val.
     *
     * In case operator is %, the rightOperand is not used and leftOperand is multiplied by 0.01
     *
     * @param leftOperand -> The double value to be the left operand
     * @param operator -> The operator string to go between left operand and right operand
     * @param rightOperand -> The double value to be the right operand
     *
     * @return result -> The result value double after the appropriate calculation is performed.
     */
    private fun performOperation(leftOperand: Double, operator: String, rightOperand: Double): Double
    {
        // efficient when statement syntax when only one val is to be worked on by all when cases.
        val result: Double =
            when (operator)
            {
                "+" -> leftOperand + rightOperand
                "-" -> leftOperand - rightOperand
                "*" -> leftOperand * rightOperand
                "/" -> leftOperand / rightOperand
                "%" -> leftOperand * 0.01
                // The user cannot really enter any other operator but exception handled for best practice.
                else -> throw IllegalArgumentException("Invalid operator: $operator")
            }
        return result
    }

    /**
     * Checks if the given string expression is a string representation of a number.
     *
     * @param expression -> Given string expression to be checked
     */
    private fun isNumeric(expression: String): Boolean {
        return expression.toDoubleOrNull() != null
    }

    /**
     * Checks if the given string expression is an operator.
     *
     * @param expression -> Given string expression to be checked
     */
    private fun isOperator(expression: String): Boolean {
        return expression.matches(Regex("[+\\-*/%]"))
    }

    /**
     * Returns the precedence of a given operator.
     *
     * + and - have a precedence of 1 whereas *, /, and % have a precedence of 2
     *
     * @param operator -> Given operator whose precedence is to be checked
     */
    private fun precedenceChecker(operator: String): Int
    {
        // efficient when statement since we are working with the operator in all cases
        return when (operator)
        {
            "+", "-" -> 1
            "*", "/", "%" -> 2
            else -> 0
        }
    }

    /**
     * Replaces are unicode occurrences of the operators in expressions with actual string representations.
     * Appends and prepends whitespaces to the operators as well.
     *
     * @param expression -> The given expression to be formatted
     */
    private fun unicodeOperatorsFormatter(expression: String): String
    {
        // Do not need to consider extra whitespaces if two operators occur consecutively since we made sure they can never occur consecutively.
        return expression.replace(unicodeAdd, " + ")
            .replace(unicodeDivide, " / ")
            .replace(unicodeMultiply, " * ")
            .replace(unicodeSubtract, " - ")
            .replace(unicodePercent, " % ")
    }

    /**
     * Checks if the remainder of dividing the givenDouble by 1 is equal to 0.0.
     * This condition is true when givenDouble is a whole number and has no fractional parts.
     *
     * If true, it converts the givenDouble to an int and returns its STRING representation.
     * If false, it just returns the number as a STRING representation of a double.
     *
     * @param givenDouble -> The given double to be converted if required
     */
    private fun convertToIntIfRequired(givenDouble: Double): String
    {
        return if (givenDouble % 1 == 0.0) {
            givenDouble.toInt().toString()
        } else {
            givenDouble.toString()
        }
    }
}