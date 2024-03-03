package ca.uwaterloo.cs

enum class PageStates {
    WELCOME, LOGIN, SIGNUP_STEP1, SIGNUP_STEP2,
    HOME,
    AFFIRMATION,
    JOURNAL_STEP1, JOURNAL_STEP2, JOURNAL_STEP3, PAST_JOURNAL,
    MEDITATE,
    PHOTOBOOK,
    SETTINGS
}

enum class InputErrorStates {
    NONE,
    EMPTY_INPUT,
    INVALID_EMAIL,
    INVALID_PASSWORD
}