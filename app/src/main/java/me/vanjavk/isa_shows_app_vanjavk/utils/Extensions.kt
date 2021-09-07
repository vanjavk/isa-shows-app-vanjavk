package me.vanjavk.isa_shows_app_vanjavk

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Patterns
import java.io.File
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.URL
import android.graphics.Bitmap

import android.content.ContentValues
import java.io.OutputStream


const val MIN_PASSWORD_LENGTH = 6

fun CharSequence?.isValidEmail() =
    !(!isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun CharSequence.getUsername() = this.substring(0, this.indexOf('@')).orEmpty()

fun View.applyAnimation(resourceId: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, resourceId))

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    if (network != null) {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        if (networkCapabilities != null) {
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

const val GOOGLE_DNS = "8.8.8.8"
const val DNS_PORT = 53
const val TIMEOUT = 1500
const val TAG = "NetworkChecker"


fun checkInternetConnectivity(): Boolean {
    return try {
        val sock = Socket()
        val socketAddress: SocketAddress = InetSocketAddress(GOOGLE_DNS, DNS_PORT)
        sock.connect(socketAddress, TIMEOUT)
        sock.close()
        Log.d(TAG, "Network connection available")
        true
    } catch (e: IOException) {
        Log.d(TAG, "Network connection not available")
        false
    }
}




fun Uri.getFileFromUri(context: Context): File? {
    if (this.path == null) {
        return null
    }
    var realPath = String()
    val databaseUri: Uri
    val selection: String?
    val selectionArgs: Array<String>?
    if (this.path!!.contains("/document/image:")) {
        databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        selection = "_id=?"
        selectionArgs = arrayOf(DocumentsContract.getDocumentId(this).split(":")[1])
    } else {
        databaseUri = this
        selection = null
        selectionArgs = null
    }
    try {
        val column = "_data"
        val projection = arrayOf(column)
        val cursor = context.contentResolver.query(
            databaseUri,
            projection,
            selection,
            selectionArgs,
            null
        )
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                realPath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
    } catch (e: Exception) {
        Log.i("GetFileUri Exception:", e.message ?: "")
    }
    val path = if (realPath.isNotEmpty()) realPath else {
        when {
            this.path!!.contains("/document/raw:") -> this.path!!.replace(
                "/document/raw:",
                ""
            )
            this.path!!.contains("/document/primary:") -> this.path!!.replace(
                "/document/primary:",
                "/storage/emulated/0/"
            )
            else -> return null
        }
    }
    return File(path)
}

fun String.getImageSignature(urlString: String): String {
    val url = URL(urlString)
    return url.path + (System.currentTimeMillis() / (15 * 60 * 1000)).toString()
}
