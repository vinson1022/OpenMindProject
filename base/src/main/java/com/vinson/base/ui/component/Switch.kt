package com.vinson.base.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vinson.base.ui.theme.BoldBody

@Composable
fun rememberSwitchRowState(
        connection: SwitchRowConnection
): SwitchRowState {
    return remember {
        SwitchRowState(connection = connection)
    }
}

@Stable
class SwitchRowState(private val connection: SwitchRowConnection) {
    var checkedState by mutableStateOf(connection.getCheckState())
    var title by mutableStateOf(connection.getTitle())

    fun switch(to: Boolean) {
        checkedState = to
        connection.switchedAction(to)
        title = connection.getTitle()
    }
}

interface SwitchRowConnection {
    fun getTitle(): String
    fun getCheckState(): Boolean
    fun switchedAction(checkState: Boolean)
}

@Composable
fun SwitchRow(
    modifier: Modifier = Modifier,
    state: SwitchRowState,
) {
    Row(modifier = modifier) {
        Switch(
                checked = state.checkedState,
                onCheckedChange = { state.switch(it) }
        )
        Text(
                text = state.title,
                style = BoldBody,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp)
        )
    }
}