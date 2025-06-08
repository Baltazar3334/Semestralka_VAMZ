package com.example.semestralka_vamz.ui
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@Composable
fun QuizStorageScreen( onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit, onPlayClick: (Quiz) -> Unit, onAlterClick: (Quiz) -> Unit) {
        // funkcia sa pouziva na zobrazenie lozenych kvizov v DB a ich uprava/spustenie/vymazanie/pridanie do favourite
    val context = LocalContext.current // kontext na pracovanie s DB
    val db = AppDatabase.getDatabase(context)
    val quizRepository = QuizRepository(db.quizDao())
    val questionRepository = QuestionRepository(db.questionDao()) // pristup k DB cez repozitar

    val quizList0 = quizRepository.getQuizzes() // list kvizov ulozeny z DB
    var quizList by remember { mutableStateOf<List<Quiz>>(emptyList()) } // list kvizov pre pracvovanie s composable

    val questionsList0 = questionRepository.getQuestions() // list otazok ulozeny z DB
    var questionsList by remember { mutableStateOf<List<Question>>(emptyList()) } // list otazok pre pracovanie s composable

    LaunchedEffect(questionsList0) { // zapisanie otazok do questionList cez asynchronnu operaciu
        questionsList0.collect { question ->
            questionsList = question
        }
    }
    LaunchedEffect(quizList0) { // zapisanie kvizov co quizList cez asynchronnu operaciu
        quizList0.collect { quiz ->
            quizList = quiz
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
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

                QuizSection(title = "Všetky kvízy", quizList, onPlayClick, onAlterClick, true) // vytvorenie QuizSection pre oddelenie kvizov
                Spacer(modifier = Modifier.weight(1f))

            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            BottomNavigationBar( // vyvolanie bottom navigation baru
                onEditClick = onEditClick,
                onHomeClick = onHomeClick,
                onStorageClick = onStorageClick
            )
        }
    }

    BackHandler { // handlovanie vracania sa
        onHomeClick()
    }
}

@Composable
fun QuizSection(title: String,quizzes: List<Quiz>,onPlayClick: (Quiz) -> Unit,onAlterClick: (Quiz) -> Unit, showAlter: Boolean) {
            // funkcia umoznuje zobrazovanie sekcii kvizov pre doplnenie roznych sekcii v buducnosti
    var colapsed by remember { mutableStateOf(false) } //boolean premenna pre pracu s zavretim a otvorenim sekcie


    val sortedQuizzes = remember(quizzes) { // zoradenie kvizov podla toho ci su favourite alebo nie remember pre ulozenie kvizov zo zoznamu len ak sa kvizy zmenia zo strany DB
        quizzes.sortedByDescending { it.favourite }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { colapsed = !colapsed }) { // tlacidlo na zmenu ci je sekcia colapsed alebo nie
                Icon(
                    if (colapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = if (colapsed) "Expand" else "Collapse"
                )
            }
        }

        if (!colapsed) { // logika na spravovanie ci je sekcia colapsed alebo nie
            if (showAlter) { // logika na to ci sa ma zobrazovat aj tlacidlo na upravu kvizu(v menu sa nezobrazuje len v storage screene)
                sortedQuizzes.forEach { quiz ->
                    QuizItem(
                        kviz = quiz,
                        onPlayClick = onPlayClick,
                        onAlterClick = onAlterClick,
                        true // premenna ktora zobrazovanie tlacidla spravuje

                        )
                }
            } else {
                sortedQuizzes.forEach { quiz ->
                    QuizItem(
                        kviz = quiz,
                        onPlayClick = onPlayClick,
                        onAlterClick = onAlterClick,
                        false

                    )
                }
            }

        }
    }
}

@Composable
fun QuizItem(kviz: Quiz, onPlayClick: (Quiz) -> Unit, onAlterClick: (Quiz) -> Unit, showAlter: Boolean) {
    val context = LocalContext.current // kontext na spravovanie DB
    val db = AppDatabase.getDatabase(context)
    val quizRepository = QuizRepository(db.quizDao()) // pristup do kvizov v DB cez repozitar
    var showDeleteDialog by remember { mutableStateOf(false) } // boolean na pracu so zobrazovanim delete dialogu pri vymazani kvizu
    //funckia sluziaca na zobrazenie jednotlivych kvizov do tabulky


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )

    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { // button na pridanie alebo odobratie z favourite, meni ikonku na zaklade toho ci je favourite
                CoroutineScope(Dispatchers.IO).launch { // coroutine na pracu s hodnotou favourite v DB a zmenu UI podla toho
                    val newValue = !kviz.favourite
                    db.quizDao().updateFavourite(kviz.id, newValue)
                }
            } ) {
                if (kviz.favourite) {
                    Icon(Icons.Default.Favorite, contentDescription = "Obľúbený")
                } else {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Nie je obľúbený")
                }

            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = kviz.title)

            }
            IconButton(onClick = { showDeleteDialog = true } ) { // tlacidlo na vymazanie kvizu
                Icon(Icons.Default.Delete, contentDescription = "Vymazat")
            }
            if (showAlter) {
                IconButton(onClick = { onAlterClick(kviz) } ) { // tlacidlo na upravenie kvizu, spusti QuizCreateScreen s tymto kvizom ako parametrom
                    Icon(Icons.Default.Build, contentDescription = "Upravit")
                }
            }
            IconButton(onClick = { // spusti screen QuizPlayScreen s tymto kvizom ako parametrom
                onPlayClick(kviz)
            } ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "hratKviz")
            }
        }
    }
    if (showDeleteDialog) { // logika ktora spravuje popup message pri vymazavani kvizu
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                CoroutineScope(Dispatchers.IO).launch { // coroutine pre vymazanie kvizu z DB
                    quizRepository.deleteQuizById(kviz.id)
                }
            }
        )
    }
}

@Composable
fun DeleteDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    // funckia na zobrazenie popup okna pri vymazavani kvizu
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Naozaj chcete vymazať tento Kvíz?") },
        text = { Text("Vaše zmeny sa nedajú vrátiť.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Áno")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Nie")
            }
        }
    )
}
