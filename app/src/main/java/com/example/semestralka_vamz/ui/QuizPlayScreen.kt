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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.data.database.AppDatabase
import com.example.semestralka_vamz.data.database.Repository.QuestionRepository
import com.example.semestralka_vamz.data.database.Repository.StatsRepository
import com.example.semestralka_vamz.data.database.entity.Question
import com.example.semestralka_vamz.data.database.entity.Quiz
import kotlinx.coroutines.delay


@Composable
fun PlayQuizScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit, quiz: Quiz, onBack: () -> Unit, KickOutNoQuiz: () -> Unit, onDoneClick: (correctAnswers: Int, totalQuestions: Int) -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val questionRepository = QuestionRepository(db.questionDao())
    val statsRepository = StatsRepository(db.userStatsDao())

    var questionsList by remember { mutableStateOf<List<Question>>(emptyList()) }
    var nCorrect by remember { mutableStateOf(0) }
    var currentQuestionIndex by remember { mutableStateOf(0) }

    var showWelcomeMessage by remember { mutableStateOf(true) }


    var remainingSeconds by remember { mutableStateOf(60*quiz.timeLimit) }
    val totalMillis = quiz.timeLimit * 60 * 1000
    var remainingMillis by remember { mutableStateOf(totalMillis) }

    LaunchedEffect(Unit) {
        questionRepository.getQuestions().collect { allQuestions ->
            questionsList = allQuestions.filter { it.quizId == quiz.id }
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
        showWelcomeMessage = false
        if (!questionsList.isNotEmpty()){
            KickOutNoQuiz()
        }
    }

    if (quiz.timeLimitOn) {
        LaunchedEffect(questionsList, showWelcomeMessage) {
            if (!showWelcomeMessage && questionsList.isNotEmpty()) {
                while (remainingSeconds > 0) {
                    delay(1000)
                    remainingSeconds--
                }
                val isPerfect = nCorrect == questionsList.size
                statsRepository.updateStats(nCorrect, questionsList.size, isPerfect, quiz.id)
                onDoneClick(nCorrect, questionsList.size)
            }
        }
        LaunchedEffect(showWelcomeMessage, questionsList) {
            if (!showWelcomeMessage && questionsList.isNotEmpty()) {
                while (remainingMillis > 0) {
                    delay(10)
                    remainingMillis -= 10
                    if (remainingMillis < 0) remainingMillis = 0
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        if (showWelcomeMessage) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (questionsList.isNotEmpty())
                {
                    if (quiz.timeLimitOn) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Veľa šťastia!",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(28.dp))
                            Text(
                                text = "Na kvíz máte ${quiz.timeLimit}m",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    } else {
                        Text(
                            text = "Veľa šťastia!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }else{
                    Text(
                        text = "Tento Kvíz nemá žiadne otázky...",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Späť")
                }
                if (!showWelcomeMessage && questionsList.isNotEmpty()) {
                    val progress = (currentQuestionIndex + 1).toFloat() / questionsList.size.toFloat()

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF3F51B5),
                        trackColor = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (quiz.timeLimitOn) {
                        val timeProgress = 1f - (remainingMillis.toFloat() / totalMillis.toFloat())

                        LinearProgressIndicator(
                            progress = timeProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color.Red,
                            trackColor = Color.LightGray
                        )
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))
                if (questionsList.isNotEmpty() && currentQuestionIndex < questionsList.size) {
                    QuestionPanel(
                        question = questionsList[currentQuestionIndex],
                        meno = quiz.title,
                        onAnswerSelected = { isCorrect ->
                            if (isCorrect) {
                                nCorrect += 1
                                println("pocet bodov je $nCorrect")
                            } else {
                                println("pocet bodov je $nCorrect")
                            }
                            currentQuestionIndex += 1
                        }
                    )
                } else if (questionsList.isNotEmpty()) {
                    onDoneClick(nCorrect, questionsList.size)
                    val isPerfect = nCorrect == questionsList.size
                    LaunchedEffect(Unit) {
                        statsRepository.updateStats(nCorrect, questionsList.size, isPerfect, quiz.id)
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

}

@Composable
fun QuestionPanel(question: Question, meno: String, onAnswerSelected: (Boolean) -> Unit){

    var answers = remember (question) { mutableListOf(question.answer1, question.answer2, question.answer3, question.correctAnswer).filterNotNull().shuffled() }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(question) {
        selectedAnswer = null
    }

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer != null) {
            delay(1000)
            onAnswerSelected(selectedAnswer == question.correctAnswer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = meno,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)

        )
        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (answers.isEmpty()) {
            Text("Táto otázka nemá odpovede.")
        } else {
            answers.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pair.forEach { answer ->
                        AnswerCard(answer = answer, modifier = Modifier.weight(1f), question.correctAnswer, selectedAnswer,
                            onClick = {
                                selectedAnswer = answer
                            })
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AnswerCard(answer: String, modifier: Modifier = Modifier, correctAnswer: String, selectedAnswer: String?, onClick: () -> Unit) {
    val backgroundColor = when {
        selectedAnswer == null -> Color.White
        answer == correctAnswer && selectedAnswer == answer -> Color(0xFFAAF683)
        selectedAnswer == answer -> Color(0xFFFF8C94)
        else -> Color.White
    }

    Card(
        modifier = modifier
            .heightIn(min = 64.dp, max = 400.dp)
            .fillMaxWidth()
            .clickable(enabled = selectedAnswer == null) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}







