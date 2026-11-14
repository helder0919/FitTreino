package com.example.fittreino.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarRoute(val route: String, val title: String, val icon: ImageVector) {
    object LuizEdsonWorkspace : BottomBarRoute("luiz_edson_room", "Luiz Edson - Room", Icons.Default.FitnessCenter)
    object LuizEdsonApi : BottomBarRoute("luiz_edson_api", "Luiz Edson - API", Icons.Default.Api)
    object HolderViniciusWorkspace : BottomBarRoute("helder_vinicius_room", "Hélder Vinícius - Room", Icons.Default.Fastfood)
    object HolderViniciusApi : BottomBarRoute("helder_vinicius_api", "Hélder Vinícius - API", Icons.Default.List)
}
