package com.example.notesstorage.model

/**
 * Pure validation of note input. Title and content are trimmed before the check,
 * so whitespace-only values are rejected.
 */
object NoteValidator {

    data class Result(
        val isTitleEmpty: Boolean,
        val isContentEmpty: Boolean
    ) {
        val isValid: Boolean get() = !isTitleEmpty && !isContentEmpty
    }

    fun validate(rawTitle: String, rawContent: String): Result = Result(
        isTitleEmpty = rawTitle.trim().isEmpty(),
        isContentEmpty = rawContent.trim().isEmpty()
    )
}
