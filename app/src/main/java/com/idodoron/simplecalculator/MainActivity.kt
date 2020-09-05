package com.idodoron.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    var ans : Double = 0.0
    var a : Double = 0.0
    var A : Int? = 0
    var b : Double? = null
    var B : Int? = null
    var op : Operator = Operator.PLUS
    val d = 0.00001
    var decimalPointUsage : Boolean = false

    enum class Operator {
        PLUS{
            override fun commitOp(a: Double, b: Double) : Double { return a + b }

            override fun toString(): String { return "+" }
        },
        MINUS{
            override fun commitOp(a: Double, b: Double) : Double { return a - b }

            override fun toString(): String { return "-" }
        },
        MULT{
            override fun commitOp(a: Double, b: Double) : Double { return a * b }

            override fun toString(): String { return "*" }
        },
        DIV{
            override fun commitOp(a: Double, b: Double) : Double {
                return a / b
            }

            override fun toString(): String { return "/" }
        },
        EQ{
            override fun commitOp(a: Double, b: Double) : Double { return a + b }

            override fun toString(): String { return "=" }
        };

        abstract fun commitOp(a : Double, b : Double) : Double;

        abstract override fun toString(): String;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigit(view: View){
        val input = (view as Button).text
        if(op == Operator.EQ)
            onClear(view)
        if(ioScreen.text.toString() == "" && input == ".")
            ioScreen.text = "0"
        ioScreen.append(input)
    }

    fun onDecimalPoint(view: View){
        if(!decimalPointUsage){
            if(ioScreen.text.toString() == "")
                ioScreen.text = "0."
            else
                onDigit(view)
            decimalPointUsage = true
        }
    }

    fun switchOperator(newOp : Operator, view : View){
        val input = commit()
        op = newOp
        if(input == ""){
            val text = partialResultScreen.text.toString()
            if(text.isNotEmpty())
                partialResultScreen.text = text.dropLast(2)
        }
        partialResultScreen.append("$op ")
    }

    fun onPlus(view: View){
        switchOperator(Operator.PLUS, view)
    }

    fun onMinus(view: View){
        switchOperator(Operator.MINUS, view)
    }

    fun onDivide(view: View){
        switchOperator(Operator.DIV, view)
    }

    fun onMultiply(view: View){
        switchOperator(Operator.MULT, view)
    }

    fun onDel(view: View){
        val input = ioScreen.text.toString()
        if(input.isEmpty())
            return
        else if(input[input.length-1] == '.')
            decimalPointUsage = false

        if(op == Operator.EQ){
            onClear(view)
        }

        ioScreen.text = input.dropLast(1)
    }

    fun onClear(view: View){
        a = 0.0
        b = 0.0
        op = Operator.PLUS
        ioScreen.text = ""
        partialResultScreen.text = ""
        decimalPointUsage = false
    }

    fun onEquals(view: View){
        switchOperator(Operator.EQ, view)
        ans = a
        if(A != null)
            ioScreen.text = A.toString()
        else
            ioScreen.text = a.toString()
        a = 0.0
        A = 0
    }

    fun onAnswer(view: View){
        if(op == Operator.EQ){
            onClear(view)
        }
        ioScreen.text = ans.toString()
    }

    fun commit() : String{
        if(op == Operator.EQ)
            partialResultScreen.text = ""
        val io : String = ioScreen.text.toString()
        b = io.toDoubleOrNull()
        B = io.toIntOrNull()
        val partialResult : String
        if(b != null) {
            if (B != null)
                partialResult = "$B "
            else
                partialResult = "$b "
            a = op.commitOp(a, b!!)
        }
        else
            partialResult = ""
        val integerA = a.roundToInt()
        if(Math.abs(a - integerA.toDouble()) < d) {
            A = integerA
            a = A!!.toDouble()
        }
        else
            A = null
        partialResultScreen.append(partialResult)
        ioScreen.text = ""
        b = 0.0
        decimalPointUsage = false
        return io
    }
}