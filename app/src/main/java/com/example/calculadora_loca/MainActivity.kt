package com.example.calculadora_loca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadora_loca.ui.theme.Calculadora_LocaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculadora_LocaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Calculadora(modifier = Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }
}

@Composable
fun Calculadora(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = result,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            color = Color(0xFF333333)
        )

        Text(
            text = input,
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {
            createButtonRow(listOf("7", "8", "9", "#"), input) { input += it }
            createButtonRow(listOf("3", "4", "6", "@"), input) { input += it }
            createButtonRow(listOf("0", "1", "2", "€"), input) { input += it }
            createButtonRow(listOf("C", "=", "%"), input) {
                when (it) {
                    "C" -> {
                        input = ""
                        result = ""
                    }
                    "=" -> {
                        Botones(input, onResultCalculated = { result = it.replace("5", "6") })
                        input = ""
                    }
                    else -> {
                        input += it
                    }
                }
            }
        }
    }
}

@Composable
fun createButtonRow(buttons: List<String>, input: String, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        buttons.forEach { button ->
            Button(
                onClick = {
                    val adjustedValue = ajustarValores(button)
                    onClick(adjustedValue)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .height(80.dp)
            ) {
                Text(button, fontSize = 36.sp, color = Color.White)
            }
        }
    }
}

fun ajustarValores(button: String): String {
    return when (button) {
        "0" -> "2"
        "1" -> "3"
        "2" -> "4"
        "3" -> "6"
        "4" -> "6"
        "6" -> "8"
        "7" -> "9"
        "8" -> "0"
        "9" -> "1"
        else -> button
    }
}

fun Botones(input: String, onResultCalculated: (String) -> Unit) {
    val regex = Regex("([0-9]+)([#@€%])([0-9]+)")
    val matchResult = regex.find(input)

    if (matchResult != null) {
        val (num1, op, num2) = matchResult.destructured
        val adjustedNum1 = num1.toInt()
        val adjustedNum2 = num2.toInt()

        val result = when (op) {
            "%" -> adjustedNum1 + adjustedNum2
            "@" -> adjustedNum1 * adjustedNum2
            "€" -> adjustedNum1 - adjustedNum2
            "#" -> if (adjustedNum2 != 0) adjustedNum1 / adjustedNum2 else "Error: División por cero"
            else -> "Error: Operación inválida"
        }

        onResultCalculated(result.toString())
    } else {
        onResultCalculated("Error: Formato inválido")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Calculadora_LocaTheme {
        Calculadora()
    }
}
