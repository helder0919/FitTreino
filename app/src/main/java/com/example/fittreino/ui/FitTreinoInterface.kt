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
import com.example.fittreino.data.LuizEdsonWorkoutEntity
import com.example.fittreino.data.HelderViniciusMealEntity
import com.example.fittreino.data.api.LuizEdsonTodoResponse
import com.example.fittreino.data.api.HelderViniciusPostResponse
import com.example.fittreino.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class BottomBarRoute(val route: String, val title: String, val icon: ImageVector) {
    object LuizEdsonWorkspace : BottomBarRoute("luiz_edson_room", "Luiz Edson - Room", Icons.Default.FitnessCenter)
    object LuizEdsonApi : BottomBarRoute("luiz_edson_api", "Luiz Edson - API", Icons.Default.Api)
    object HolderViniciusWorkspace : BottomBarRoute("helder_vinicius_room", "Hélder Vinícius - Room", Icons.Default.Fastfood)
    object HolderViniciusApi : BottomBarRoute("helder_vinicius_api", "Hélder Vinícius - API", Icons.Default.List)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitTreinoMainApp() {
    var currentRoute by remember { mutableStateOf<BottomBarRoute>(BottomBarRoute.LuizEdsonWorkspace) }

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
                    BottomBarRoute.LuizEdsonWorkspace,
                    BottomBarRoute.LuizEdsonApi,
                    BottomBarRoute.HolderViniciusWorkspace,
                    BottomBarRoute.HolderViniciusApi
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
                is BottomBarRoute.LuizEdsonWorkspace -> LuizEdsonWorkspaceScreen()
                is BottomBarRoute.LuizEdsonApi -> LuizEdsonApiScreen()
                is BottomBarRoute.HolderViniciusWorkspace -> HolderViniciusWorkspaceScreen()
                is BottomBarRoute.HolderViniciusApi -> HolderViniciusApiScreen()
            }
        }
    }
}

@Composable
fun LuizEdsonWorkspaceScreen() {
    val context = LocalContext.current.applicationContext
    val db = remember { FitTreinoDatabase.getInstance(context) }
    var workouts by remember { mutableStateOf<List<LuizEdsonWorkoutEntity>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val dao = db.luizEdsonWorkoutDao()
            if (dao.count() == 0) {
                dao.insertAll(
                    listOf(
                        LuizEdsonWorkoutEntity(name = "Supino Ajustado", muscleGroup = "Peito", duration = "35 min"),
                        LuizEdsonWorkoutEntity(name = "Remada Curvada", muscleGroup = "Costas", duration = "30 min"),
                        LuizEdsonWorkoutEntity(name = "Agachamento Livre", muscleGroup = "Pernas", duration = "40 min")
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
fun LuizEdsonApiScreen() {
    var todos by remember { mutableStateOf<List<LuizEdsonTodoResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val result = withContext(Dispatchers.IO) {
                RetrofitClient.service.getLuizEdsonTodos()
            }
            todos = result.take(5)
        } catch (e: Exception) {
            errorMessage = "Não foi possível carregar os dados da API. Verifique a conexão e tente novamente."
        } finally {
            loading = false
        }
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

        if (errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = errorMessage!!,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
            return
        }

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
fun HolderViniciusWorkspaceScreen() {
    val context = LocalContext.current.applicationContext
    val db = remember { FitTreinoDatabase.getInstance(context) }
    var meals by remember { mutableStateOf<List<HelderViniciusMealEntity>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val dao = db.helderViniciusMealDao()
            if (dao.count() == 0) {
                dao.insertAll(
                    listOf(
                        HelderViniciusMealEntity(name = "Salada de Frango", mealType = "Almoço", calories = "420 kcal"),
                        HelderViniciusMealEntity(name = "Omelete com Vegetais", mealType = "Café da manhã", calories = "310 kcal"),
                        HelderViniciusMealEntity(name = "Iogurte com Granola", mealType = "Lanche", calories = "260 kcal")
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
fun HolderViniciusApiScreen() {
    var posts by remember { mutableStateOf<List<HelderViniciusPostResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val result = withContext(Dispatchers.IO) {
                RetrofitClient.service.getHelderViniciusPosts()
            }
            posts = result.take(5)
        } catch (e: Exception) {
            errorMessage = "Não foi possível carregar os dados da API. Verifique a conexão e tente novamente."
        } finally {
            loading = false
        }
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

        if (errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = errorMessage!!,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
            return
        }

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

