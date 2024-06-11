package com.example.transactionalstore.store.internal

/**
 * Class to keep the old values for the changed keys before a transaction begins
 */
class TransactionChanges {
    // null value represents an added key that didn't exist before the transactions
    private val changes = hashMapOf<String, String?>()

    fun insertChange(key: String, value: String?) {
        if (!changes.containsKey(key)) {
            changes[key] = value
        }
    }

    fun getChanges() = changes.toMap()
}
