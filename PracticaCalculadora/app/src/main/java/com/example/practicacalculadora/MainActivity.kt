package com.example.practicacalculadora

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var prevNumber: Int = 0
    private var currentOperation: OperationType = OperationType.NONE
    private var result = ""
    private lateinit var lblResult: TextView
    private lateinit var btn0: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btnAddition: Button
    private lateinit var btnSubtraction: Button
    private lateinit var btnMultiplication: Button
    private lateinit var btnDivision: Button
    private lateinit var btnEquals: Button
    private lateinit var btnClearOne: Button
    private lateinit var btnClearEverything: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lblResult = findViewById(R.id.lblResult)
        btn0 = findViewById(R.id.btn0)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btn7 = findViewById(R.id.btn7)
        btn8 = findViewById(R.id.btn8)
        btn9 = findViewById(R.id.btn9)
        btnEquals = findViewById(R.id.btnEquals)
        btnAddition = findViewById(R.id.btnAddition)
        btnSubtraction = findViewById(R.id.btnSubtraction)
        btnMultiplication = findViewById(R.id.btnMultiplication)
        btnDivision = findViewById(R.id.btnDivision)
        btnClearOne = findViewById(R.id.btnClearOne)
        btnClearEverything = findViewById(R.id.btnClearEverything)
        setupEventListeners()
    }

    private fun setupEventListeners() {
        btn1.setOnClickListener { appendNumber(1) }
        btn2.setOnClickListener { appendNumber(2) }
        btn3.setOnClickListener { appendNumber(3) }
        btn4.setOnClickListener { appendNumber(4) }
        btn5.setOnClickListener { appendNumber(5) }
        btn6.setOnClickListener { appendNumber(6) }
        btn7.setOnClickListener { appendNumber(7) }
        btn8.setOnClickListener { appendNumber(8) }
        btn9.setOnClickListener { appendNumber(9) }
        btn0.setOnClickListener { appendNumber(0) }
        btnAddition.setOnClickListener { startOperation(OperationType.ADDITION) }
        btnSubtraction.setOnClickListener { startOperation(OperationType.SUBTRACTION) }
        btnMultiplication.setOnClickListener { startOperation(OperationType.MULTIPLICATION) }
        btnDivision.setOnClickListener { startOperation(OperationType.DIVISION) }
        btnEquals.setOnClickListener { solveOperation() }
        btnClearOne.setOnClickListener { clearOne() }
        btnClearEverything.setOnClickListener { clearEverything() }
    }

    private fun clearEverything() {
        result = ""
        prevNumber = 0
        currentOperation = OperationType.NONE
        reloadScreen()
    }

    private fun clearOne() {
        if (result.isEmpty()) {
            return
        }
        result = result.dropLast(1)
        reloadScreen()
    }

    private fun solveOperation() {
        val currentNumber = result.toInt()
        val operationResult = when (currentOperation) {
            OperationType.ADDITION -> prevNumber + currentNumber
            OperationType.SUBTRACTION -> prevNumber - currentNumber
            OperationType.MULTIPLICATION -> prevNumber * currentNumber
            OperationType.DIVISION -> {
                if (currentNumber == 0) {
                    Toast.makeText(this,
                        getString(R.string.divide_by_zero_error), Toast.LENGTH_SHORT).show()
                    return
                }
                prevNumber / currentNumber
            }

            OperationType.NONE -> currentNumber
        }
        result = operationResult.toString()
        reloadScreen()
    }

    private fun startOperation(operation: OperationType) {
        currentOperation = operation
        prevNumber = result.toInt()
        result = ""
        reloadScreen()
    }

    private fun appendNumber(number: Int) {
        result += number
        reloadScreen()
    }

    private fun reloadScreen() {
        if (result.isEmpty()) {
            lblResult.text = "0"
        } else {
            lblResult.text = result
        }
    }
}