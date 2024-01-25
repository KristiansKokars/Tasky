package com.kristianskokars.tasky.lib

import androidx.navigation.NavController
import com.ramcosta.composedestinations.utils.navGraph

fun NavController.currentGraph() = currentBackStackEntry?.navGraph()
