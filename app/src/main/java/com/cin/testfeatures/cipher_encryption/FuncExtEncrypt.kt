package com.cin.testfeatures.cipher_encryption

import android.util.Base64
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val cipherTransformation = "AES/CBC/PKCS5Padding"
private const val aesEncryptionAlgorithm = "AES"

@Throws(
    UnsupportedEncodingException::class,
    InvalidKeyException::class,
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidAlgorithmParameterException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
fun String.encrypt(key: String): String {
    val plainTextBytes = this.toByteArray(Charsets.UTF_8)
    val keyBytes: ByteArray = getKeyBytes(key)
    return Base64.encodeToString(encrypt(plainTextBytes, keyBytes, keyBytes), Base64.DEFAULT)
}

@Throws(
    KeyException::class,
    GeneralSecurityException::class,
    GeneralSecurityException::class,
    InvalidAlgorithmParameterException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class,
    IOException::class
)
fun String.decrypt(key: String): String {
    val cipheredBytes = Base64.decode(this, Base64.DEFAULT)
    val keyBytes = getKeyBytes(key)
    return String(decrypt(cipheredBytes, keyBytes, keyBytes), Charsets.UTF_8)
}

@Throws(
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    InvalidAlgorithmParameterException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
private fun decrypt(cipherText: ByteArray, key: ByteArray, initialVector: ByteArray): ByteArray {
    val cipher: Cipher = Cipher.getInstance(cipherTransformation)
    val secretKeySpec = SecretKeySpec(key, aesEncryptionAlgorithm)
    val ivParameterSpec = IvParameterSpec(initialVector)
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
    return cipher.doFinal(cipherText)
}

@Throws(
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    InvalidAlgorithmParameterException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
private fun encrypt(plainText: ByteArray, key: ByteArray, initialVector: ByteArray): ByteArray {
    val cipher = Cipher.getInstance(cipherTransformation)
    val secretKeySpec = SecretKeySpec(key, aesEncryptionAlgorithm)
    val ivParameterSpec = IvParameterSpec(initialVector)
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
    return cipher.doFinal(plainText)
}

@Throws(UnsupportedEncodingException::class)
private fun getKeyBytes(key: String): ByteArray {
    val keyBytes = ByteArray(16)
    val parameterKeyBytes = key.toByteArray(Charsets.UTF_8)
    System.arraycopy(
        parameterKeyBytes, 0, keyBytes, 0,
        parameterKeyBytes.size.coerceAtMost(keyBytes.size)
    )
    return keyBytes
}