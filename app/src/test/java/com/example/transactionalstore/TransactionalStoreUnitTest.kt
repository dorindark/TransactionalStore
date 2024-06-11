package com.example.transactionalstore

import app.cash.turbine.test
import com.example.transactionalstore.store.BeginTransactionCommand
import com.example.transactionalstore.store.CountCommand
import com.example.transactionalstore.store.DeleteCommand
import com.example.transactionalstore.store.GetCommand
import com.example.transactionalstore.store.RollbackTransactionCommand
import com.example.transactionalstore.store.SetCommand
import com.example.transactionalstore.store.StoreProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Some unit tests for the TransactionalStore
 */
class TransactionalStoreUnitTest {

    private val store = StoreProvider.getStore()

    @Test
    fun testNestedTransactions() = runBlocking {
        val outputFlow = store.getOutputs().map { it.description }

        outputFlow.test {
            store.sendCommand(SetCommand(key = "foo", value = "123"))
            assertEquals("", awaitItem())

            store.sendCommand(SetCommand(key = "bar", value = "456"))
            assertEquals("", awaitItem())

            store.sendCommand(BeginTransactionCommand())
            assertEquals("", awaitItem())

            store.sendCommand(SetCommand(key = "foo", value = "456"))
            assertEquals("", awaitItem())

            store.sendCommand(BeginTransactionCommand())
            assertEquals("", awaitItem())

            store.sendCommand(CountCommand(value = "456"))
            assertEquals("2", awaitItem())

            store.sendCommand(GetCommand(key = "foo"))
            assertEquals("456", awaitItem())

            store.sendCommand(SetCommand(key = "foo", value = "789"))
            assertEquals("", awaitItem())

            store.sendCommand(GetCommand(key = "foo"))
            assertEquals("789", awaitItem())

            store.sendCommand(RollbackTransactionCommand())
            assertEquals("", awaitItem())

            store.sendCommand(GetCommand(key = "foo"))
            assertEquals("456", awaitItem())

            store.sendCommand(DeleteCommand(key = "foo"))
            assertEquals("", awaitItem())

            store.sendCommand(GetCommand(key = "foo"))
            assertEquals("key not set", awaitItem())

            store.sendCommand(RollbackTransactionCommand())
            assertEquals("", awaitItem())

            store.sendCommand(GetCommand(key = "foo"))
            assertEquals("123", awaitItem())
        }
    }
}