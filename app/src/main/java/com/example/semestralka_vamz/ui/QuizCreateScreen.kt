package com.example.semestralka_vamz.ui
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.data.database.AppDatabase
import com.example.semestralka_vamz.data.database.Repository.QuestionRepository
import com.example.semestralka_vamz.data.database.Repository.QuizRepository
import com.example.semestralka_vamz.data.database.entity.Question
import com.example.semestralka_vamz.data.database.entity.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch


@Composable
fun CreateQuizScreen(existingQuiz: Quiz? = null, onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit, onHomeClickNoPopUp: () -> Unit) {
    // obrazovka pre vytvaranie kvizov alebo upravu kvizov, hodnoty kvizu sa ukladaju ako premenne a nasledne sa nahravaju do databazy v celku
    var quizName by remember { mutableStateOf("") } //meno kvizu
    var timeLimit by remember { mutableStateOf(0f) } // casove obmedzenie kvizu
    val keyboardController = LocalSoftwareKeyboardController.current // ovladac pre klavesnicu, aby sa dala otvorit
    var isTimeLimitEnabled by remember { mutableStateOf(false) } // hodnota ci je kviz casovo obmedzeny
    val questions2 = remember { mutableStateListOf<QuestionData>() } // zoznam otazok tohto kvizu

    var existingQuizQuestions by remember { mutableStateOf<List<Question>>(emptyList()) } // zoznam otazok ak je obrazovka nacitana uz s existujucim kvizom

    val context = LocalContext.current // pristup do databazy kvizov a otazok
    val db = AppDatabase.getDatabase(context)
    val quizRepository = QuizRepository(db.quizDao())
    val questionRepository = QuestionRepository(db.questionDao())



    BackHandler { //backhandler aby aplikacia vedela co ma robit ked je stlacene back na telefone
        onHomeClick()
    }




        LaunchedEffect(Unit) { // asynchronna operacia na spustenie kodu ktory intereaguje s databazou
            if (existingQuiz != null) { // ak bola funkcia volana na upravenie nejakeho kvizu tak sa data na obrazovke automaticky vyplnia
                println("načítal sa quiz")
                quizName = existingQuiz.title
                timeLimit = existingQuiz.timeLimit.toFloat()
                isTimeLimitEnabled = existingQuiz.timeLimitOn
                questionRepository.getQuestions() // vyberie otazky z databazy ako entity Question
                    .take(1) // zoberie len prvu emisiu flow a potom sa vypne
                    .collect { allQuestions ->
                        existingQuizQuestions = allQuestions.filter { it.quizId == existingQuiz.id } //filtruje otazky len tie ktore maju QuizId rovnake ako id kvizu z parametrov

                        questions2.clear()
                        for (question in existingQuizQuestions) {
                            val answersList = mutableStateListOf<String>().apply { // pre kazdu otazku sa vytvori zoznam odpovedi
                                question.answer1?.let { add(it) }
                                question.answer2?.let { add(it) }
                                question.answer3?.let { add(it) }
                            }

                            questions2.add( // nasledne sa otazky aj s odpovedami nahraju do zoznamu questions2 teraz uz ako QuestionData data class instancie
                                QuestionData(
                                    title = "Otázka " + questions2.size.toString(),
                                    question = mutableStateOf(question.question),
                                    correctAnswer = mutableStateOf(question.correctAnswer),
                                    answers = answersList,
                                    isTimeLimitEnabled = mutableStateOf(existingQuiz.timeLimitOn),
                                    favourite = mutableStateOf(false)
                                )
                            )
                        }
                    }
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
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF000000),
                        unfocusedTextColor = Color(0xFF000000),
                        cursorColor = Color(0xFF1E3A8A),
                        focusedBorderColor = Color(0xFF1E3A8A),
                        unfocusedBorderColor = Color(0x80274690),
                        focusedLabelColor = Color(0xFF1E3A8A),
                        unfocusedLabelColor = Color(0xFF000000),
                        focusedContainerColor = Color(0xFFFFFFFF),
                        unfocusedContainerColor = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { keyboardController?.show() }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch( // tlacidlo pre zobrazenie posuvnika pre casove obmedzenie, zaroven tlacidlo zmeni hodnotu ci je viz casovo obmedzeny
                        checked = isTimeLimitEnabled,
                        onCheckedChange = { isTimeLimitEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF1E3A8A),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFCCCCCC),
                            uncheckedBorderColor = Color(0xFF888888),
                            checkedBorderColor = Color(0xFF1E3A8A)
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Časové obmedzenie")

                }

                if (isTimeLimitEnabled) { //logika pre zobrazovanie posuvnika pre casove obmedzenie
                    Column {
                        Text("${timeLimit.toInt()} min")
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
                    ) {
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
                                IconButton(onClick = { //tlacidlo na pridanie otazky do kvizu
                                    questions2.add(QuestionData(title = "Otázka ${questions2.size + 1}"))

                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add question")
                                }
                                Text(
                                    "Pridať Otázku",
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )
                                if(existingQuiz != null) { // if podmienka pre to ci sa ma vytvorit novy kviz alebo len upravit uz existujuci kviz, spravuje to nepovinnym parametrom
                                    IconButton(onClick = { CoroutineScope(Dispatchers.IO).launch { //tlacidlo na ulozenie kvizu do databazy vyvola funkciu saveQuizAndQuestions
                                        saveQuizAndQuestions(                                      // tlacidlo vyvolava coroutine ktora spravuje interakcie medzi databazou a funkciou cez Dispatcher.IO
                                            quizName, timeLimit, isTimeLimitEnabled,
                                            questions2, quizRepository, questionRepository, false, existingQuiz
                                        )
                                        onHomeClickNoPopUp() // nasledne sa obrazovka zmeni na uvodne menu
                                    } } ) {
                                        Icon(Icons.Default.Send, contentDescription = "Finish quiz")
                                    }
                                } else {
                                    IconButton(onClick = { CoroutineScope(Dispatchers.IO).launch {
                                        saveQuizAndQuestions(
                                            quizName, timeLimit, isTimeLimitEnabled,
                                            questions2, quizRepository, questionRepository, false
                                        )
                                        onHomeClickNoPopUp()
                                    } } ) {
                                        Icon(Icons.Default.Send, contentDescription = "Finish quiz")
                                    }
                                }


                            }
                        }
                    }

                    questions2.forEachIndexed { index, data -> //zobrazenie otazok na UI pomocou funkcie QuestionItem
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
            BottomNavigationBar( //vytvorenie bottomNavigationBaru
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
    //funkcia QuestionItem sluzi na zobrazenie jednotlivych otazok na obrazovke a ich upravu
    val keyboardController = LocalSoftwareKeyboardController.current // zobrazovanie klavesnice
    var isExpanded by remember { mutableStateOf(false) } // tato composable funkcia ma dva stadia: rozsirena a nerozsirena pre jednoduchsie zobrazovanie velkeho mnozstva otazok

    if (!isExpanded) { //ked neni rozsirena
        Card(
            modifier = Modifier.
            fillMaxWidth()
                .padding(4.dp)
                .height(60.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(7.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
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
    } else { // ked je rozsirena
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(7.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
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
                            .clickable { keyboardController?.show() },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF000000),
                            unfocusedTextColor = Color(0xFF000000),
                            cursorColor = Color(0xFF1E3A8A),
                            focusedIndicatorColor = Color(0xFF1E3A8A),
                            unfocusedIndicatorColor = Color(0x80274690),
                            focusedLabelColor = Color(0xFF1E3A8A),
                            unfocusedLabelColor = Color(0xFF000000),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedContainerColor = Color(0xFFFFFFFF)
                        ),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("○", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                    TextField(
                        value = data.correctAnswer.value,
                        onValueChange = { data.correctAnswer.value = it },
                        label = { Text("Správna odpoveď") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { keyboardController?.show() },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF000000),
                            unfocusedTextColor = Color(0xFF000000),
                            cursorColor = Color(0xFF1E3A8A),
                            focusedIndicatorColor = Color(0xFF1E3A8A),
                            unfocusedIndicatorColor = Color(0x80274690),
                            focusedLabelColor = Color(0xFF1E3A8A),
                            unfocusedLabelColor = Color(0xFF000000),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedContainerColor = Color(0xFFFFFFFF)
                        ),
                    )
                }
                data.answers.forEachIndexed { index, answer ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("○", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                        TextField(
                            value = answer,
                            onValueChange = {
                                data.answers[index] = it
                            },
                            label = { Text("Odpoveď ${index + 1}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { keyboardController?.show() },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF000000),
                                unfocusedTextColor = Color(0xFF000000),
                                cursorColor = Color(0xFF1E3A8A),
                                focusedIndicatorColor = Color(0xFF1E3A8A),
                                unfocusedIndicatorColor = Color(0x80274690),
                                focusedLabelColor = Color(0xFF1E3A8A),
                                unfocusedLabelColor = Color(0xFF000000),
                                focusedContainerColor = Color(0xFFFFFFFF),
                                unfocusedContainerColor = Color(0xFFFFFFFF)
                            ),
                        )
                    }
                }
                Row(

                ){
                    IconButton(onClick = { // tlacidlo na pridavanie odpovedi, maximalne 4 odpovede
                        if (data.answers.size < 3){
                            data.answers.add("")
                        } else {

                        }
                    } ) {
                        Icon(Icons.Default.Add, contentDescription = "Add answer")
                    }
                    IconButton(onClick = {
                        if (data.answers.size >= 1){
                            data.answers.removeAt(data.answers.lastIndex)
                        }
                    } ) {
                        Icon(Icons.Default.Delete, contentDescription = "delete answer")
                    }
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
    questionRepository: QuestionRepository,
    favourite: Boolean,
    existingQuiz: Quiz? = null
) {
    //funkcia sluzi na ulozenie dat kvizu do databazy, podla nepovinneho parametru sa rozhoduje ci musi najskor odstranit nejaky kviz a potom ho nahradit alebo ci len pridava novy kviz do DB
    if (existingQuiz != null) {
        quizRepository.deleteQuizById(existingQuiz.id)
    }
    val newQuiz = Quiz(
        title = quizName,
        timeLimit = timeLimit.toInt(),
        timeLimitOn = isTimeLimitEnabled,
        favourite = favourite
    )
    val newQuizId = quizRepository.addQuizReturningId(newQuiz)
    questions.forEach { data ->
        val question = Question(
            quizId = newQuizId,
            question = data.question.value,
            correctAnswer = data.correctAnswer.value,
            answer1 = data.answers.getOrNull(0),
            answer2 = data.answers.getOrNull(1),
            answer3 = data.answers.getOrNull(2)
        )
        questionRepository.addQuestion(question)
    }
}



data class QuestionData( //data class pre otazky v tomto screene pouziva sa pre jednoduchsie upravovanie dat kvizu pred nahratim do DB
    var title: String = "",
    var question: MutableState<String> = mutableStateOf(""),
    var correctAnswer: MutableState<String> = mutableStateOf(""),
    var answers: SnapshotStateList<String> = mutableStateListOf(""),
    var isTimeLimitEnabled: MutableState<Boolean> = mutableStateOf(true),
    var favourite: MutableState<Boolean> = mutableStateOf(false)
)
