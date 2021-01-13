package os.abuyahya.hashgeneratorapp

import android.util.Log
import androidx.lifecycle.ViewModel
import java.security.MessageDigest

class HomeViewModel: ViewModel() {

    fun getHash(plainText: String, algorithms: String): String{
        val bytes = MessageDigest.getInstance(algorithms).digest(plainText.toByteArray())
        return toHex(bytes)
    }

    private fun toHex(bytesArray: ByteArray): String {
        return bytesArray.joinToString("") { "%02x".format(it) }
    }
}
