package ca.uwaterloo.cs.api

import ca.uwaterloo.cs.AppState
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object Encryption {
    // string -> secretkey
    fun generateSymmetricKey(str: String): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val salt = ByteArray(100)
        salt.fill(1)
        val spec: KeySpec = PBEKeySpec(str.toCharArray(), salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    // secretkey -> cipher
    fun initCipherFromSecretKey(secretKey: SecretKey, isEncrypt: Boolean, iv: ByteArray?): Cipher {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        if (isEncrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        } else {
            val ivParameterSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        }
        return cipher
    }

    // secretkey.toString
    @OptIn(ExperimentalEncodingApi::class)
    fun secretKeyToStr(key: SecretKey): String {
        return Base64.encode(key.encoded)
    }

    // string.toSecretKey (string must be representation of existing secretkey)
    @OptIn(ExperimentalEncodingApi::class)
    fun strToSecretKey(keystr: String): SecretKey {
        val decodedKey: ByteArray = Base64.decode(keystr)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
    }

    // add a new symmetric key to prefs if it doesn't exist already
    fun addKeyToPrefs(appState: AppState, symkey: String) {
        val prefs = appState.encryptedKeyValStore
        val userid = appState.userId.value
        if (!prefs.contains(symkey)) {
            val secretkey = generateSymmetricKey(userid)
            val keystr = secretKeyToStr(secretkey)
            prefs.edit().putString(symkey, keystr).apply()
        }
    }

    fun getCipherFromPrefs(appState: AppState, isEncrypt: Boolean, iv: ByteArray?): Cipher? {
        try {
            val symkey = "photo_encryption_key"
            addKeyToPrefs(appState, symkey)

            val prefs = appState.encryptedKeyValStore
            val secretKeyStr = prefs.getString(symkey, "")

            val secretkey = if (secretKeyStr == null || secretKeyStr == "") {
                // this shouldn't ever run if addKeyToPrefs worked properly
                println("did not find key in prefs")
                generateSymmetricKey(appState.userId.value)
            } else {
                strToSecretKey(secretKeyStr)
            }
            return initCipherFromSecretKey(secretkey, isEncrypt, iv)
        }catch( e: Exception){
            println("error in getCipherFromPrefs")
            println(e)
            return null
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun encrypt(appState: AppState, message: ByteArray): String? {
        try {
            val cipher = getCipherFromPrefs(appState, isEncrypt = true, null)
            val iv = cipher?.iv
            if (iv != null) {
                return Base64.encode(iv + cipher.doFinal(message)!!)
            }
        } catch (e: Exception) {
            println("error in encrypt")
            println(e)
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decrypt(appState: AppState, encryptedMessage: String): ByteArray? {
        try {
            val bytes: ByteArray = Base64.decode(encryptedMessage)
            val iv = getIV(bytes)
            val cipherText = getCipherText(bytes)
            val cipher = getCipherFromPrefs(appState, isEncrypt = false, iv)
            return cipher?.doFinal(cipherText)
        } catch (e: Exception) {
            println("error in decrypt")
            println(e)
        }
        return null
    }

    private fun getIV(bytes: ByteArray): ByteArray {
        return bytes.copyOfRange(0, 16)
    }

    private fun getCipherText(bytes: ByteArray): ByteArray {
        return bytes.copyOfRange(16, bytes.size)
    }


}