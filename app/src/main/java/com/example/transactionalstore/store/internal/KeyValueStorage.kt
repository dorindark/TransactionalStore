package com.example.transactionalstore.store.internal

/**
 * Main repository storage for the key value String pairs
 */
class KeyValueStorage {
    private val map = hashMapOf<String, String>()
    private val counts = hashMapOf<String, Int>()

    fun get(key: String) = map[key]

    fun set(key: String, value: String) {
        // keep the counter accurate for the set values
        // decrement the counter for the old value count if it exists
        map[key]?.let { oldValue ->
            counts[oldValue]?.let { count ->
                if (count > 1) {
                    counts[oldValue] = count - 1
                } else {
                    counts.remove(oldValue)
                }
            }
        }

        // put or update the value for the key in the internal data structure
        map[key] = value

        // increment the counter for the new value
        counts[value] = (counts[value] ?: 0) + 1
    }

    fun delete(key: String) {
        val value = map.remove(key)

        // update counter for the removed value
        value?.let { v ->
            counts[v]?.let { count ->
                if (count > 1) {
                    counts[v] = count - 1
                } else {
                    counts.remove(v)
                }
            }
        }
    }

    fun count(value: String) = counts[value] ?: 0
}
