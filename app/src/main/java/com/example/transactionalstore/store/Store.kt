package com.example.transactionalstore.store

import kotlinx.coroutines.flow.Flow

interface Store {

    /**
     * Gives a command to the Store for processing
     */
    fun sendCommand(command: Command)

    /**
     * Flow of operation results for the given commands
     */
    fun getOutputs(): Flow<OperationResult>
}
