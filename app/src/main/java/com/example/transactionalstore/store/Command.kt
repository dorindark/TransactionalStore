package com.example.transactionalstore.store

import com.example.transactionalstore.store.internal.CommandType

sealed interface Command

sealed class TypedCommand(val type: CommandType) : Command {
    override fun toString() = when (type) {
        CommandType.SET -> "SET"
        CommandType.GET -> "GET"
        CommandType.DELETE -> "DELETE"
        CommandType.COUNT -> "COUNT"
        CommandType.BEGIN -> "BEGIN"
        CommandType.COMMIT -> "COMMIT"
        CommandType.ROLLBACK -> "ROLLBACK"
        CommandType.INVALID -> "INVALID"
    }
}

data class SetCommand(val key: String, val value: String) : TypedCommand(CommandType.SET) {
    override fun toString() = "${super.toString()} $key $value"
}

data class GetCommand(val key: String) : TypedCommand(CommandType.GET) {
    override fun toString() = "${super.toString()} $key"
}

data class DeleteCommand(val key: String) : TypedCommand(CommandType.DELETE) {
    override fun toString() = "${super.toString()} $key"
}

data class CountCommand(val value: String) : TypedCommand(CommandType.COUNT) {
    override fun toString() = "${super.toString()} $value"
}

class BeginTransactionCommand : TypedCommand(CommandType.BEGIN)
class CommitTransactionCommand : TypedCommand(CommandType.COMMIT)
class RollbackTransactionCommand : TypedCommand(CommandType.ROLLBACK)
