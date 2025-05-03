package com.example.semestralka_vamz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(questionsList0) {
        questionsList0.collect { question ->
            questionsList = question
        }
    }
    questionsList = questionsList.filter { it.quizId == quiz.id }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F0F0))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Späť šípka
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Späť")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Obrázok (zatiaľ len ako box s textom)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("OBRAZOK", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Otázka
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("ahoj")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Odpovede (2x2 grid)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

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




