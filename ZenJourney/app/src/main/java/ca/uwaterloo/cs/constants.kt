package ca.uwaterloo.cs

enum class PageStates {
    WELCOME, LOGIN, SIGNUP_STEP1, SIGNUP_STEP2, SIGNUP_STEP3,
    SIGNUP_CLOUD, SIGNUP_CLOUD_MORE, SIGNUP_AFFIRMATION, SIGNUP_PIN,
    HOME,
    AFFIRMATION,
    JOURNAL_STEP1, JOURNAL_STEP2, JOURNAL_STEP3, PAST_JOURNAL,
    MEDITATE, MEDITATE_PICK_TUNE,
    PHOTOBOOK_MONTH, PHOTOBOOK_ALL,
    SETTINGS, DISCLAIMER,
    LOADING
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

/* Taken from:
* https://www.wondermind.com/article/daily-affirmations/,
* https://www.mentalhelp.net/blogs/120-daily-positive-mental-health-affirmations/
*  */
val customAffirmations = listOf(
    "Sad" to listOf(
        "I am allowed to feel sad.",
        "Being open with others about feeling sad makes me stronger.",
        "I am doing my best to manage my sadness.",
        "My sadness does not define me; it encourages me to take care of myself.",
    ),
    "Angry" to listOf(
        "There is nothing wrong with feeling angry.",
        "I am open to what my anger is trying to tell me about myself.",
        "My anger does not always have to result in a strong reaction.",
        "I inhale in peace and exhale anger.",
        "How I choose to express my anger is my responsibility.",
    ),
    "Happy" to listOf(
        "I deserve to live a happy and healthy life.",
        "I no longer wait for something bad to ruin my happiness.",
        "I am allowed to prioritize my happiness.",
        "I create my own version of happiness every day.",
        "I choose to let go of the things that no longer make me happy.",
    ),
    "Anxious" to listOf(
        "I always make it through these tough moments.",
        "I will not allow what is stressing me out to ruin my entire day.",
        "I am choosing to focus on things that bring me peace.",
        "I give myself permission to release my anxious thoughts.",
        "I trust that I will be able to move past all of this anxiety.",
    ),
    "Relieved" to listOf(
        "I am worthy of love and acceptance, just as I am.",
        "I trust in my ability to handle any situation that comes my way.",
        "I release the need to impress others and instead focus on being authentic and true to myself.",
        "I choose to focus on the present moment and enjoy the company of those around me.",
    ),
    "Loved" to listOf(
        "I deserve to feel happy and full of life.",
        "I am deserving of contentment, joy, and peace.",
        "I am kind to others and inspire others to be kind and that feels great.",
        "Love is a birthright for each and every one of us, me included.",
        "I am worthy of love just for being who I am.",
    ),
    "Shocked" to listOf(
        "I will keep breathing, no matter what.",
        "Changes aren’t always bad things — when the seasons change from winter to spring, flowers bloom, and birds sing.",
        "It’s a gift and a superpower to be able to adapt to my surroundings — adapting will allow me to persevere.",
        "I know it’s ok to shift my course."
    ),
    "Disappointed" to listOf(
        "I am in control of my thoughts, feelings, and actions, and I use this power to achieve my goals and fulfill my dreams.",
        "I know that failure is not a sign of weakness, but rather an opportunity to learn and grow.",
        "I am committed to creating a life that brings me joy and satisfaction.",
        "I am grateful for all that I have and look forward to what the future holds.",
        "I am confident in my abilities and believe in myself.",
    )
).toMap()

/* Taken from https://www.betterup.com/blog/positive-affirmations */
val randAffirmations = listOf(
    "I am enough. I have enough.",
    "I am in the right place, at the right time, doing the right thing.",
    "I can do hard things.",
    "I allow myself to be more fully me.",
    "I believe in myself.",
    "I am grateful for another day of life.",
    "I am worthy of what I desire.",
    "I choose myself.",
    "I am resilient in the face of challenges.",
    "I am proud of myself and my achievements.",
    "I will accomplish everything I need to do today.",
    "I do my best, and my best is good enough.",
    "I prioritize my well-being.",
    "I overcome my fears by getting out of my comfort zone.",
    "I am love, and I am loved.",
    "Money comes frequently and easily to me.",
    "I trust my inner guidance and follow it.",
    "I accept my emotions and let them move through me.",
    "I take care of myself, mind, body, and spirit.",
    "I trust myself to make the right decisions.",
    "I give myself permission to take up space.",
    "I use my voice to speak up for myself and others.",
    "I trust that I’m heading in the right direction.",
    "I allow myself to make mistakes as they help me grow.",
    "I accept myself exactly as I am without judgment.",
    "I have everything I need to achieve my goals.",
    "I am constantly generating brilliant ideas.",
    "I am safe and supported.",
    "I love and accept myself.",
    "I am kind to myself and others."
)
