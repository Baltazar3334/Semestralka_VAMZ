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
//composable funkcia na zobrazenie obrazovky pocas hrania kvizu, podla toho ci kviz je validny hraca bud vyhodi naspat alebo spusti kviz a zobrazuje postupne otazky a odpovede
    val context = LocalContext.current // context pre pracu s DB
    val db = AppDatabase.getDatabase(context)
    val questionRepository = QuestionRepository(db.questionDao())
    val statsRepository = StatsRepository(db.userStatsDao()) //prustup k databaze cez repozitar stats a question

    var questionsList by remember { mutableStateOf<List<Question>>(emptyList()) } // zoznam otazok
    var nCorrect by remember { mutableStateOf(0) } //pocet spravne zodpovedanych otazok
    var currentQuestionIndex by remember { mutableStateOf(0) } // index na ktrej otazke sa prave uzivatel nachadza

    var showWelcomeMessage by remember { mutableStateOf(true) } // pomocny boolean pre zobrazenie welcome message


    var remainingSeconds by remember { mutableStateOf(60*quiz.timeLimit) } // zostavajuci pocet sekund do konca kvizu ak je casomiera zapnuta
    val totalMillis = quiz.timeLimit * 60 * 1000 //kolko sekund ma uzivatel na vyplnenie kvizu
    var remainingMillis by remember { mutableStateOf(totalMillis) } // zostavajuce milisekundy

    LaunchedEffect(Unit) {
        questionRepository.getQuestions().collect { allQuestions ->
            questionsList = allQuestions.filter { it.quizId == quiz.id } // precitanie otazok z databazy ktore maju spravne quizId cez asynchronnu operaciu
        }
    }

    LaunchedEffect(Unit) { // asynchronna operacia na zobrazenie welcome message len na 2 sekundy a skoncenie kvizu aj kviz nema otazky
        delay(2000)
        showWelcomeMessage = false
        if (!questionsList.isNotEmpty()){
            KickOutNoQuiz()
        }
    }

    if (quiz.timeLimitOn) {
        LaunchedEffect(questionsList, showWelcomeMessage) { // asynchronna operacia na vypocet zostavajuceho casu
            if (!showWelcomeMessage && questionsList.isNotEmpty()) {
                while (remainingMillis > 0) {
                    delay(10)
                    remainingMillis -= 10
                    if (remainingMillis < 0) remainingMillis = 0
                }
                val isPerfect = nCorrect == questionsList.size // ked cas dojde tak sa automaticky ukonci kviz a ulozia sa data
                statsRepository.updateStats(nCorrect, questionsList.size, isPerfect, quiz.id)
                onDoneClick(nCorrect, questionsList.size)
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

                    LinearProgressIndicator( // progress bar na zobrazenie kolko otazok je zodpovedanych a kolko zostava
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

                        LinearProgressIndicator( // progress bar na zobrazenie zostavajuceho casu na vyplnenie
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
                    QuestionPanel(// logika na prechadzanie otazkami(pomocou questionPanel) a nasledne uozenie dat a vypnutie kvizu po vyplneni poslednej otazky
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
                } else if (questionsList.isNotEmpty()) { // prikazy ked sa kviz ukoncuje, ukladanie dat
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
//funkcia na zobrazenie otazok kvizu pocas jeho prehravania
    var answers = remember (question) { mutableListOf(question.answer1, question.answer2, question.answer3, question.correctAnswer).filterNotNull().shuffled() } //premenna na zapisanie odpovedi
    var selectedAnswer by remember { mutableStateOf<String?>(null) } // premenna na zapisanie vybranej odpovede

    LaunchedEffect(question) {
        selectedAnswer = null
    } // ked sa zmeni otazka tak sa vynuluje vybrana otazka

    LaunchedEffect(selectedAnswer) { // ked sa vyberie otazka tak sa spusti onAnswerSelected s oneskorenim jednej sekundy aby si hrac stihol pozret UI ci odpovedal spravne
        if (selectedAnswer != null) {
            delay(1000)
            onAnswerSelected(selectedAnswer == question.correctAnswer) // vyvolanie onAnswerSelected de sa podava Boolean hodnota ci je vybrata odpoved zhodna s spravnou odpovedou otazky
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

        if (answers.isEmpty()) { // logika na spravovanie otazky ktora nema odpoved
            Text("Táto otázka nemá odpovede.")
        } else {
            answers.chunked(2).forEach { pair -> // parovanie odpovedi do dvojic a ich zobrazovanie vedla seba
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
    val backgroundColor = when { // premenna na spravovanie vyfarbenia odpovedovej karty na zaklade toho ci je zvolena a je spravne alebo nie
        selectedAnswer == null -> Color.White
        answer == correctAnswer && selectedAnswer == answer -> Color(0xFFAAF683)
        selectedAnswer == answer -> Color(0xFFFF8C94)
        else -> Color.White
    }
            // AnswerCard je funkcia na zobrazenie jednej odpovede ako Card a jej zmeny pri jej stlaceni
    Card(
        modifier = modifier
            .heightIn(min = 64.dp, max = 400.dp)
            .fillMaxWidth()
            .clickable(enabled = selectedAnswer == null) { onClick() }, // odpoved je kliknutelna a vyvolava zmenu farby pri kliknuti a nasledne onClick ktorý vyvolá hodnotenie odpovede
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







