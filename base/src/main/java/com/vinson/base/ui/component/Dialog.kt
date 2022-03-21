package com.vinson.base.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vinson.base.ui.theme.Body
import com.vinson.base.ui.theme.BoldTitle

@Composable
fun rememberDialogState(initOpen: Boolean = false) = remember { DialogState(initOpen) }

@Stable
class DialogState(
        initOpen: Boolean = false
) {
    var isOpen by mutableStateOf(initOpen)
}

@Composable
fun Dialog(
    title: String,
    description: String,
    positiveText: String? = null,
    negativeText: String? = null,
    positiveAction: () -> Unit = {},
    negativeAction: () -> Unit = {},
    state: DialogState = rememberDialogState(),
) {
    if (state.isOpen) {
        AlertDialog(
                onDismissRequest = {
                    state.isOpen = false
                },
                title = {
                    Text(
                            text = title,
                            style = BoldTitle
                    )
                },
                text = {
                    Text(
                            text = description,
                            style = Body,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                    .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    )
                },
                buttons = {
                    Column(
                            modifier = Modifier
                                    .padding(24.dp, 32.dp, 24.dp, 24.dp),
                            verticalArrangement = Arrangement.Center
                    ) {
                        if (positiveText != null) {
                            DarkNormalButton(
                                    text = positiveText,
                                    modifier = Modifier
                                            .fillMaxWidth(),
                                    onClick = {
                                        positiveAction()
                                        state.isOpen = false
                                    },
                            )
                        }
                        if (negativeText != null) {
                            val paddingTop = if (positiveText != null) 12.dp else 0.dp
                            LightNormalButton(
                                    text = negativeText,
                                    onClick = {
                                        negativeAction()
                                        state.isOpen = false
                                    },
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, paddingTop, 0.dp, 0.dp),
                            )
                        }
                    }
                }
        )
    }
}