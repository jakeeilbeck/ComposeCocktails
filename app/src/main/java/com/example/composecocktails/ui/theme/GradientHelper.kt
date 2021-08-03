package com.example.composecocktails.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun gradientDetailsSearch(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colors.secondary,
            MaterialTheme.colors.primary
        )
    )
}

@Composable
fun gradientHeader(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colors.surface,
            MaterialTheme.colors.primaryVariant
        )
    )
}

@Composable
fun gradientBackground(): Brush {
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
fun gradientErrorBackground(): Brush {
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

@Composable
fun gradientBlueBackground(): Brush{
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFF6200EA),
            Color(0xFF304FFE),
            Color(0xFF00BFA5),
        )
    )
}