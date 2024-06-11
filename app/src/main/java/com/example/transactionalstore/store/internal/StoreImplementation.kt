package com.example.transactionalstore.store.internal

import com.example.transactionalstore.store.BeginTransactionCommand
import com.example.transactionalstore.store.Command
import com.example.transactionalstore.store.CommitTransactionCommand
import com.example.transactionalstore.store.CountCommand
import com.example.transactionalstore.store.DeleteCommand
import com.example.transactionalstore.store.GetCommand
import com.example.transactionalstore.store.OperationResult
import com.example.transactionalstore.store.RollbackTransactionCommand
import com.example.transactionalstore.store.SetCommand
import com.example.transactionalstore.store.Store
import com.example.transactionalstore.store.TypedCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Stack

class StoreImplementation : Store {

    override fun sendCommand(command: Command) {
        runBlocking {
            launch(Dispatchers.IO) {
                processCommand(command)
            }
        }
    }

    override fun getOutputs() = outputs.asSharedFlow()

    // Internal implementation
    // Each group of changes corresponds to a started (in progress) transaction
    // We use a stack to track possible nested transactions
    private val transactionStack = Stack<TransactionChanges>()

    // main storage for the key value pairs (and their counts)
    private val mainStorage = KeyValueStorage()

    private val outputs = MutableSharedFlow<OperationResult>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private suspend fun processCommand(command: Command) {
        val opRes = OperationResult(command = command, result = ResultType.ERROR)

        if (command is TypedCommand) {
            when(command) {
                is GetCommand -> {
                    val value = mainStorage.get(command.key)
                    if (value != null) {
                        opRes.result = ResultType.SUCCESS
                        opRes.description = value
                    } else {
                        opRes.result = ResultType.KEY_NOT_FOUND
                        opRes.description = "key not set"
                    }
                }
                is SetCommand -> {
                    setInStore(command.key, command.value)
                    opRes.result = ResultType.SUCCESS
                    // can be expanded with more error cases, not needed atm
                }
                is DeleteCommand -> {
                    deleteFromStore(command.key)
                    opRes.result = ResultType.SUCCESS
                    // can be expanded with more error cases, not needed atm
                }
                is CountCommand -> {
                    opRes.result = ResultType.SUCCESS
                    opRes.description = mainStorage.count(command.value).toString()
                    // can be expanded with more error cases, not needed atm
                }
                is BeginTransactionCommand -> {
                    beginTransaction()
                    opRes.result = ResultType.SUCCESS
                    // can be expanded with more error cases, not needed atm
                }
                is CommitTransactionCommand -> {
                    val result = commitTransaction()
                    if (result != null) {
                        opRes.result = ResultType.INVALID_OPERATION
                        opRes.description = result
                    } else {
                        opRes.result = ResultType.SUCCESS
                    }
                }
                is RollbackTransactionCommand -> {
                    val result = rollbackTransaction()
                    if (result != null) {
                        opRes.result = ResultType.INVALID_OPERATION
                        opRes.description = result
                    } else {
                        opRes.result = ResultType.SUCCESS
                    }
                }
            }
        }

        outputs.emit(opRes)
    }

    // Transactional and main storage ops
    // returns null for success, error message otherwise
    private fun rollbackTransaction(): String? {
        if (transactionStack.isEmpty()) {
            return "no transaction to rollback"
        }

        // rollback logic, revert the changes to previous values
        val changes = transactionStack.pop().getChanges()
        for (change in changes) {
            val key = change.key
            val oldValue = change.value
            if (oldValue != null) {
                mainStorage.set(key, oldValue)
            } else {
                mainStorage.delete(key)
            }
        }

        return null
    }

    // returns null for success, error message otherwise
    private fun commitTransaction(): String? {
        if (transactionStack.isEmpty()) {
            return "no transaction to commit"
        }

        transactionStack.pop()
        return null
    }

    private fun beginTransaction(): String? {
        transactionStack.push(TransactionChanges())
        return null
    }

    private fun deleteFromStore(key: String): String? {
        trackTransaction(key, mainStorage.get(key))
        mainStorage.delete(key)
        return null
    }

    private fun setInStore(key: String, value: String): String? {
        trackTransaction(key, mainStorage.get(key))
        mainStorage.set(key, value)
        return null
    }

    private fun trackTransaction(key: String, oldValue: String?) {
        if (transactionStack.isNotEmpty()) {
            val transactionChanges = transactionStack.peek()
            transactionChanges.insertChange(key, oldValue)
        }
    }
}
