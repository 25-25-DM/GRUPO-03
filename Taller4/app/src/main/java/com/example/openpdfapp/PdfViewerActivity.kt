package com.example.openpdfapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity

class PdfViewerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pdfUriString = intent.getStringExtra("pdf_uri")
        val pdfUri = pdfUriString?.let { Uri.parse(it) }

        if (pdfUri != null) {
            Log.d("PDF_URI", "URI: $pdfUri")

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(pdfUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                startActivity(Intent.createChooser(intent, "Abrir PDF con..."))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No hay apps para abrir PDFs", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                finish()
            }
        } else {
            Toast.makeText(this, "No se recibió un archivo válido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}