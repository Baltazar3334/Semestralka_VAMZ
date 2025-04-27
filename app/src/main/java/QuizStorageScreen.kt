
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.BottomNavigationBar

@Composable
fun QuizStorageScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        QuizSection(title = "Basic", quizzes = listOf("QUIZ1", "QUIZ2"))
        QuizSection(title = "Flash Cards", quizzes = listOf("Flash cards 1", "Flash cards 2"))
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(onEditClick = onEditClick, onHomeClick = onHomeClick, onStorageClick = onStorageClick)
    }
}

@Composable
fun QuizSection(title: String, quizzes: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Collapse")
        }

        quizzes.forEach { quiz ->
            QuizItem(title = quiz)
        }
    }
}

@Composable
fun QuizItem(title: String) {
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
            Icon(Icons.Default.Star, contentDescription = "Star")
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title)
                Text(text = "popis", style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.Build, contentDescription = "Score")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}

