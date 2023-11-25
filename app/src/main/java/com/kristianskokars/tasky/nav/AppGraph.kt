package com.kristianskokars.tasky.nav

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class AppGraph(
    val start: Boolean = false
)
