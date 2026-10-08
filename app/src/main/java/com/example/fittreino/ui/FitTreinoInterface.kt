package com.example.fittreino.ui // Altere para o nome exato do seu pacote se necessário

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// --- ESTRUTURA DE ROTAS DE NAVEGAÇÃO ---
sealed class BottomBarRoute(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarRoute("home", "Treinos", Icons.Default.Home)
    object Search : BottomBarRoute("search", "Buscar API", Icons.Default.Search)
    object Tracker : BottomBarRoute("tracker", "Executar", Icons.Default.PlayArrow)
    object History : BottomBarRoute("history", "Histórico", Icons.Default.DateRange)
}

// --- TELA PRINCIPAL (CONTAINER COM BOTTOM BAR) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitTreinoMainApp() {
    var currentRoute by remember { mutableStateOf<BottomBarRoute>(BottomBarRoute.Home) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FitTreino 💪",
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
                    BottomBarRoute.Home,
                    BottomBarRoute.Search,
                    BottomBarRoute.Tracker,
                    BottomBarRoute.History
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
        },
        floatingActionButton = {
            if (currentRoute == BottomBarRoute.Home) {
                FloatingActionButton(onClick = { /* Ação de Criar Treino */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Criar Treino")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                is BottomBarRoute.Home -> HomeScreen()
                is BottomBarRoute.Search -> SearchApiScreen()
                is BottomBarRoute.Tracker -> TrackerScreen()
                is BottomBarRoute.History -> HistoryScreen()
            }
        }
    }
}

// --- 1. TELA: MEUS TREINOS (HOME) ---
@Composable
fun HomeScreen() {
    val treinosExemplo = listOf(
        "Treino A: Peito e Tríceps" to "4 exercícios • ~45 min",
        "Treino B: Costas e Bíceps" to "5 exercícios • ~50 min",
        "Treino C: Pernas e Ombros" to "6 exercícios • ~60 min"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Minhas Fichas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(treinosExemplo) { (titulo, detalhe) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(titulo, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text(detalhe, fontSize = 13.sp, color = Color.Gray)
                        }
                        Button(onClick = { /* Iniciar */ }) {
                            Text("Iniciar")
                        }
                    }
                }
            }
        }
    }
}

// --- 2. TELA: BUSCAR EXERCÍCIOS (INTEGRAÇÃO COM API) ---
@Composable
fun SearchApiScreen() {
    var query by remember { mutableStateOf("") }
    val exerciciosMock = listOf(
        "Supino Reto" to "Peitoral",
        "Puxada Alta" to "Costas",
        "Agachamento Livre" to "Quadríceps",
        "Rosca Direta" to "Bíceps"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar exercício na API...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(exerciciosMock) { (nome, grupo) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Caixa cinza simulando onde entrará a foto carregada do link da API
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Foto", fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(nome, fontWeight = FontWeight.Medium)
                            Text(grupo, fontSize = 12.sp, color = Color.Gray)
                        }

                        IconButton(onClick = { /* Adicionar */ }) {
                            Icon(Icons.Default.Add, contentDescription = "Adicionar")
                        }
                    }
                }
            }
        }
    }
}

// --- 3. TELA: EXECUÇÃO DO TREINO ---
@Composable
fun TrackerScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Executando: Treino A", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Supino Reto com Barra", color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        // Card de Séries
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Séries & Carga", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                repeat(3) { index ->
                    var peso by remember { mutableStateOf("20") }
                    var reps by remember { mutableStateOf("10") }
                    var concluida by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Série ${index + 1}")

                        OutlinedTextField(
                            value = peso,
                            onValueChange = { peso = it },
                            label = { Text("kg") },
                            modifier = Modifier.width(70.dp)
                        )

                        OutlinedTextField(
                            value = reps,
                            onValueChange = { reps = it },
                            label = { Text("reps") },
                            modifier = Modifier.width(70.dp)
                        )

                        FilterChip(
                            selected = concluida,
                            onClick = { concluida = !concluida },
                            label = {
                                if (concluida) Icon(Icons.Default.Check, contentDescription = null)
                                else Text("Ok")
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Salvar treino no Room */ },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Finalizar e Salvar Treino")
        }
    }
}

// --- 4. TELA: HISTÓRICO ---
@Composable
fun HistoryScreen() {
    val historicoExemplo = listOf(
        "Ontem, 18:30" to "Treino A: Peito e Tríceps",
        "24/10/2026" to "Treino B: Costas e Bíceps",
        "22/10/2026" to "Treino C: Pernas"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Histórico de Registros", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(historicoExemplo) { (data, treino) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(data, fontSize = 12.sp, color = Color.Gray)
                        Text(treino, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

