package com.vinson.base.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.vinson.base.R
import com.vinson.base.ui.theme.*
import com.vinson.base.ui.util.isFloatValid
import com.vinson.base.ui.theme.BoldTitle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QueryEditText(
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    onValueConfirm: (String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit),
    textState: MutableState<String>
) {
    DelayConfirmEditTextField(
        type = QueryEditType,
        modifier = modifier,
        onValueChange = onValueChange,
        onValueConfirm = onValueConfirm,
        onDone = onDone,
        textState = textState,
    )
}

@Composable
fun DelayConfirmEditTextField(
    type: EditType,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    onValueConfirm: (String) -> Unit,
    onNext: (KeyboardActionScope.() -> Unit)? = null,
    onDone: (KeyboardActionScope.() -> Unit)? = null,
    textState: MutableState<String>
) {
    val isValid by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var confirmJob: Job? = null
    fun createConfirmJob(value: String) = scope.launch {
        delay(3000L)
        onValueConfirm(value)
    }

    Column(
        modifier = modifier
    ) {
        BaseEditTextField(
            hint = type.getHintId(),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, Shapes.medium),
            onValueChange = {
                confirmJob?.cancel()
                confirmJob = createConfirmJob(it).also { job ->
                    job.start()
                }
                onValueChange(it)
            },
            visualTransformation = type.getVisualType(),
            keyboardOptions = KeyboardOptions(
                keyboardType = type.getKeyboardType(),
                imeAction = if (onNext != null) ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = onNext,
                onDone = onDone
            ),
            isError = !isValid,
            textState = textState
        )
        if (!isValid) {
            Text(
                text = type.getErrorMessage(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 4.dp, 4.dp, 0.dp),
                style = Caption + TextStyle(color = Alert100)
            )
        }
    }
}

@Composable
fun BaseEditTextField(
    hint: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation,
    isError: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    textState: MutableState<String> = rememberSaveable { mutableStateOf("") },
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = textState.value,
        onValueChange = {
            textState.value = it
            onValueChange(it)
        },
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused },
        placeholder = {
            Text(
                text = hint,
                style = BoldTitle + TextStyle(color = Text40),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        trailingIcon = {
            if (textState.value.isNotEmpty() && isFocused) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filled_close),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { textState.value = "" }
                )
            }
        },
        isError = isError,
        singleLine = true,
        shape = Shapes.medium,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            focusedBorderColor = Tint100,
            unfocusedBorderColor = Color.White,
            errorBorderColor = Alert100,
            errorTrailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
            placeholderColor = Text40,
            cursorColor = Tint100,
            errorCursorColor = Tint100
        )
    )
}

val MountEditType = object : EditType {
    override fun getHintId() = "000000.00"
    override fun getErrorMessage() = "Invalid format"
    override fun getKeyboardType() = KeyboardType.Number
    override fun getVisualType() = VisualTransformation.None
    override fun isValid(value: String?) = isFloatValid(value)
}

val QueryEditType = object : EditType {
    override fun getHintId() = "Input query text"
    override fun getErrorMessage() = "Invalid format"
    override fun getKeyboardType() = KeyboardType.Text
    override fun getVisualType() = VisualTransformation.None
    override fun isValid(value: String?) = true
}

interface EditType {
    fun getHintId(): String
    fun getErrorMessage(): String
    fun getKeyboardType(): KeyboardType
    fun getVisualType(): VisualTransformation
    fun isValid(value: String?): Boolean
}