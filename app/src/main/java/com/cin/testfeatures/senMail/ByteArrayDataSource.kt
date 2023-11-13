package com.cin.testfeatures.senMail

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.activation.DataSource

class ByteArrayDataSource(private var data: ByteArray, private var type: String? = null) : DataSource {

    override fun getContentType(): String {
        return type ?: "application/octet-stream"
    }

    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(data)
    }

    override fun getName(): String {
        return "ByteArrayDataSource"
    }

    @Throws(IOException::class)
    override fun getOutputStream(): OutputStream {
        throw IOException("Not Supported")
    }
}