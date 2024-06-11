package com.example.transactionalstore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.transactionalstore.R
import com.example.transactionalstore.store.BeginTransactionCommand
import com.example.transactionalstore.store.CommitTransactionCommand
import com.example.transactionalstore.store.CountCommand
import com.example.transactionalstore.store.DeleteCommand
import com.example.transactionalstore.store.GetCommand
import com.example.transactionalstore.store.RollbackTransactionCommand
import com.example.transactionalstore.store.SetCommand
import com.example.transactionalstore.store.Store

@Composable
fun TitleText() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.title_text),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun TransactionCommands(store: Store) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        BeginTransactionCommandBox(store)
        CommitTransactionCommandBox(store)
        RollbackTransactionCommandBox(store)
    }
}

@Composable
fun GetCommandBox(store: Store) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var keyText by remember { mutableStateOf("") }

        Button(modifier = Modifier
            .width(100.dp)
            .padding(horizontal = 2.dp), onClick = {
            store.sendCommand(GetCommand(key = keyText))
        }) {
            Text(text = stringResource(R.string.get_text), fontSize = 12.sp)
        }
        TextField(
            value = keyText,
            onValueChange = { keyText = it },
            label = { Text(text = stringResource(R.string.key_text)) },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun SetCommandBox(store: Store) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        var keyText by remember { mutableStateOf("") }
        var valueText by remember { mutableStateOf("") }

        Button(modifier = Modifier
            .width(100.dp)
            .padding(horizontal = 2.dp), onClick = {
            store.sendCommand(SetCommand(key = keyText, value = valueText))
        }) {
            Text(text = stringResource(R.string.set_text), fontSize = 12.sp)
        }
        Column {
            TextField(
                value = keyText,
                onValueChange = { keyText = it },
                label = { Text(text = stringResource(R.string.key_text)) },
                modifier = Modifier.padding(4.dp)
            )
            TextField(
                value = valueText,
                onValueChange = { valueText = it },
                label = { Text(text = stringResource(R.string.value_text)) },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun CountCommandBox(store: Store) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        var valueText by remember { mutableStateOf("") }

        Button(modifier = Modifier
            .width(100.dp)
            .padding(horizontal = 2.dp), onClick = {
            store.sendCommand(CountCommand(value = valueText))
        }) {
            Text(text = stringResource(R.string.count_text), fontSize = 12.sp)
        }
        TextField(
            value = valueText,
            onValueChange = { valueText = it },
            label = { Text(text = stringResource(R.string.value_text)) },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun DeleteCommandBox(store: Store) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var keyText by remember { mutableStateOf("") }

        Button(modifier = Modifier
            .width(100.dp)
            .padding(horizontal = 2.dp), onClick = {
            store.sendCommand(DeleteCommand(key = keyText))
        }) {
            Text(
                text = stringResource(R.string.delete_text), fontSize = 12.sp
            )
        }
        TextField(
            value = keyText,
            onValueChange = { keyText = it },
            label = { Text(text = stringResource(R.string.key_text)) },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun BeginTransactionCommandBox(store: Store) {
    Button(modifier = Modifier
        .width(120.dp)
        .padding(horizontal = 8.dp), onClick = {
        store.sendCommand(BeginTransactionCommand())
    }) {
        Text(text = stringResource(R.string.begin_text), fontSize = 12.sp)
    }
}

@Composable
fun CommitTransactionCommandBox(store: Store) {
    Button(modifier = Modifier
        .width(120.dp)
        .padding(horizontal = 8.dp), onClick = {
        store.sendCommand(CommitTransactionCommand())
    }) {
        Text(text = stringResource(R.string.commit_text), fontSize = 12.sp)
    }
}

@Composable
fun RollbackTransactionCommandBox(store: Store) {
    Button(modifier = Modifier
        .width(130.dp)
        .padding(horizontal = 8.dp), onClick = {
        store.sendCommand(RollbackTransactionCommand())
    }) {
        Text(text = stringResource(R.string.rollback_text), fontSize = 12.sp)
    }
}
