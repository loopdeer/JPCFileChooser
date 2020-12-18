import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Dialog
import java.io.File
import kotlin.system.exitProcess

fun main() =  Window(title = "File Chooser Demo", size = IntSize(800,600)) {
    var text = remember { mutableStateOf("Hello, World!") }
    val dialogState = remember { mutableStateOf(false) }
    val fc by remember { mutableStateOf(FileChooser("C:\\")) }
    Column {
        Button({ dialogState.value = true }) {
            Text(text.value)
        }
        if (dialogState.value) {
            fc.openFileChooser(dialogState, "C:\\Anime", text)
        }

    }
}