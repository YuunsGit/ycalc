package dev.yunusemre.ycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

private lateinit var resultTextView: TextView
private lateinit var stepsTextView: TextView
private var currentInput = StringBuilder()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.results)
        stepsTextView = findViewById(R.id.steps)

        val digitButtonIds = arrayOf(
            R.id.B0, R.id.B1, R.id.B2, R.id.B3, R.id.B4,
            R.id.B5, R.id.B6, R.id.B7, R.id.B8, R.id.B9
        )

        for (id in digitButtonIds) {
            findViewById<Button>(id).setOnClickListener { onDigitButtonClick(it) }
        }

        val operatorButtonIds = arrayOf(R.id.Bplus, R.id.Bminus, R.id.Bmultiply, R.id.Bdivide)

        for (id in operatorButtonIds) {
            findViewById<Button>(id).setOnClickListener { onOperatorButtonClick(it) }
        }

        findViewById<Button>(R.id.Bequals).setOnClickListener { onEqualsButtonClick() }

        findViewById<Button>(R.id.Bclear).setOnClickListener { onClearButtonClick() }
    }
}

private fun onDigitButtonClick(view: View) {
    val digit = (view as Button).text.toString()

    currentInput.append(digit)
    updateResult()
}

private fun onOperatorButtonClick(view: View) {
    val operator = (view as Button).text.toString()

    currentInput.append(" $operator ")
    updateResult()
}

private fun onEqualsButtonClick() {
    try {
        val result = evaluateExpression(currentInput.toString().replace("×", "*").replace("÷", "/"))

        val formattedResult = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.3f", result).trimEnd('0').trimEnd('.')
        }

        stepsTextView.text = resultTextView.text
        resultTextView.text = formattedResult
        currentInput.clear()
        currentInput.append(formattedResult)
    } catch (e: Exception) {
        currentInput.clear()
        resultTextView.text = "Error"
    }
}

private fun onClearButtonClick() {
    currentInput.clear()
    stepsTextView.text = ""
    updateResult()
}

private fun updateResult() {
    resultTextView.text = currentInput.toString()
}

private fun evaluateExpression(expression: String): Double {
    try {
        return ExpressionBuilder(expression)
            .build()
            .evaluate()
    } catch (e: ArithmeticException) {
        throw RuntimeException("Error in expression evaluation", e)
    }
}