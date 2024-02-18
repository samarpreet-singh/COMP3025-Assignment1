package ca.georgiancollege.comp3025_assignment1

import android.util.Log
import android.view.View
import ca.georgiancollege.comp3025_assignment1.databinding.ActivityMainBinding
import java.lang.IllegalArgumentException
import java.util.Stack

class Calculator(binding: ActivityMainBinding)
{
    private var m_resultLabelValue: String
    private var m_binding: ActivityMainBinding // member variable underscore. In c++ this means it's a pointer and you cant use it unless the object you are pointing to is a pointer

    init
    {
        this.m_binding = binding
        this.m_resultLabelValue = "0"

        initializeOnClickListeners()
    }

    private val unicodeAdd: String = this.m_binding.addButton.text.toString()
    private val unicodeSubtract: String = this.m_binding.subtractButton.text.toString()
    private val unicodeMultiply: String = this.m_binding.multiplyButton.text.toString()
    private val unicodeDivide: String = this.m_binding.divideButton.text.toString()
    private val unicodePercent: String = this.m_binding.percentButton.text.toString()

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

    private fun processOperatorButtons(view: View)
    {
        val operator = when (view.tag.toString())
        {
            "add" -> this.m_binding.addButton.text
            "multiply" -> this.m_binding.multiplyButton.text
            "divide" -> this.m_binding.divideButton.text
            "subtract" -> this.m_binding.subtractButton.text
            "percent" -> this.m_binding.percentButton.text
            else -> ""
        }

        if (operator.isNotEmpty())
        {
            if (this.m_resultLabelValue.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide]\\s*$")))
            {
                this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1)
            }

            this.m_resultLabelValue += operator
            this.m_binding.resultTextView.text = this.m_resultLabelValue
            println("Operator: " + this.m_resultLabelValue)

            performCalculations()

        }
    }

    private fun processExtraButtons(view: View)
    {
        when (view.tag.toString())
        {
            "backspace" ->
            {
                this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1)
                this.m_binding.resultTextView.text = this.m_resultLabelValue

                if (this.m_resultLabelValue.isEmpty() || this.m_resultLabelValue == "-") // KOTLIN IS TYPE SAFE IT WILL IGNORE AN ERROR
                {
                    this.m_resultLabelValue = "0"
                    this.m_binding.resultTextView.text = "0"
                }

            }
            "clear" ->
            {
                this.m_resultLabelValue ="0"
                this.m_binding.resultTextView.text = "0"
            }
            "plus_minus" ->
            {
                if (this.m_resultLabelValue.isNotEmpty())
                {
                    if (this.m_resultLabelValue.contains("-"))
                    {
                        this.m_resultLabelValue = this.m_resultLabelValue.removePrefix("-")
                    }
                    else
                    {
                        this.m_resultLabelValue = "-" + this.m_resultLabelValue
                    }
                    this.m_binding.resultTextView.text = this.m_resultLabelValue
                }
            }
            "=" ->
            {
                performCalculations()
            }
        }
    }

    private fun processNumberButtons(view: View)
    {
        val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue)
        val lastElement = if (cleanedExpression.isNotEmpty()) cleanedExpression.split(" ").last() else ""

        when (view.tag.toString())
        {
            "0" ->
            {
                if (this.m_binding.resultTextView.text == "0")
                {
                    this.m_resultLabelValue = "0"
                    this.m_binding.resultTextView.text = this.m_resultLabelValue
                }
                else if (lastElement == "0")
                {
                    Log.e("Last Element Zero", "The last element in the expression cannot be multiple zeroes!")
                }
                else
                {
                    this.m_resultLabelValue += view.tag.toString()
                }
            }
            "." -> {
                if(!lastElement.contains("."))
                {
                    this.m_resultLabelValue += view.tag.toString()
                }
            }
            else -> {
                if (this.m_binding.resultTextView.text == "0")
                {
                    this.m_resultLabelValue = view.tag.toString()
                }
                else
                {
                    this.m_resultLabelValue += view.tag.toString()
                }
            }
        }


        this.m_binding.resultTextView.text = this.m_resultLabelValue
        println("Number: " + this.m_resultLabelValue)

    }

    /* Evaluation function chain begins */
    private fun performCalculations(){
        // equals button will usually only be pressed when the proper full expression has been entered, example 9+3-2 and not 9+3-
        if (this.m_resultLabelValue.isNotEmpty() &&
            !this.m_resultLabelValue.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide]\\s*$")))
        {
            val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue)

            // for debugging
            //this.m_binding.resultTextView.text = infixToPostfix(cleanedExpression).toString()

            //testing
           // this.m_resultLabelValue = evaluateExpression(cleanedExpression).toString()

            val result = evaluateExpression(cleanedExpression)

            // Check if the result is an integer, and format accordingly
            this.m_resultLabelValue = intConversionRequirementChecker(result)

            this.m_binding.resultTextView.text = this.m_resultLabelValue
        }
        // this part is to keep evaluating user input as they go and press more operator buttons, just like in real calculators! Also regex makes sure that this gets triggered whenever the operator buttons are pressed too but only after the first calculation has been made
        else if (this.m_resultLabelValue.isNotEmpty() &&
            this.m_resultLabelValue.matches(Regex(".*[0-9]+[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide][0-9]+.*")))
        {
            val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue)

            println("Realtime String before drop: " + cleanedExpression)
            var realtimeEvaluationString = cleanedExpression.dropLast(2) // drop the most recently pressed operator for evaluating the string before that
            println("Realtime String: " + realtimeEvaluationString)

            val result = evaluateExpression(realtimeEvaluationString)

            realtimeEvaluationString = intConversionRequirementChecker(result)

            this.m_binding.resultTextView.text = realtimeEvaluationString
        }
        else
        {
            // have to fix wrongful exception trigger on first operator press
            Log.e("Operator Ending", "Invalid expression: Expression cannot end with an operator!")
        }
    }

    private fun evaluateExpression(expression: String): Double {
        println("Given Expression: " + expression)
        val postfixExpression = infixToPostfix(expression)
        val result = evaluatePostfix(postfixExpression)

        println("Result: " + result)

        return result
    }

    private fun infixToPostfix(infixExpression: String): List<String>
    {
        val output = mutableListOf<String>()
        val stack = Stack<String>()

        val splitInfixExpressionList = infixExpression.split(" ")

        for (element in splitInfixExpressionList)
        {
            when
            {
                isNumeric(element) -> output.add(element)
                isOperator(element) -> {
                    while (stack.isNotEmpty() && isOperator(stack.peek()) &&
                        (precedenceChecker(element) <= precedenceChecker(stack.peek()))
                    ) {
                        output.add(stack.pop())
                    }
                    stack.push(element)
                }
            }
        }

        while (stack.isNotEmpty()) {
            output.add(stack.pop())
        }

        println("Postfix output: " + output)
        return output
    }

    private fun evaluatePostfix(postfixExpression: List<String>): Double
    {
        val stack = Stack<Double>() // stack plays a different role here than infixToPostfix

        for (element in postfixExpression)
        {
            if (isNumeric(element))
            {
                stack.push(element.toDouble())
            }
            else if (isOperator(element))
            {
                var rightOperand: Double = 0.0
                if (element != "%")
                {
                rightOperand = stack.pop()
                }
                val leftOperand = stack.pop()
                val result = performOperation(leftOperand, element, rightOperand)// need to perform the operation here
                stack.push(result)
            }
        }

        return if (stack.isNotEmpty()) stack.pop() else throw IllegalArgumentException("Invalid expression provided")
    }



    private fun performOperation(leftOperand: Double, operator: String, rightOperand: Double): Double
    {
        val result: Double = when (operator) {
            "+" -> leftOperand + rightOperand
            "-" -> leftOperand - rightOperand
            "*" -> leftOperand * rightOperand
            "/" -> leftOperand / rightOperand
            "%" -> leftOperand * 0.01
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }

        // Need to account for precision loss because of big decimal and account for 8 point accuracy as per requirements
        return result
    }





    private fun isNumeric(expression: String): Boolean {
        return expression.toDoubleOrNull() != null
    }

    private fun isOperator(expression: String): Boolean {
        return expression.matches(Regex("[+\\-*/%]"))
    }

    private fun precedenceChecker(expression: String): Int
    {
        return when (expression)
        {
            "+", "-" -> 1
            "*", "/", "%" -> 2
            else -> 0
        }
    }

    private fun unicodeOperatorsFormatter(expression: String): String
    {
        return expression.replace(unicodeAdd, " + ")
            .replace(unicodeDivide, " / ")
            .replace(unicodeMultiply, " * ")
            .replace(unicodeSubtract, " - ")
            .replace(unicodePercent, " % ")
    }

    private fun intConversionRequirementChecker(givenDouble: Double): String
    {
        return if (givenDouble % 1 == 0.0) {
            givenDouble.toInt().toString()
        } else {
            givenDouble.toString()
        }
    }
}