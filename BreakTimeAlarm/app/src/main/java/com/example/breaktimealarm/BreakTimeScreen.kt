package com.example.breaktimealarm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breaktimealarm.ui.theme.BreakTimeAlarmTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BreakTimeScreen(
    viewModel: BreakTimeViewModel
){
    val breakTime by viewModel.breakTime.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.break_time_select_title),
            style = MaterialTheme.typography.h4
        )
        
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = breakTime,
                onValueChange = { viewModel.updateBreakTime(it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                textStyle = MaterialTheme.typography.h4,
                singleLine = true,
            )
            Text(
                text = stringResource(id = R.string.minute),
                style = MaterialTheme.typography.h4
            )
        }
        
        Button(
            onClick = { viewModel.createBreakTimeNotification(breakTime) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.break_time),
                style = MaterialTheme.typography.h4
            )
        }

        Text(
            text = stringResource(id = R.string.current_break_time, breakTime)
        )
    }
}

@Preview
@Composable
fun BreakTimeScreenPreview(){
    BreakTimeAlarmTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            BreakTimeScreen(
                viewModel = BreakTimeViewModel(MyApplication())
            )
        }
    }
}