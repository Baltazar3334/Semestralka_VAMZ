package com.example.semestralka_vamz.ui

import android.content.Context
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.data.database.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen (onReturnClick: () -> Unit, context: Context = LocalContext.current) {
    var showDBDeleteDialog by remember { mutableStateOf(false) }
    val db = remember { AppDatabase.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 50.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onReturnClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Späť", modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Nastavenia", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        }
        Divider(
            thickness = 2.dp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { showDBDeleteDialog = true },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Resetovať Databázu", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

        }
        Divider(
            thickness = 2.dp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    if (showDBDeleteDialog) {
        DeleteDBDialog(
            onConfirm = {
                showDBDeleteDialog = false
                coroutineScope.launch {
                    db.quizDao().deleteAll()
                    db.userStatsDao().deleteStats()
                }
            },
            onDismiss = { showDBDeleteDialog = false }
        )
    }

}

@Composable
fun DeleteDBDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Naozaj chcete vymazať databázu kvízov?") },
        text = { Text("Vaše zmeny sa nebudú dať vrátiť.") },
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