package com.whitelext.dotaHunter.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

const val MEDIUM_SHAPE_SIZE = 4
const val LARGE_SHAPE_SEIZE = 0

val Shapes = Shapes(
    small = RoundedCornerShape(MEDIUM_SHAPE_SIZE.dp),
    medium = RoundedCornerShape(MEDIUM_SHAPE_SIZE.dp),
    large = RoundedCornerShape(LARGE_SHAPE_SEIZE.dp)
)
