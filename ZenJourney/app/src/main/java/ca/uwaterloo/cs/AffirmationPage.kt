package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun AffirmationPage(pageState: MutableState<PageStates>) {
    val affirmations = listOf(
        "You got this",
        "You'll figure it out",
        "You're a smart cookie",
        "I believe in you",
        "Sucking at something is the first step towards being good at something",
        "Struggling is part of learning",
        "Everything has cracks - that's how the light gets in",
        "Mistakes don't make you less capable",
        "We are all works in progress",
        "You are a capable human",
        "You know more than you think",
        "10x engineers are a myth",
        "If everything was easy you'd be bored",
        "I admire you for taking this on",
        "You're resourceful and clever",
        "You'll find a way",
        "I know you'll sort it out",
        "Struggling means you're learning",
        "You're doing a great job",
        "It'll feel magical when it's working",
        "I'm rooting for you",
        "Your mind is full of brilliant ideas",
        "You make a difference in the world by simply existing in it",
        "You are learning valuable lessons from yourself every day",
        "You are worthy and deserving of respect",
        "You know more than you knew yesterday",
        "You're an inspiration",
        "Your life is already a miracle of chance waiting for you to shape its destiny",
        "Your life is about to be incredible",
        "Nothing is impossible. The word itself says 'I’m possible!'",
        "Failure is just another way to learn how to do something right",
        "I give myself permission to do what is right for me",
        "You can do it",
        "It is not a sprint, it is a marathon. One step at a time",
        "Success is the progressive realization of a worthy goal",
        "People with goals succeed because they know where they’re going",
        "All you need is the plan, the roadmap, and the courage to press on to your destination",
        "The opposite of courage in our society is not cowardice... it is conformity",
        "Whenever we’re afraid, it’s because we don’t know enough. If we understood enough, we would never be afraid",
        "The past does not equal the future",
        "The path to success is to take massive, determined action",
        "It’s what you practice in private that you will be rewarded for in public",
        "Small progress is still progress",
        "Don't worry if you find flaws in your past creations, it's because you've evolved",
        "Starting is the most difficult step - but you can do it",
        "Don't forget to enjoy the journey",
        "It's not a mistake, it's a learning opportunity"
    )

    val (currAffirmation, setCurrAffirmation) = remember { mutableStateOf(affirmations.random()) }


    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 550.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = currAffirmation,
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF4F4F4F),
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp, 80.dp)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = { setCurrAffirmation(affirmations.random()) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Next",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}

