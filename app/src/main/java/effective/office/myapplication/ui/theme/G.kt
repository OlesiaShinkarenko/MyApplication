package effective.office.myapplication.ui.theme

import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DropDownItem(
    val text: String
)
@Composable
fun PersonItem(
    personName: String,
    dropdownItems: List<DropDownItem>,
    modifier: Modifier = Modifier,
    onItemClick: (DropDownItem) -> Unit
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current
    Card(modifier = modifier.onSizeChanged {
        itemHeight = with(density) { it.height.toDp() }
    }) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(true) {
                detectTapGestures(onLongPress = {
                    isContextMenuVisible = true
                    pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                }, onPress = {
                    val press = PressInteraction.Press(it)
                    interactionSource.emit(press)
                    tryAwaitRelease()
                    interactionSource.emit(PressInteraction.Release(press))
                })
            }
            .background(Color.Magenta)
            .padding(16.dp)) {
            Row() {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = personName, color = Color.White, fontSize = 18.sp)
            }
        }
        DropdownMenu(
            expanded = isContextMenuVisible, onDismissRequest = {
                isContextMenuVisible = false
            }, offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )
        ) {
            dropdownItems.forEach {
                DropdownMenuItem(onClick = {
                    onItemClick(it)
                    isContextMenuVisible = false
                },

                    text = {
                        Text(text = it.text)
                    }
                )
            }
        }
    }
}
@Composable
fun ComposeContextDropDownExampleUI() {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            listOf(
                "Surya",
                "Morris",
                "Shaw",
                "Gill",
                "Jane",
                "Alex",
                "Jake",
                "Surya",
                "Surya",
            )
        ) {
            PersonItem(
                personName = it, dropdownItems = listOf(
                    DropDownItem("Edit"),
                    DropDownItem("Delete"),
                    DropDownItem("Share"),
                ), onItemClick = {
                    Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                }, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}