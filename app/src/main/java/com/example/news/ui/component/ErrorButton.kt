package com.example.news.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ErrorButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            buttonText,
        ) = createRefs()
        TextButton(
            modifier = Modifier
                .constrainAs(buttonText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            border = BorderStroke(
                1.dp,
                Color.Blue,
            ),
            onClick = onClick,
        ) {
            Text(text = text)
        }
    }
}