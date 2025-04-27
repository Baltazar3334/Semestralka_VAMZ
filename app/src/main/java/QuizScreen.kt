import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.BottomNavigationBar

@Composable
fun CreateQuizScreen(onEditClick: () -> Unit, onHomeClick: () -> Unit, onStorageClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Meno
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Meno") },
                placeholder = { Text("...") },
                modifier = Modifier.fillMaxWidth()
            )

            // Časové obmedzenie
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Časové obmedzenie")
                Switch(checked = true, onCheckedChange = {})
            }

            // Slider na minúty
            Column {
                Text("minút")
                Slider(
                    value = 0f,
                    onValueChange = {},
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

            // Otázky
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuestionItem("Otázka 1")
                QuestionItem("Otázka 2")
                QuestionItemWithDescription("Otázka 3", "Popis", "popis")
            }
        }

        // Bottom navigation
        BottomNavigationBar(onEditClick = onEditClick, onHomeClick = onHomeClick, onStorageClick = onStorageClick)
    }
}

    @Composable
fun QuestionItem(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(title)
            IconButton(onClick = { /* Move up */ }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move up")
            }
            IconButton(onClick = { /* Delete */ }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun QuestionItemWithDescription(title: String, popis1: String, popis2: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title)
                    Text(popis1)
                    Text(popis2)
                }
                IconButton(onClick = { /* Download */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Download")
                }
            }
        }
    }
}

