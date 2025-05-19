package com.progavanzada.migrado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.progavanzada.migrado.ui.theme.MiGradoTheme
import java.util.Calendar
import java.util.GregorianCalendar




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiGradoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
//                    SaludoText(message = "Te Invito a Mi Grado ", from = "De Andres")

                }
            }
        }
    }
}
val greatVibesFont = FontFamily(
    //el archivo esta en font mas sencillo de implementar, se puede implementar con google fonts
    Font(R.font.vibes)
)


@Composable
fun SaludoText(message:String,from:String,modifier: Modifier = Modifier) {


    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(
            8.dp
        )) {
        Text(
            text = message,
            fontSize = 100.sp,
            fontFamily = greatVibesFont,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = from,
            fontSize = 36.sp,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.End)

        )
    }

}

@Composable
fun InvitationCard() {
    val context = LocalContext.current
    val imageModifier = Modifier
        .fillMaxWidth()
        .height(200.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondoinv),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.40f),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen superior con texto encima
                Box(
                    modifier = imageModifier,
                    contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.birrete),
                        contentDescription = "Invitación",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "¡Te Invito a Mi Grado!",
                        fontFamily = greatVibesFont,
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black)

                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // InfoRow 1: Dirección
                InfoRow(
                    icon = Icons.Default.Place,
                    text = "Av. Francisco de Orellana 1172 Y Amazonas",
                    onClick = {
                        val uri = Uri.parse("geo:0,0?q=Av. Francisco de Orellana 1172 Y, Quito 170150")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                )

                // Redireccion para agendar el evento
                InfoRow(
                    icon = Icons.Default.DateRange,
                    text = "Sábado 25 de Mayo, 18:00",
                    onClick = {
                        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
                            data = CalendarContract.Events.CONTENT_URI
                            putExtra(CalendarContract.Events.TITLE, "Mi Grado")
                            putExtra(CalendarContract.Events.EVENT_LOCATION, "Av. Francisco de Orellana 1172 Y Amazonas")
                            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, GregorianCalendar(2025, Calendar.MAY, 25, 18, 0).timeInMillis)
                            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, GregorianCalendar(2025, Calendar.MAY, 25, 20, 0).timeInMillis)
                        }
                        context.startActivity(calendarIntent)
                    }
                )

                // Pagina Eventos Marriot
                InfoRow(
                    icon = Icons.Default.Share,
                    text = "Confirma tu Asistencia",
                    onClick = {
                        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.marriott.com/es/hotels/uiodt-jw-marriott-hotel-quito/events/"))
                        context.startActivity(urlIntent)
                    }
                )

                // Solo Info Mail
                InfoRow(
                    icon = Icons.Default.Email,
                    text = "JayAlexanderUCE@gmail.com",
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("JayAlexanderUCE@gmail.com")
                        }
                        context.startActivity(emailIntent)
                    }
                )

                // InfoRow 5: Teléfono
                InfoRow(
                    icon = Icons.Default.Phone,
                    text = "+593987654321",
                    onClick = {
                        val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:+593987654321")
                        }
                        context.startActivity(phoneIntent)
                    }
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String, onClick: (() -> Unit)?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 27.dp)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
   // MiGradoTheme {
//        SaludoText(message = "Te Invito a Mi Grado ", from = "De Andres")
   // }

    InvitationCard()
}