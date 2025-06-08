package com.example.semestralka_vamz.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.data.database.AppDatabase
import com.example.semestralka_vamz.data.database.Repository.QuizRepository
import com.example.semestralka_vamz.data.database.Repository.StatsRepository
import com.example.semestralka_vamz.data.database.entity.Quiz
import com.example.semestralka_vamz.ui.theme.SemestralkaTheme

//Hlavná trieda ktorá prepína medzi obrazovkami
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SemestralkaTheme {
                var currentScreen by remember { mutableStateOf("menu") } //obrazovka na ktorej sa aplikacia nachadza
                var showExitDialog by remember { mutableStateOf(false) } // na zobrazenie popup exit
                var showExitDialogToDatabase by remember { mutableStateOf(false) } // popup pre exit z databazy
                var selectedQuiz by remember { mutableStateOf<Quiz?>(null) } //kviz ktorý je aktualne vybraty v aplikacia(napr. na vyplnenie)
                var transitionDirection by remember { mutableStateOf(1) } //-1 alebo 1, mení smer do ktorého sa prechod posúva
                var correctCount by remember { mutableStateOf(0) } // pocet spravnych odpovedí
                var totalCount by remember { mutableStateOf(0) } // pocet nespravnych odpovedí

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        slideInHorizontally { fullWidth -> fullWidth * transitionDirection } + fadeIn() with
                                slideOutHorizontally { fullWidth -> -fullWidth * transitionDirection } + fadeOut()
                    },
                    label = "Screen Transition"
                ) { screen ->
                    when (screen) {    //logika menenia obrazoviek na zaklade premennej currentScreen
                        "menu" -> MenuScreen(
                            onEditClick = {   // po spusteni tohto onClick sa spustia tieto prikazy
                                selectedQuiz = null // null pre to aby sa hodnota vynulovala pri prechode na iny screen
                                currentScreen = "createQuiz"
                                transitionDirection = 1
                            },
                            onHomeClick = { currentScreen = "menu" },
                            onStorageClick = {
                                currentScreen = "storage"
                                transitionDirection = -1
                            },
                            onPlayClick = { quiz ->
                                selectedQuiz = quiz // tu sa selected quiz, ktorý je priradený podla QuizItem na ktorý klikneme, spusti cez playQuiz
                                currentScreen = "playQuiz"
                                transitionDirection = -1
                            },
                            onSettingsClick = {
                                currentScreen = "settings"
                            }

                        )
                        "settings" -> SettingsScreen(
                            onReturnClick = { currentScreen = "menu" }
                        )
                        "finish" -> FinishedQuizScreen(
                            correctAnswers = correctCount,
                            totalQuestions = totalCount,
                            onRetryClick = {
                                currentScreen = "playQuiz"
                            },
                            onHomeClick = { currentScreen = "menu" }
                        )
                        "createQuiz" -> CreateQuizScreen(
                            existingQuiz = selectedQuiz,
                            onHomeClick = {
                                showExitDialog = true
                                transitionDirection = -1
                            },
                            onHomeClickNoPopUp = {
                                currentScreen = "menu"
                                transitionDirection = -1
                            },
                            onEditClick = { currentScreen = "createQuiz" },
                            onStorageClick = {
                                showExitDialog = true
                                showExitDialogToDatabase = true
                                transitionDirection = -1
                            }
                        )
                        "storage" -> QuizStorageScreen(

                            onEditClick = {
                                selectedQuiz = null
                                currentScreen = "createQuiz"
                                transitionDirection = 1
                            },
                            onHomeClick = {
                                currentScreen = "menu"
                                transitionDirection = 1
                            },
                            onStorageClick = { currentScreen = "storage" },
                            onPlayClick = { quiz ->
                                selectedQuiz = quiz
                                currentScreen = "playQuiz"
                                transitionDirection = -1
                            },
                            onAlterClick = { quiz ->
                                selectedQuiz = quiz
                                currentScreen = "createQuiz"
                            }
                        )
                        "playQuiz" -> selectedQuiz?.let { quiz ->
                            PlayQuizScreen(
                                onEditClick = {
                                    selectedQuiz = null
                                    showExitDialog = true
                                    transitionDirection = 1
                                },
                                onHomeClick = {
                                    showExitDialog = true
                                    transitionDirection = 1
                                },
                                onStorageClick = {
                                    showExitDialog = true
                                    showExitDialogToDatabase = true
                                    transitionDirection = 1
                                },
                                quiz = quiz,
                                onBack = {
                                    showExitDialog = true
                                    showExitDialogToDatabase = true
                                    transitionDirection = 1
                                },
                                KickOutNoQuiz = { currentScreen = "storage" },
                                onDoneClick = { correctAnswers, totalQuestions ->
                                    correctCount = correctAnswers
                                    totalCount = totalQuestions
                                    currentScreen = "finish" }
                            )
                        }
                    }
                }
                if (showExitDialog) {  //logika aby pri urcitych obrazovkach sa nedalo nahodou odist bez potvrdenia
                    if (!showExitDialogToDatabase) {
                        ExitDialog(
                            onDismiss = { showExitDialog = false },
                            onConfirm = {
                                showExitDialog = false
                                currentScreen = "menu"
                            }
                        )
                    } else { //dva boolean aby pri stlaceni homescreen sa slo na homescreen a pri stlaceni storage sa slo na storage screen
                        ExitDialog(
                            onDismiss = { showExitDialog = false },
                            onConfirm = {
                                showExitDialog = false
                                showExitDialogToDatabase = false
                                currentScreen = "storage"
                            }
                        )
                    }

                }

            }

        }
    }
}

