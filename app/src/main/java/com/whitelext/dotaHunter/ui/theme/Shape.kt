package com.whitelext.dotaHunter.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

const val mediumShapeSize = 4
const val largeShapeSize = 0

val Shapes = Shapes(
    small = RoundedCornerShape(mediumShapeSize.dp),
    medium = RoundedCornerShape(mediumShapeSize.dp),
    large = RoundedCornerShape(largeShapeSize.dp)
)
