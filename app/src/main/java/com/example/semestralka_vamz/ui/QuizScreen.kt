package com.example.semestralka_vamz.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CreateQuizScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit) {
    var questions by remember { mutableStateOf(listOf("Otázka")) } // otazky
    var quizName by remember { mutableStateOf("") } // názov kvízu
    var timeLimit by remember { mutableStateOf(0f) } // časové obmedzenie

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
                    value = "",
                    onValueChange = { quizName = it },
                    label = { Text("Meno") },
                    placeholder = { Text("...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Časové obmedzenie")
                    Switch(checked = true, onCheckedChange = {})
                }

                Column {
                    Text("minút")
                    Slider(
                        value = 0f,
                        onValueChange = {timeLimit = it},
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

                    questions.forEach { question ->
                        QuestionItem(question)
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
fun QuestionItem(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(title)
            IconButton(onClick = { /* Move up */ }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move up")
            }
            IconButton(onClick = { /* Delete */ }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun QuestionItemWithDescription(title: String, popis1: String, popis2: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title)
                    Text(popis1)
                    Text(popis2)
                }
                IconButton(onClick = { /* Download */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Download")
                }
            }
        }
    }
}

