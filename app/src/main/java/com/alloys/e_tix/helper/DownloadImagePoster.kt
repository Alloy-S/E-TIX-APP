package com.alloys.e_tix.helper

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.File

object DownloadImagePoster {

    class DownloadImagesTask(private val callback: Callback) : AsyncTask<Void, Void, List<File>>() {

        interface Callback {
            fun onDownloadComplete(imageFiles: List<File>)
            fun onDownloadError(error: String)
        }

        override fun doInBackground(vararg params: Void?): List<File> {
            return downloadImages()
        }

        private fun downloadImages(): List<File> {
            val storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
            val storageRef = storage.reference.child("img_poster_film")

            // Specify the local folder where images will be stored
            val localFolder = File(Environment.getExternalStorageDirectory(), "DownloadedImages")
            localFolder.mkdirs()

            val imageFiles = mutableListOf<File>()

            // List all items in the storage reference
            try {
                storageRef.listAll().addOnSuccessListener {
                    val allListfile = it.items
                    for (storageReference in allListfile) {
                        val localFile = File(localFolder, storageReference.name)

                        // Download the image file
                        try {
                            storageReference.getFile(localFile).addOnSuccessListener {
                                imageFiles.add(localFile)
//                                Log.d("DownloadImages", "Image downloaded: ${localFile.absolutePath}")

                                if (imageFiles.size == allListfile.size) {

                                }
                            }
                        } catch (exception: Exception) {
                            Log.e("DownloadImages", "Error downloading image: ${exception.message}")
                            callback.onDownloadError(exception.message ?: "Unknown error")
                        }
                    }

                }

                return imageFiles
            } catch (exception: Exception) {
                Log.e("DownloadImages", "Error listing images: ${exception.message}")
                callback.onDownloadError(exception.message ?: "Unknown error")
                return emptyList()
            }
        }

        override fun onPostExecute(result: List<File>?) {
            // This method is called after the task is complete
            // You can perform any UI updates or additional operations here
            if (result != null) {
                callback.onDownloadComplete(result)
            }
        }
    }
}
