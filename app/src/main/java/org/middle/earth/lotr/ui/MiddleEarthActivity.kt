package org.middle.earth.lotr.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.middle.earth.lotr.surveillance.NetworkState
import org.middle.earth.lotr.surveillance.NetworkSurveillance
import org.middle.earth.lotr.ui.theme.TheLordOfTheRingsTheme
import javax.inject.Inject

@AndroidEntryPoint
class MiddleEarthActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var networkSurveillance: dagger.Lazy<NetworkSurveillance>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        setContent {


            val connectionState by networkSurveillance.get().connectionFlow.collectAsState(initial = NetworkState.Connected)

            val isNetworkConnected by remember {
                derivedStateOf {
                    when (connectionState) {
                        is NetworkState.Connected -> true
                        is NetworkState.Disconnected -> false
                    }
                }
            }
            TheLordOfTheRingsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheLordOfTheRingsTheme {
        Greeting("Android")
    }
}