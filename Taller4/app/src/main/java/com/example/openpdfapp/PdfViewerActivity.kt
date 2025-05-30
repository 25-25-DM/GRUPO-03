package com.example.openpdfapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity

class PdfViewerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pdfUriString = intent.getStringExtra("pdf_uri")
        val pdfUri = pdfUriString?.let { Uri.parse(it) }

        if (pdfUri != null) {
            try {
                val viewPdfIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(pdfUri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                if (viewPdfIntent.resolveActivity(packageManager) != null) {
                    startActivity(viewPdfIntent)
                    finish() // Finaliza esta actividad una vez que se lanzó el intent
                } else {
                    // Si no hay aplicación para abrir el PDF
                    Toast.makeText(this, "No se encontró una aplicación para abrir el PDF. Por favor, instala un lector de PDF (ej. Google PDF Viewer, Adobe Reader).", Toast.LENGTH_LONG).show()
                    finish() // Finaliza si no hay app para abrirlo
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error al intentar abrir el PDF: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                finish() // Finaliza en caso de error
            }
        } else {
            Toast.makeText(this, "URI del PDF no válida o no seleccionada.", Toast.LENGTH_SHORT).show()
            finish() // Finaliza si la URI es nula
        }
    }
}