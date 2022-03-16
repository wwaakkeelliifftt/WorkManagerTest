package com.example.workmanagerloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import coil.compose.rememberImagePainter
import com.example.workmanagerloader.ui.theme.SecondScreen
import com.example.workmanagerloader.ui.theme.WorkManagerLoaderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWorker>()
            .build()
        val workManager = WorkManager.getInstance(applicationContext)

        setContent {
            WorkManagerLoaderTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MAIN_SCREEN
                ) {
                    composable(MAIN_SCREEN) {
                        MainScreen(
                            navController = navController,
                            workManager = workManager,
                            downloadRequest = downloadRequest,
                            colorFilterRequest = colorFilterRequest
                        )
                    }
                    composable(SECOND_SCREEN) {
                        SecondScreen(navController)
                    }

                }


            }
        }
    }

}

@Composable
fun MainScreen(
    navController: NavController,
    workManager: WorkManager,
    downloadRequest: OneTimeWorkRequest,
    colorFilterRequest: OneTimeWorkRequest
) {
    val workInfo = workManager
        .getWorkInfosForUniqueWorkLiveData("download")
        .observeAsState()
        .value
    val downloadInfo = remember(key1 = workInfo) {
        workInfo?.find { it.id == downloadRequest.id }
    }
    val filterInfo = remember(key1 = workInfo) {
        workInfo?.find { it.id == colorFilterRequest.id }
    }
    val imageUri by derivedStateOf {
        val downloadUri = downloadInfo
            ?.outputData
            ?.getString(WorkerKeys.IMAGE_URI)
            ?.toUri()
        val colorFilterUri = filterInfo
            ?.outputData
            ?.getString(WorkerKeys.FILTER_URI)
            ?.toUri()
        colorFilterUri ?: downloadUri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(3f)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                imageUri?.let { uri ->
                    Image(
                        painter = rememberImagePainter(data = uri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        workManager
                            .beginUniqueWork(
                                "download",
                                ExistingWorkPolicy.KEEP,
                                downloadRequest
                            )
                            .then(colorFilterRequest)
                            .enqueue()
                    },
                    enabled = downloadInfo?.state != WorkInfo.State.RUNNING
                ) {
                    Text(text = "Start download")
                }
                Spacer(modifier = Modifier.height(8.dp))
                when (downloadInfo?.state) {
                    WorkInfo.State.RUNNING -> Text(text = "Downloading...")
                    WorkInfo.State.SUCCEEDED -> Text(text = "Download succeeded")
                    WorkInfo.State.FAILED -> Text(text = "Download failed")
                    WorkInfo.State.BLOCKED -> Text(text = "Download BLOCKED")
                    WorkInfo.State.CANCELLED -> Text(text = "Download cancelled")
                    WorkInfo.State.ENQUEUED -> Text(text = "Download ENQUEUED")
                }
                Spacer(modifier = Modifier.height(8.dp))
                when (filterInfo?.state) {
                    WorkInfo.State.RUNNING -> Text(text = "Applying color filter...")
                    WorkInfo.State.SUCCEEDED -> Text(text = "Color filter SUCCEEDED")
                    WorkInfo.State.FAILED -> Text(text = "Color filter failed")
                    WorkInfo.State.BLOCKED -> Text(text = "Color filter BLOCKED")
                    WorkInfo.State.CANCELLED -> Text(text = "Color filter cancelled")
                    WorkInfo.State.ENQUEUED -> Text(text = "Color filter ENQUEUED")
                }
                Spacer(Modifier.height(32.dp))
                //
                Button(
                    onClick = { navController.navigate(SECOND_SCREEN) }
                ) {
                    Text(text = "Go to second screen")
                }

            }

        }

    }
}

const val MAIN_SCREEN = "mainScreen"
const val SECOND_SCREEN = "secondScreen"