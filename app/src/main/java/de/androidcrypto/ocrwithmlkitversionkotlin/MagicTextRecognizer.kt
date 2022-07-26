package de.androidcrypto.ocrwithmlkitversionkotlin

import android.media.Image
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition

class MagicTextRecognizer(private val onTextFound: (String) -> Unit) {
    fun recognizeImageText(image: Image, rotationDegrees: Int, onResult: (Boolean) -> Unit) {
        val inputImage = InputImage.fromMediaImage(image, rotationDegrees)
        TextRecognition.getClient()
            .process(inputImage)
            .addOnSuccessListener { recognizedText ->
                processTextFromImage(recognizedText)
                onResult(true)
            }
            .addOnFailureListener { error ->
                Log.d(TAG, "Failed to recognize image text")
                error.printStackTrace()
                onResult(false)
            }
    }

    private fun processTextFromImage(text: Text) {
        text.textBlocks.joinToString {
            it.text.lines().joinToString(" ")
        }.let {
            if (!it.isBlank()) {
                onTextFound(it)
            }
        }
    }

    companion object {
        private val TAG = MagicTextRecognizer::class.java.name
    }
}