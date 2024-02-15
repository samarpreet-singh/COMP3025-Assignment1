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
            "add" -> m_binding.addButton.text
            "multiply" -> m_binding.multiplyButton.text
            "divide" -> m_binding.divideButton.text
            "subtract" -> m_binding.subtractButton.text
            "percent" -> m_binding.percentButton.text
            else -> ""
        }

        if (operator.isNotEmpty())
        {
            if (this.m_binding.resultTextView.text.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide$unicodePercent]\\s*$")))
            {
                this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1)
            }

            this.m_resultLabelValue += operator
            this.m_binding.resultTextView.text = this.m_resultLabelValue
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
                if (this.m_resultLabelValue.isNotEmpty() &&
                    !this.m_binding.resultTextView.text.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide$unicodePercent]\\s*$")))
                {
                    val cleanedExpression = unicodeOperatorsFormatter(this.m_resultLabelValue)

                    // for debugging
                    //this.m_binding.resultTextView.text = infixToPostfix(cleanedExpression).toString()

                    //testing
                    this.m_binding.resultTextView.text = evaluateExpression(cleanedExpression).toString()
                }
                else
                {
                    Log.e("Operator Ending", "Invalid expression: Expression cannot end with an operator!")
                }
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
        println(this.m_resultLabelValue)
    }


    /* Evaluation function chain begins */

    private fun evaluateExpression(expression: String): Double {
        val postfixExpression = infixToPostfix(expression)
        return evaluatePostfix(postfixExpression)
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

        return output
    }

    private fun evaluatePostfix(postfixExpression: List<String>): Double
    {
        val stack = Stack<Double>()

        for (element in postfixExpression)
        {
            if (isNumeric(element))
            {
                stack.push(element.toDouble())
            }
            else if (isOperator(element))
            {
                val rightOperand = stack.pop()
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
            // % will be handled later as it is tough to implement, still need to play around with it in my phone calculator before I try to implement it
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
            "*", "/" -> 2
            "%" -> 3 // % will be handled later. here it is not modulus, it is Percentage! So effectively means we are multiplying by 0.01, and it needs to be performed first
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


}