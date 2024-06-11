package com.example.transactionalstore.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.transactionalstore.R
import com.example.transactionalstore.store.StoreProvider
import com.example.transactionalstore.ui.components.CountCommandBox
import com.example.transactionalstore.ui.components.DeleteCommandBox
import com.example.transactionalstore.ui.components.GetCommandBox
import com.example.transactionalstore.ui.components.SetCommandBox
import com.example.transactionalstore.ui.components.TitleText
import com.example.transactionalstore.ui.components.TransactionCommands
import com.example.transactionalstore.ui.theme.TransactionalStoreTheme
import com.example.transactionalstore.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val store = StoreProvider.getStore() // better injected
        val mainViewModel: MainViewModel by viewModels()

        setContent {
            TransactionalStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TitleText()

                        SetCommandBox(store)
                        GetCommandBox(store)
                        CountCommandBox(store)
                        DeleteCommandBox(store)

                        TransactionCommands(store)

                        LogsTextField(mainViewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun LogsTextField(mainViewModel: MainViewModel) {
        val text =
            mainViewModel.logsText.collectAsStateWithLifecycle("", lifecycleOwner = this).value
        val scrollState = rememberScrollState(0)

        TextField(
            value = text,
            onValueChange = { /*value = it*/ },
            label = { Text(stringResource(R.string.logs_text)) },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        )

        LaunchedEffect(text) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
}
