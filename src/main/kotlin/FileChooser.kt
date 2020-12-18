import androidx.compose.desktop.AppWindowAmbient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import java.io.File


class FileChooser(val startingFolder: String) {
    var currentFolder = mutableStateOf(File(startingFolder))
    var addressBarField = mutableStateOf(startingFolder)
    var fileNameField = mutableStateOf("")

    var returnFile: File? = null


    @Composable
    fun openFileChooser(ds: MutableState<Boolean>, newStart: String = currentFolder.value.absolutePath, rv: MutableState<String>) {
        if(!File(newStart).exists()) return
        val fileSet = remember {mutableStateOf(false)}
        currentFolder = mutableStateOf(File(startingFolder))
        addressBarField = mutableStateOf(startingFolder)
        fileNameField = mutableStateOf("")
        currentFolder.value = File(newStart)
        addressBarField.value = newStart
        Dialog({
            ds.value = false
            rv.value = returnFile?.absolutePath ?: ""
        }) {
            returnFile = null
            MaterialTheme {
                Column {
                    //Row will have (back, forward (maybe)) up one folder bottons, program path bar, button for program path bar, maybe search
                    Row(modifier = Modifier.weight(.15f)) {
                        Button({
                            addressBarField.value =
                                currentFolder.value.parentFile?.absolutePath ?: addressBarField.value
                            currentFolder.value = currentFolder.value.parentFile ?: currentFolder.value
                        }) { Text("UP") }
                        TextField(addressBarField.value, { addressBarField.value = it })
                        Button({}) { Text("Refresh") }
                    }
                    //content
                    LazyColumn(modifier = Modifier.weight(.7f)) {
                        items(
                            currentFolder.value.walkTopDown().maxDepth(1).toList()
                        ) { if (it != currentFolder.value) fileEntry(it) }
                        //actual folders and files are shown here with scrolling

                    }
                    //File Name box and select button
                    Column(modifier = Modifier.weight(.15f)) {
                        Row {
                            Text("File Name:")
                            TextField(fileNameField.value, { fileNameField.value = it })
                            Button({
                                //check if fileNameField is not empty, and that the corresponding File/Directory exists
                                if (fileNameField.value.isNotBlank() && File(currentFolder.value.absolutePath + "\\"+ fileNameField.value).exists()) {
                                    returnFile = File(currentFolder.value.absolutePath + "\\" + fileNameField.value)
                                    fileSet.value = true
                                }
                            }) { Text("Select") }
                        }
                        //file type filter (maybe?) and cancel
                    }
                    if (fileSet.value) AppWindowAmbient.current?.close()
                }
            }
        }
      //  }
    }
    @Composable
    fun fileEntry(file: File){
        //has an image
        //a button/text entry
        //theoretically stuff about the file (but I am probably going to be lazy)
        Row{
            if(file.isDirectory)Text("D  ") else Text("F  ")
            Text(file.name, modifier = Modifier.clickable(onClick = {fileNameField.value = file.name}, onDoubleClick = {
                if(file.isDirectory){
                    currentFolder.value = file
                    addressBarField.value = file.absolutePath
                }
            }))
        }

    }
}
