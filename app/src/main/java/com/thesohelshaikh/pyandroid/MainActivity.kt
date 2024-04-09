package com.thesohelshaikh.pyandroid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.thesohelshaikh.pyandroid.ui.theme.PyandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize python module
        val py = Python.getInstance()
        val pyModule = py.getModule("Main")

        val bitmap = generateBitmap(pyModule)
        Log.d(TAG, "random number: ${pyModule["generate_random"]?.call()}")
        Log.d(TAG, "onCreate: bitmap: $bitmap")
        val shape = pyModule["generate_shape"]?.call()
        val area = pyModule.callAttr("calculate_area", shape)
        Log.d(TAG, "onCreate: shapely:$shape, $area")
        val geopyDistance = pyModule["distance"]?.call()
        Log.d(TAG, "onCreate: geopy: $geopyDistance")

        setContent {
            PyandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        SectionHeader("Basic Function")
                        Text(pyModule["hello"]?.call().toString())
                        SectionSpacer()

                        SectionHeader("numpy")
                        Text(text = "Random number: ${pyModule["generate_random"]?.call()}")
                        SectionSpacer()

                        SectionHeader(heading = "matplotlib")
                        if (bitmap != null) {
                            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
                        }
                        SectionSpacer()

                        SectionHeader(heading = "shapely")
                        Text(text = "Area of ${shape?.type()} is $area")
                        SectionSpacer()

                        SectionHeader(heading = "geopy")
                        Text(text = "Distance from Newport to Cleveland is $geopyDistance")
                        SectionSpacer()
                    }
                }
            }
        }
    }

    @Composable
    private fun SectionSpacer() {
        Spacer(modifier = Modifier.size(16.dp))
    }

    @Composable
    private fun SectionHeader(heading: String) {
        Text(text = heading, style = MaterialTheme.typography.headlineSmall)
        Divider(thickness = 2.dp)
    }

    private fun generateBitmap(pyModule: PyObject): Bitmap? {
        val bytes = pyModule.callAttr("plot")
            .toJava(ByteArray::class.java)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    companion object {
        private const val TAG = "PyMainActivity"
    }
}