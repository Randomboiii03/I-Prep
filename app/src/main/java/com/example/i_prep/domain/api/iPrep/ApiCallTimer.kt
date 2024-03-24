package com.example.i_prep.domain.api.iPrep

object ApiCallTimer {
    suspend fun <T> measureTime(block: suspend () -> T): Pair<T, String> {
        val startTime = System.currentTimeMillis()
        val result = block()
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        val formattedTime = formatTime(elapsedTime)

        return Pair(result, formattedTime)
    }

    private fun formatTime(timeMillis: Long): String {
        val seconds = timeMillis / 1000
        val minutes = seconds / 60

        if (minutes > 0) {
            val remainingSeconds = seconds % 60
            return "$minutes min $remainingSeconds sec"
        } else {
            return "$seconds sec"
        }
    }
}