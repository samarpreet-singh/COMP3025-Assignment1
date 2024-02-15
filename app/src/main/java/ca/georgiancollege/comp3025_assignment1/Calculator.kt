package ca.georgiancollege.comp3025_assignment1

import android.view.View
import ca.georgiancollege.comp3025_assignment1.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.util.Stack

class Calculator(binding: ActivityMainBinding)
{
    private var m_resultLabelValue: String
    private var m_binding: ActivityMainBinding // member variable underscore. In c++ this means it's a pointer and you cant use it unless the object you are pointing to is a pointer

    init
    {
        this.m_binding = binding
        this.m_resultLabelValue = ""

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
        if (this.m_resultLabelValue.isNotEmpty())
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
                if (this.m_binding.resultTextView.text.matches(Regex(".*[$unicodeAdd$unicodeSubtract$unicodeMultiply$unicodeDivide$unicodePercent]$")))
                {
                    this.m_resultLabelValue = this.m_resultLabelValue.dropLast(1)
                }

                this.m_resultLabelValue += operator
                this.m_binding.resultTextView.text = this.m_resultLabelValue

            }

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
                    this.m_resultLabelValue = ""
                    this.m_binding.resultTextView.text = "0"
                }

            }
            "clear" ->
            {
                this.m_resultLabelValue =""
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
                if (this.m_resultLabelValue.isNotEmpty())
                {
                    val cleanedExpression = this.m_resultLabelValue.replace(unicodeAdd, " + ")
                        .replace(unicodeDivide, " / ")
                        .replace(unicodeMultiply, " * ")
                        .replace(unicodeSubtract, " - ")
                        .replace(unicodePercent, " % ")


                }
            }
        }
    }

    private fun processNumberButtons(view: View)
    {
        when (view.tag.toString())
        {
            "." -> {
                if(!this.m_resultLabelValue.contains("."))
                {
                    if (this.m_resultLabelValue.isEmpty())
                    {
                        this.m_resultLabelValue = "0."
                    }
                    else
                    {
                        this.m_resultLabelValue += view.tag.toString()
                    }
                }
            }
            else -> {
                this.m_resultLabelValue += view.tag.toString()
            }
        }

        this.m_binding.resultTextView.text = this.m_resultLabelValue
    }


    /* Evaluation function chain begins */
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

                isOperator(element) ->
                {
                    while (stack.isNotEmpty() && stack.peek())
                }
            }
        }
    }






    private fun isNumeric(expression: String): Boolean {
        return expression.toDoubleOrNull() != null
    }

    private fun isOperator(expression: String): Boolean {
        return expression.matches(Regex("[+\\-*/%]"))
    }


}