package brawijaya.example.sportsync.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReadOnlyTextField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(25.dp),
    maxLines: Int = 1
) {
    Column(
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        OutlinedTextField(
            value = value,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = shape,
            textStyle = TextStyle(fontSize = 14.sp),
            readOnly = true,
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            enabled = false
        )
    }
}
