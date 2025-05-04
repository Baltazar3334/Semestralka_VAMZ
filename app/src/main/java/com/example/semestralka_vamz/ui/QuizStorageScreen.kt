package com.example.semestralka_vamz.ui
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.data.database.AppDatabase
import com.example.semestralka_vamz.data.database.Repository.QuestionRepository
import com.example.semestralka_vamz.data.database.Repository.QuizRepository
import com.example.semestralka_vamz.data.database.entity.Question
import com.example.semestralka_vamz.data.database.entity.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun QuizStorageScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit, onPlayClick: (Quiz) -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val quizRepository = QuizRepository(db.quizDao())
    val questionRepository = QuestionRepository(db.questionDao())

    val quizList0 = quizRepository.getQuizzes()
    var quizList by remember { mutableStateOf<List<Quiz>>(emptyList()) }

    val questionsList0 = questionRepository.getQuestions()
    var questionsList by remember { mutableStateOf<List<Question>>(emptyList()) }

    LaunchedEffect(questionsList0) {
        questionsList0.collect { question ->
            questionsList = question
        }
    }
    LaunchedEffect(quizList0) {
        quizList0.collect { quiz ->
            quizList = quiz
        }
    }

    for ( Quiz in quizList) {
        println("Quiz:")
        println(Quiz.title)
        println("Otazky:")
        for (Question in questionsList) {
            if (Question.quizId == Quiz.id) {
                println(Question.question)
                println(Question.correctAnswer)
                println(Question.answer1)
                println(Question.answer2)
                println(Question.answer3)
            }
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
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 25.dp,
                        bottom = 120.dp,
                        start = 16.dp,
                        end = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                QuizSection(title = "Basic", quizList, onPlayClick)
                QuizSection(title = "Flash Cards", quizList, onPlayClick)
                Spacer(modifier = Modifier.weight(1f))

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

    BackHandler {
        onHomeClick()
    }
}

@Composable
fun QuizSection(title: String, quizzes: List<Quiz>, onPlayClick: (Quiz) -> Unit) {
    var colapsed by remember { mutableStateOf(false) }
    if (!colapsed){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = { colapsed = true } ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "colapse")
                }
            }
            for (quiz in quizzes) {
                QuizItem(quiz, onPlayClick)
            }


        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = { colapsed = false } ) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "expand")
                }
            }
        }
    }

}

@Composable
fun QuizItem(kviz: Quiz, onPlayClick: (Quiz) -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    var jeOblubeny by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            jeOblubeny = db.quizDao().isFavourite(kviz.id)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val novaHodnota = !jeOblubeny
                    db.quizDao().updateFavourite(kviz.id, novaHodnota)
                    withContext(Dispatchers.Main) {
                        jeOblubeny = novaHodnota
                    }
                }
            } ) {
                if (jeOblubeny) {
                    Icon(Icons.Default.Favorite, contentDescription = "Obľúbený")
                } else {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Nie je obľúbený")
                }

            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = kviz.title)

            }
            IconButton(onClick = {  } ) {
                Icon(Icons.Default.Build, contentDescription = "Upravit")
            }
            IconButton(onClick = {
                onPlayClick(kviz)
            } ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "hratKviz")
            }
        }
    }
}


