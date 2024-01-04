package com.example.i_prep.presentation.navigation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavIcon(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    contentDescription: String,
) {
//    val animationRotation by animateFloatAsState(
//        targetValue = if (isSelected) 360f else 0f,
//        animationSpec = spring(
//            stiffness = Spring.StiffnessLow,
//            dampingRatio = Spring.DampingRatioMediumBouncy
//        )
//    )

    Box(
        modifier = modifier
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                RoundedCornerShape(24.dp)
            )
            .width(60.dp)
            .padding(3.dp),
//        .graphicsLayer { rotationZ = animationRotation }
        contentAlignment = Alignment.Center
    ) {
        Icon(
//            rememberVectorPainter(
//                image = if (animationRotation > 90f) selectedIcon else unselectedIcon
//            ),
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = contentDescription,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = modifier.size(26.dp)
        )
    }
}