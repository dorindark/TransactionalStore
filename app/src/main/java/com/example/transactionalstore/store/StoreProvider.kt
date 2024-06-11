package com.example.transactionalstore.store

import com.example.transactionalstore.store.internal.StoreImplementation

object StoreProvider {

    private val storeImplementation by lazy { StoreImplementation() }

    @Synchronized
    fun getStore() = storeImplementation
}