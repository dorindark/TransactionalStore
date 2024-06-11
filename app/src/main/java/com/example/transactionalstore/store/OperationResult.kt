package com.example.transactionalstore.store

import com.example.transactionalstore.store.internal.ResultType

data class OperationResult(
    val command: Command,
    var result: ResultType,
    var description: String = ""
) {
    override fun toString() =
        if (description.isNotEmpty()) {
            "> $command\n$description\n"
        } else {
            "> $command\n"
        }
}