@Composable
fun MenuScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit, onPlayClick: (Quiz) -> Unit, onSettingsClick: () -> Unit) {
 // obrazovka zakladného menu ktoré sa zobrazi ked sa otvori aplikacia
    val context = LocalContext.current // context pre pracovanie s databázou
    val db = AppDatabase.getDatabase(context) // vyvolanie databazy a jendotlivých repozitárov
    val statsRepository = StatsRepository(db.userStatsDao())
    val quizRepository = QuizRepository(db.quizDao())
    val statsState by statsRepository.statsFlow.collectAsState(initial = null) //nacitanie flow statistik do hodnoty
    val stats = statsState //prepis do hodnoty pre lahsiu pracu
    val allQuizzes by quizRepository.getQuizzes().collectAsState(initial = emptyList()) // nacitanie kvizov do hodnoty
    val lastQuiz = allQuizzes.find { it.id == statsState?.lastQuizId } // vyfiltrovanie posledneho kvízu
    val activity = (LocalContext.current as? Activity) // ulozenie aktualnej aktivity pre tlacidlo exit
    val favouriteQuizzes = allQuizzes.filter { it.favourite } // filtrovanie len oblubenych kvizov


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 25.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dobrý Deň,",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Vaše štatistiky",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (stats == null) {       //logika pre zapisovanie statistik o uzivatelovi
                        Text("Žiadne údaje zatiaľ nie sú dostupné.")
                    } else {
                        val successRate = if (stats.totalQuestionsAnswered > 0)
                            (stats.totalCorrectAnswers * 100 / stats.totalQuestionsAnswered)
                        else 0
                        Text("Dokončené kvízy: ${stats.totalQuizzesCompleted}")
                        Text("Perfektné skóre: ${stats.perfectScores}")
                        Text("Úspešnosť: $successRate %")
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
                    .clickable(enabled = lastQuiz != null)  //  aby sa na kartu s poslednym kvizom dalo kliknut a zapnut ten kviz
                    { lastQuiz?.let { onPlayClick(it) } },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Posledný kvíz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (lastQuiz != null) {         // logika pre zobrazenie posledneho kvizu ktory bol zapnuty
                        Text(lastQuiz.title)
                    } else {
                        Text("Nemáte posledný dokončený kvíz.")
                    }

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clickable { onSettingsClick() },   //povolene klinutie na tlacidlo settings ktore meni obrazovku
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Nastavenia", modifier = Modifier.size(60.dp))
                    }
                }

                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clickable { activity?.finish() }, // tlacidlo EXIT ktore vypne aktivity a kedze to je main activity tak sa vypne aplikacia
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Exit", modifier = Modifier.size(60.dp))
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.height(200.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    QuizSection("Obľúbené Kvízy", favouriteQuizzes, onPlayClick, onAlterClick = {} , showAlter = false)
                }
            }
        }
        BottomNavigationBar(onEditClick = onEditClick, onHomeClick = onHomeClick, onStorageClick = onStorageClick) // vyvolanie dolneho navygacneho baru
    }
}

@Composable
fun BottomNavigationBar(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit) {
    //navygacny bar na dolnej strane obrazovky
    Surface(
        tonalElevation = 10.dp,
        shadowElevation = 18.dp,
        color = Color(0xFFeeeeee),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onStorageClick() }) {    // tlacidla na liste ktore menia obrazovku
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Documents",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = { onHomeClick() }) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = { onEditClick() }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Favorites",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun ExitDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    //exit dialog pre odchod zo screenu, aktivuje sa len pre urcite screeny
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Naozaj chcete odísť?") },
        text = { Text("Vaše zmeny nebudú uložené.") },
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



