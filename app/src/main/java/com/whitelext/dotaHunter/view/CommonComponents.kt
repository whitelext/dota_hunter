package com.whitelext.dotaHunter.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.whitelext.dotaHunter.domain.ItemStore
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.EmptyItemSlot
import com.whitelext.dotaHunter.ui.theme.Transparent
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Utils

object CommonComponents {

    @Composable
    fun ProfilePhoto(id: String?) {
        val painter = rememberImagePainter(
            data = Utils.getAvatarUrl(id),
            builder = {
                crossfade(500)
            }
        )
        Image(
            painter = painter,
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
            val painterRank = rememberImagePainter(
                data = Utils.getRankUrl(index),
                builder = {
                    crossfade(500)
                }
            )
            val painterStars = rememberImagePainter(
                data = Utils.getStarsUrl(index),
                builder = {
                    crossfade(500)
                }
            )
            Image(
                painter = painterRank,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Image(
                painter = painterStars,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }

    @Composable
    fun ItemsGrid(items: List<Short>) {

        Column {
            Row {
                for (i in 0..2) {
                    val itemName = ItemStore.getItemById(items[i].toInt())
                    if (items[i] < 0 || itemName == null) EmptySpace(EmptyItemSlot) else ItemIcon(
                        itemUrl = Utils.getItemUrl(itemName),
                        itemName = itemName,
                        colorFilter = null
                    )
                }
            }
            Row {
                for (i in 3..5) {
                    val itemName = ItemStore.getItemById(items[i].toInt())
                    if (items[i] < 0 || itemName == null) EmptySpace(EmptyItemSlot) else ItemIcon(
                        itemUrl = Utils.getItemUrl(itemName),
                        itemName = itemName,
                        colorFilter = null
                    )
                }
            }
        }
    }

    @Composable
    fun ItemIcon(
        itemUrl: String,
        itemName: String,
        colorFilter: ColorFilter?
    ) {
        val painter = rememberImagePainter(
            data = itemUrl,
            builder = {
                crossfade(500)
            }
        )
        Image(
            painter = painter,
            contentDescription = itemName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 3.dp, vertical = 2.dp)
                .width(55.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(10.dp)),
            colorFilter = colorFilter
        )
    }

    @Composable
    fun EmptySpace(color: Color) {
        Spacer(
            modifier = Modifier
                .padding(horizontal = 3.dp, vertical = 2.dp)
                .width(55.dp)
                .height(45.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = BackgroundDark,
                    shape = RoundedCornerShape(10.dp)
                )
        )
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

    @Composable
    fun TextLabelWithPictureRounded(
        modifier: Modifier = Modifier,
        picture: String,
        text: String,
        borderWidth: Dp = 2.dp,
        fontWeight: FontWeight = FontWeight.SemiBold,
        textColor: Color = BackgroundDark,
        backgroundColor: Color = Transparent,
        fontFamily: FontFamily = poppinsFamily
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 8.dp)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberImagePainter(
                data = picture,
                builder = {
                    crossfade(500)
                }
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(27.dp)

            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = text,
                color = textColor,
                fontFamily = fontFamily,
                textAlign = TextAlign.End,
                fontWeight = fontWeight,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }

    @Composable
    fun TextLabelWithTwoRows(
        modifier: Modifier = Modifier,
        label: String,
        text: String,
        borderWidth: Dp = 2.dp,
        fontWeight: FontWeight = FontWeight.SemiBold,
        textColor: Color = BackgroundDark,
        backgroundColor: Color = Transparent,
        fontFamily: FontFamily = poppinsFamily
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 8.dp)
//                .padding(bottom = 7.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .border(
                    width = borderWidth,
                    color = textColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = textColor,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center,
                fontWeight = fontWeight,
                fontSize = 16.sp,
                maxLines = 1
            )
            Text(
                text = text,
                color = textColor,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center,
                fontWeight = fontWeight,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }
}
