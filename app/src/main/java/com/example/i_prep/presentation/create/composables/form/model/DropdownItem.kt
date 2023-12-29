package com.example.i_prep.presentation.create.composables.form.model

sealed class DropdownItem(
    val name: String,
    val abbreviation: String
) {
    object MultipleChoice : DropdownItem(
        name = "Multiple Choice",
        abbreviation = "mcq"
    )

    object TrueOrFalse : DropdownItem(
        name = "True or False",
        abbreviation = "tof"
    )

    object ShortAnswer : DropdownItem(
        name = "Short Answer",
        abbreviation = "sa"
    )

    object FillInTheBlank : DropdownItem(
        name = "Fill-in-the-Blank",
        abbreviation = "fitb"
    )

    object Easy : DropdownItem(
        name = "Easy",
        abbreviation = "Easy"
    )

    object Intermediate : DropdownItem(
        name = "Intermediate",
        abbreviation = "Intermediate"
    )

    object Hard : DropdownItem(
        name = "Hard",
        abbreviation = "Hard"
    )

    object English : DropdownItem(
        name = "English",
        abbreviation = "English"
    )

    object Filipino : DropdownItem(
        name = "Filipino",
        abbreviation = "Filipino"
    )
}

val questionTypes = listOf(
    DropdownItem.MultipleChoice,
    DropdownItem.TrueOrFalse,
//    DropdownItem.ShortAnswer,
    DropdownItem.FillInTheBlank
)

val difficulties = listOf(
    DropdownItem.Easy,
    DropdownItem.Intermediate,
    DropdownItem.Hard
)

val languages = listOf(
    DropdownItem.English,
    DropdownItem.Filipino
)