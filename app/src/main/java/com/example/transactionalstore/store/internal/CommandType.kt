package com.example.transactionalstore.store.internal

enum class CommandType {
    SET,
    GET,
    DELETE,
    COUNT,
    BEGIN,
    COMMIT,
    ROLLBACK,
    INVALID,
}
