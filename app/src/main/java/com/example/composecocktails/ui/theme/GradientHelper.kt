package com.example.composecocktails.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

@Composable
fun getGradientDetailsSearch(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colors.secondary,
            MaterialTheme.colors.primary
        )
    )
}

@Composable
fun getGradientHeader(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colors.surface,
            MaterialTheme.colors.primaryVariant
        )
    )
}

@Composable
fun getGradientBackground(): Brush {
    return if (MaterialTheme.colors.isLight){
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.background
            )
        )
    }else{
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colors.background,
                MaterialTheme.colors.background
            )
        )
    }
}

@Composable
fun getGradientErrorBackground(): Brush {
    return if (MaterialTheme.colors.isLight){
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colors.error,
                MaterialTheme.colors.primary,
                MaterialTheme.colors.error
            )
        )
    }else{
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colors.error,
                MaterialTheme.colors.error
            )
        )
    }
}