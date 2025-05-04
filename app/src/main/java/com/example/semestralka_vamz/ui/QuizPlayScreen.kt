package com.example.semestralka_vamz.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.data.database.AppDatabase
import com.example.semestralka_vamz.data.database.Repository.QuestionRepository
import com.example.semestralka_vamz.data.database.entity.Question
import com.example.semestralka_vamz.data.database.entity.Quiz


@Composable
fun PlayQuizScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit, quiz: Quiz, onBack: () -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val questionRepository = QuestionRepository(db.questionDao())
    val questionsList0 = questionRepository.getQuestions()
    var questionsList by remember { mutableStateOf<List<Question>>(emptyList()) }
    var nCorrect by remember { mutableStateOf(Int) }

    println("***************************")
    println("Quiz:")
    println(quiz.title)
    println("Otazky:")
    for (Question in questionsList) {
        if (Question.quizId == quiz.id) {
            println(Question.question)
            println(Question.correctAnswer)
            println(Question.answer1)
            println(Question.answer2)
            println(Question.answer3)
        }
    }



    LaunchedEffect(questionsList0) {
        questionsList0.collect { question ->
            questionsList = question
        }
        questionsList = questionsList.filter { it.quizId == quiz.id }
    }


    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F0F0))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Späť")
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (questionsList.isNotEmpty()) {
                QuestionPanel(questionsList[0])
            } else {
                Text("Tento kvíz neobsahuje žiadne otázky.")
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
fun QuestionPanel(question: Question){
    var answers = mutableListOf(question.answer1, question.answer2, question.answer3, question.correctAnswer).filterNotNull().shuffled()




    if (answers.size == 0) {
        Column(){
            MenuCard(title = question.question, Modifier, 180, 0, "otazka?")
            MenuCard(title = "Otázka Nemá Odpoveď", Modifier, 180, 0, "otazka?")
        }
    } else if(answers.size == 1) {
        Column(){
            MenuCard(title = question.question, Modifier, 180, 0, "otazka?")
            MenuCard(title = answers[0], Modifier, 180, 0, "otazka?")
        }
    } else if (answers.size == 2) {
        Column(
            modifier = Modifier.
            height(100.dp).
            width(100.dp),
            verticalArrangement = Arrangement.Center

        ) {
            MenuCard(title = question.question, Modifier, 180, 0, "otazka?")
            Row() {
                MenuCard(title = answers[0], Modifier, 180, 0, "otazka?")
                MenuCard(title = answers[1], Modifier, 180, 0, "otazka?")
            }
        }
    } else if (answers.size == 3) {
        Column(){
            MenuCard(title = question.question, Modifier, 180, 0, "otazka?")
            Row(){
                MenuCard(title = answers[0], Modifier, 180, 0, "otazka?")
                MenuCard(title = answers[1], Modifier, 180, 0, "otazka?")
                MenuCard(title = answers[2], Modifier, 180, 0, "otazka?")
            }
        }
    } else if (answers.size == 4) {
        Column(){
            MenuCard(title = question.question, Modifier, 180, 0, "otazka?")
            Row(){
                MenuCard(title = answers[0], Modifier, 180, 0, "otazka?")
                MenuCard(title = answers[1], Modifier, 180, 0, "otazka?")
            }
            Row(){
                MenuCard(title = answers[2], Modifier, 180, 0, "otazka?")
                MenuCard(title = answers[3], Modifier, 180, 0, "otazka?")
            }
        }
    }

}






