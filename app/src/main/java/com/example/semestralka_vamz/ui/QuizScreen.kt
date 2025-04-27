package com.example.semestralka_vamz.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateQuizScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit) {
    var questions by remember { mutableStateOf(listOf("Otázka 1")) }
    var quizName by remember { mutableStateOf("") }
    var timeLimit by remember { mutableStateOf(0f) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isTimeLimitEnabled by remember { mutableStateOf(false) }

    fun removeQuestion(index: Int) {
        questions = questions.toMutableList().apply {
            removeAt(index)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(0.dp, 0.dp, 0.dp, 25.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                OutlinedTextField(
                    value = quizName,
                    onValueChange = { quizName = it },
                    label = { Text("Meno") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { keyboardController?.show() }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch(
                        checked = isTimeLimitEnabled,
                        onCheckedChange = { isTimeLimitEnabled = it }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Časové obmedzenie")

                }

                if (isTimeLimitEnabled) {
                    Column {
                        Text("minút")
                        Slider(
                            value = timeLimit,
                            onValueChange = { timeLimit = it },
                            valueRange = 0f..100f,
                            steps = 100,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("0")
                            Text("100")
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp)
                    )
                    {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    questions = questions + "Otázka ${questions.size + 1}"
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add question")
                                }

                                Text(
                                    "Pridať Otázku",
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )

                                IconButton(onClick = { /* Finish quiz */ }) {
                                    Icon(Icons.Default.Send, contentDescription = "Finish quiz")
                                }
                            }
                        }
                    }

                    questions.forEachIndexed { index, question ->
                        QuestionItem(
                            title = question,
                            onRemove = { removeQuestion(index) }
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            BottomNavigationBar(
                onEditClick = onEditClick,
                onHomeClick = onHomeClick,
                onStorageClick = onStorageClick
            )
        }

    }



}

@Composable
fun QuestionItem(
        title: String,
        onRemove: () -> Unit
) {
    var question by remember { mutableStateOf("") }
    var cAnswer by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isExpanded by remember { mutableStateOf(false) }
    var answers by remember { mutableStateOf(listOf(Answer(""))) }

    if (!isExpanded) {


        Card(
            modifier = Modifier.
            fillMaxWidth()
                .padding(4.dp)
                .height(40.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(7.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Text(title)
                IconButton(onClick = { isExpanded = true }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Open")
                }
                Text(question)
                IconButton(onClick = { onRemove() }) {
                    Icon(Icons.Default.Close, contentDescription = "Delete")
                }
            }
        }
    } else {


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(7.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(title)
                    IconButton(onClick = { isExpanded = false }) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Close")
                    }
                    TextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Otazka") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { keyboardController?.show() }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("○", fontSize = 20.sp, color = Color.Black, modifier = Modifier.padding(end = 8.dp))
                    TextField(
                        value = cAnswer,
                        onValueChange = { cAnswer = it },
                        label = { Text("Správna odpoveď") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { keyboardController?.show() }
                    )
                }

                answers.forEachIndexed { index, answer ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("○", fontSize = 20.sp, color = Color.Black, modifier = Modifier.padding(end = 8.dp))
                        TextField(
                            value = answer.answerText,
                            onValueChange = { newText ->
                                answers = answers.toMutableList().apply {
                                    this[index] = answer.copy(answerText = newText)
                                }
                            },
                            label = { Text("Odpoveď ${index + 1}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { keyboardController?.show() }
                        )
                    }
                }

                IconButton(onClick = {
                    answers = answers + Answer("")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add answer")
                }
            }
        }
    }

}



data class Answer(
    var answerText: String
)