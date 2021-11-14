package com.whitelext.dotaHunter.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.Transparent
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Utils

object CommonComponents {

    @Composable
    fun ProfilePhoto(id: String?) {
        Image(
            painter = rememberImagePainter(Utils.getAvatarUrl(id)),
            contentDescription = null,
            modifier = Modifier
                .padding(15.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(50))
        )
    }

    @Composable
    fun Rank(index: Int?) {
        Box(
            Modifier
                .padding(6.dp)
                .size(60.dp)
        ) {
            Image(
                painter = rememberImagePainter(Utils.getRankUrl(index)),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Image(
                painter = rememberImagePainter(Utils.getStarsUrl(index)),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }

    @Composable
    fun TextLabelRounded(
        modifier: Modifier = Modifier,
        text: String,
        borderWidth: Dp = 2.dp,
        fontWeight: FontWeight = FontWeight.SemiBold,
        textColor: Color = BackgroundDark,
        backgroundColor: Color = Transparent,
        fontFamily: FontFamily = poppinsFamily
    ) {
        Text(
            text = text,
            modifier = modifier
                .padding(8.dp)
//                .padding(bottom = 7.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(40.dp)
                )
                .border(
                    width = borderWidth,
                    color = textColor,
                    shape = RoundedCornerShape(40.dp)
                )
                .padding(8.dp),
            color = textColor,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            fontWeight = fontWeight,
            fontSize = 16.sp,
            maxLines = 1
        )
    }
}
