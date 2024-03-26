package ca.uwaterloo.cs

enum class PageStates {
    WELCOME, LOGIN, SIGNUP_STEP1, SIGNUP_STEP2, SIGNUP_STEP3,
    SIGNUP_CLOUD, SIGNUP_CLOUD_MORE, SIGNUP_AFFIRMATION, SIGNUP_PIN,
    HOME,
    AFFIRMATION,
    JOURNAL_STEP1, JOURNAL_STEP2, JOURNAL_STEP3, PAST_JOURNAL,
    MEDITATE, MEDITATE_PICK_TUNE,
    PHOTOBOOK,
    SETTINGS, DISCLAIMER
}

enum class InputErrorStates {
    NONE,
    EMPTY_INPUT,
    INVALID_EMAIL,
    INVALID_PASSWORD
}

enum class PINErrorStates {
    NONE,
    INVALID_PIN_FORMAT,
    INCORRECT_PIN
}

val moodEmojisWithLabels = listOf(
    "\uD83D\uDE0A" to "Happy",
    "\uD83D\uDE22" to "Sad",
    "\uD83D\uDE20" to "Angry",
    "\uD83D\uDE31" to "Shocked",
    "\uD83D\uDE1E" to "Disappointed",
    "\uD83D\uDE0D" to "Loved",
    "\uD83E\uDD2F" to "Mind Blown",
    "\uD83D\uDE0E" to "Cool",
    "\uD83E\uDD73" to "Party"
)

// same as above but map
val emojiToWordMap = moodEmojisWithLabels.toMap()

// map of ("Happy" to "\uD83D\uDE0A")
val wordToEmojiMap = moodEmojisWithLabels.map { (emoji, word) -> word to emoji }.toMap()

// songs
val meditationMusic = listOf(
    Tune("Once in Paris", R.raw.once_in_paris),
    Tune("Good Night", R.raw.good_night),
    Tune("Forest Lullaby", R.raw.forest_lullaby),
    Tune("Lofi Study", R.raw.lofi_study),
    Tune("Melody of Nature", R.raw.melody_of_nature),
    Tune("Scott Buckley Moonlight", R.raw.scott_buckley_moonlight),
    Tune("Test Song", R.raw.test_song)
)

val customizationTitles = arrayOf("Notifications", "Personalized Affirmations", "PIN")