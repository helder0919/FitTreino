package com.example.fittreino.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittreino.data.FitTreinoDatabase
import com.example.fittreino.data.MemberAWorkoutEntity
import com.example.fittreino.data.MemberBMealEntity
import com.example.fittreino.data.api.MemberATodoResponse
import com.example.fittreino.data.api.MemberBPostResponse
import com.example.fittreino.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class BottomBarRoute(val route: String, val title: String, val icon: ImageVector) {
    object MemberAWorkspace : BottomBarRoute("member_a_room", "Luiz Edson - Room", Icons.Default.FitnessCenter)
    object MemberAApi : BottomBarRoute("member_a_api", "Luiz Edson - API", Icons.Default.Api)
    object MemberBWorkspace : BottomBarRoute("member_b_room", "Hélder Vinícius - Room", Icons.Default.Fastfood)
    object MemberBApi : BottomBarRoute("member_b_api", "Hélder Vinícius - API", Icons.Default.List)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitTreinoMainApp() {
    var currentRoute by remember { mutableStateOf<BottomBarRoute>(BottomBarRoute.MemberAWorkspace) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FitTreino • Requisitos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomBarRoute.MemberAWorkspace,
                    BottomBarRoute.MemberAApi,
                    BottomBarRoute.MemberBWorkspace,
                    BottomBarRoute.MemberBApi
                )

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item,
                        onClick = { currentRoute = item },
                        label = { Text(item.title) },
                        icon = { Icon(item.icon, contentDescription = item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                is BottomBarRoute.MemberAWorkspace -> MemberAWorkspaceScreen()
                is BottomBarRoute.MemberAApi -> MemberAApiScreen()
                is BottomBarRoute.MemberBWorkspace -> MemberBWorkspaceScreen()
                is BottomBarRoute.MemberBApi -> MemberBApiScreen()
            }
        }
    }
}

@Composable
fun MemberAWorkspaceScreen() {
    val context = LocalContext.current.applicationContext
    val db = remember { FitTreinoDatabase.getInstance(context) }
    var workouts by remember { mutableStateOf<List<MemberAWorkoutEntity>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val dao = db.memberAWorkoutDao()
            if (dao.count() == 0) {
                dao.insertAll(
                    listOf(
                        MemberAWorkoutEntity(name = "Supino Ajustado", muscleGroup = "Peito", duration = "35 min"),
                        MemberAWorkoutEntity(name = "Remada Curvada", muscleGroup = "Costas", duration = "30 min"),
                        MemberAWorkoutEntity(name = "Agachamento Livre", muscleGroup = "Pernas", duration = "40 min")
                    )
                )
            }
            workouts = dao.getAll()
        }
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Luiz Edson — dados locais com Room", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(workouts) { workout ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(workout.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Grupo muscular: ${workout.muscleGroup}", color = Color.Gray)
                        Text("Duração: ${workout.duration}", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun MemberAApiScreen() {
    var todos by remember { mutableStateOf<List<MemberATodoResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            todos = RetrofitClient.service.getMemberATodos().take(5)
        }
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Luiz Edson — API externa via Retrofit", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(todos) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("#${item.id} • ${item.title}", fontWeight = FontWeight.SemiBold)
                        Text("Status: ${if (item.completed) "Concluído" else "Pendente"}")
                        Text("userId: ${item.userId}", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun MemberBWorkspaceScreen() {
    val context = LocalContext.current.applicationContext
    val db = remember { FitTreinoDatabase.getInstance(context) }
    var meals by remember { mutableStateOf<List<MemberBMealEntity>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val dao = db.memberBMealDao()
            if (dao.count() == 0) {
                dao.insertAll(
                    listOf(
                        MemberBMealEntity(name = "Salada de Frango", mealType = "Almoço", calories = "420 kcal"),
                        MemberBMealEntity(name = "Omelete com Vegetais", mealType = "Café da manhã", calories = "310 kcal"),
                        MemberBMealEntity(name = "Iogurte com Granola", mealType = "Lanche", calories = "260 kcal")
                    )
                )
            }
            meals = dao.getAll()
        }
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hélder Vinícius — dados locais com Room", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(meals) { meal ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(meal.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Tipo: ${meal.mealType}", color = Color.Gray)
                        Text("Calorias: ${meal.calories}", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun MemberBApiScreen() {
    var posts by remember { mutableStateOf<List<MemberBPostResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            posts = RetrofitClient.service.getMemberBPosts().take(5)
        }
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hélder Vinícius — API externa via Retrofit", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(posts) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("#${item.id} • ${item.title}", fontWeight = FontWeight.SemiBold)
                        Text(item.description, color = Color.Gray)
                        Text("userId: ${item.userId}", color = Color.Gray)
                    }
                }
            }
        }
    }
}

