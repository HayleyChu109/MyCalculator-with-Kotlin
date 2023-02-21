package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    var lastNumeric: Boolean = false
    var lastDot: Boolean = false
    var operator: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
    }

    fun onClear(view: View) {
        tvInput?.text = null
        lastNumeric = false
        lastDot = false
        operator = null
    }

    fun onPosNeg(view: View) {
        tvInput?.text.let {
            if (tvInput?.text!!.isNotEmpty() || tvInput?.text != "0") {
                if (tvInput?.text!!.startsWith("-")) {
                    tvInput?.text = tvInput?.text!!.substring(1)
                } else {
                    tvInput?.text = getString(R.string.posNegValue, tvInput?.text)
                }
            }
        }
    }

    fun onPercent(view: View) {
        tvInput?.text.let {
            if (tvInput?.text!!.isNotEmpty()) {
                val tvValue = tvInput?.text.toString()
                val resultInPercent = tvValue.toDouble() / 100

                tvInput?.text = removeZeroAfterDot(resultInPercent.toString())
            }
        }
    }

    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) {
            tvInput?.append((view as Button).text)
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        tvInput?.text?.let {
            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
                operator = (view as Button).text.toString()
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            (value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-"))
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric) {
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                operator?.let {
                  if (tvValue.contains(operator!!)) {
                      val splitValue = tvValue.split(operator!!)
                      var firstValue = splitValue[0]
                      val secondValue = splitValue[1]

                      if (prefix.isNotEmpty()) {
                          firstValue = prefix + firstValue
                      }

                      when (operator) {
                          "/" -> {
                              tvInput?.text =
                                  removeZeroAfterDot((firstValue.toDouble() / secondValue.toDouble()).toString())
                          }
                          "*" -> {
                              tvInput?.text =
                                  removeZeroAfterDot((firstValue.toDouble() * secondValue.toDouble()).toString())
                          }
                          "-" -> {
                              tvInput?.text =
                                  removeZeroAfterDot((firstValue.toDouble() - secondValue.toDouble()).toString())
                          }
                          "+" -> {
                              tvInput?.text =
                                  removeZeroAfterDot((firstValue.toDouble() + secondValue.toDouble()).toString())
                          }
                      }
                  }
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.substring(value.length - 2, value.length) == ".0") {
            value = result.substring(0, result.length - 2)
        }
        return value
    }
}