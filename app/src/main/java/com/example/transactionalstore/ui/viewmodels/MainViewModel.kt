package com.example.transactionalstore.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.transactionalstore.store.StoreProvider
import kotlinx.coroutines.flow.map

class MainViewModel : ViewModel() {

    // TODO - inject
    private val store = StoreProvider.getStore()

    // Expose results as text logs
    val logsText = store.getOutputs().map {
        sb.append(it).toString()
    }

    private val sb = StringBuilder()
}
