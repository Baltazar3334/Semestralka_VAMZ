package com.example.semestralka_vamz.ui
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.data.database.Repository.QuestionRepository
import com.example.semestralka_vamz.data.database.Repository.QuizRepository
import com.example.semestralka_vamz.data.database.entity.Question
import com.example.semestralka_vamz.data.database.entity.Quiz

@Composable
fun CreateQuizScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit) {
    var questions by remember { mutableStateOf(listOf("Otázka 1")) }
    var quizName by remember { mutableStateOf("") }
    var timeLimit by remember { mutableStateOf(0f) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isTimeLimitEnabled by remember { mutableStateOf(false) }
    val questions2 = remember { mutableStateListOf<QuestionData>() }

    BackHandler {
        onHomeClick()
    }

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
                                    questions2.add(QuestionData(title = "Otázka ${questions2.size + 1}"))
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add question")
                                }

                                Text(
                                    "Pridať Otázku",
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )

                                IconButton(onClick = {  }) {
                                    Icon(Icons.Default.Send, contentDescription = "Finish quiz")
                                }
                            }
                        }
                    }

                    questions2.forEachIndexed { index, data ->
                        QuestionItem(
                            data = data,
                            onRemove = { questions2.removeAt(index) }
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
        data: QuestionData,
        onRemove: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var isExpanded by remember { mutableStateOf(false) }

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
                Text(data.title)
                IconButton(onClick = { isExpanded = true }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Open")
                }
                Text(data.question.value)
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
                    Text(data.title)
                    IconButton(onClick = { isExpanded = false }) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Close")
                    }
                    TextField(
                        value = data.question.value,
                        onValueChange = { data.question.value = it },
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
                        value = data.correctAnswer.value,
                        onValueChange = { data.correctAnswer.value = it },
                        label = { Text("Správna odpoveď") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { keyboardController?.show() }
                    )
                }

                data.answers.forEachIndexed { index, answer ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("○", fontSize = 20.sp, color = Color.Black, modifier = Modifier.padding(end = 8.dp))
                        TextField(
                            value = answer,
                            onValueChange = {
                                data.answers[index] = it

                            },
                            label = { Text("Odpoveď ${index + 1}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { keyboardController?.show() }
                        )
                    }
                }

                IconButton(onClick = {
                    data.answers.add("")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add answer")
                }
            }
        }
    }
}

suspend fun saveQuizAndQuestions(
    quizName: String,
    timeLimit: Float,
    isTimeLimitEnabled: Boolean,
    questions: List<QuestionData>,
    quizRepository: QuizRepository,
    questionRepository: QuestionRepository
) {
    val quiz = Quiz(
        title = quizName,
        timeLimit = timeLimit.toInt(),
        timeLimitOn = isTimeLimitEnabled
    )

    val quizId = quizRepository.addQuizReturningId(quiz)

    questions.forEach { data ->
        val question = Question(
            quizId = quizId,
            question = data.question.value,
            correctAnswer = data.correctAnswer.value,
            answer1 = data.answers.getOrNull(0),
            answer2 = data.answers.getOrNull(1),
            answer3 = data.answers.getOrNull(2)
        )
        questionRepository.addQuestion(question)
    }
}




data class QuestionData(
    var title: String = "",
    var question: MutableState<String> = mutableStateOf(""),
    var correctAnswer: MutableState<String> = mutableStateOf(""),
    var answers: SnapshotStateList<String> = mutableStateListOf(""),
    var isTimeLimitEnabled: MutableState<Boolean> = mutableStateOf(true)
)
