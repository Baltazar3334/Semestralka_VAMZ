package com.example.semestralka_vamz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.ui.theme.Semestralka_VAMZTheme
import androidx.compose.ui.text.font.FontWeight

val mainFont = FontFamily(
    Font(R.font.lato_black)
)






class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Semestralka_VAMZTheme {
                MenuScreen()
            }
        }
    }
}

@Composable
fun MenuScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
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
            MenuCard(title = "Posledný kvíz", Modifier, 180, 0, "posledny_kviz")
            MenuCard(title = "Rozrobeny Kvíz", Modifier, 180, 0, "posledny_kviz")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard(title = "Obľúbené", modifier = Modifier.weight(1f), 140, 80, "")
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { /* Nastavenia */ },
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Nastavenia",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        BottomNavigationBar()
    }
}

@Composable
fun MenuCard(title: String, modifier: Modifier = Modifier, height: Int, width: Int, obrazok: String) {

    val widthModifier = if (width == 0) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.width(width.dp)
    }

    val context = LocalContext.current
    val imageId = context.resources.getIdentifier(obrazok, "drawable", context.packageName)
    val imagePainter = if (imageId != 0) {
        painterResource(id = imageId)
    } else {
        painterResource(id = R.drawable.posledny_kviz)
    }

    Card(
        modifier = modifier
            .then(widthModifier)
            .height(height.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(10.dp)

    ) {
        if (imageId != 0){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(15.dp)
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )


                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = title, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = mainFont)
                    Text(text = "Autor", fontSize = 12.sp, color = Color.Gray)


                }


            }
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Popis", fontSize = 13.sp, color = Color.Gray,modifier = Modifier.padding(15.dp) )
            }

        }else{
            Box(
                contentAlignment = Alignment.Center, // Centrovanie obsahu v Boxe
                modifier = Modifier.fillMaxSize() // Vyplní celý priestor karty
            ) {
                Text(
                    text = title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = mainFont,
                    modifier = Modifier.align(Alignment.Center) // Zarovnanie textu na stred
                )
            }
        }

    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .width(400.dp)
            .height(100.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* do something */ }) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Documents",
                modifier = Modifier.size(48.dp)

            )
        }
        IconButton(onClick = { /* do something */ }) {
            Icon(
                Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier.size(48.dp)
            )
        }
        IconButton(onClick = { /* do something */ }) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Favorites",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

