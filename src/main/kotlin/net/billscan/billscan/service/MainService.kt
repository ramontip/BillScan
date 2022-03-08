package net.billscan.billscan.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam


@Service
class MainService(

) {
    fun compressImage(image: MultipartFile): ByteArray? {
        val inputStream = image.inputStream
        val outputStream = ByteArrayOutputStream()
        val imageQuality = 0.4f

        val format = image.contentType!!.split("/").last()
        
        // Create the buffered image
        val bufferedImage = ImageIO.read(inputStream)

        // Get image writers
        val imageWriters = ImageIO.getImageWritersByFormatName(format)
        check(imageWriters.hasNext()) { "Writers Not Found!!" }
        val imageWriter = imageWriters.next()
        val imageOutputStream = ImageIO.createImageOutputStream(outputStream)
        imageWriter.output = imageOutputStream
        val imageWriteParam = imageWriter.defaultWriteParam

        // Set the compress quality metrics
        imageWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        imageWriteParam.compressionQuality = imageQuality

        // Compress and insert the image into the byte array.
        imageWriter.write(null, IIOImage(bufferedImage, null, null), imageWriteParam)
        val imageBytes: ByteArray = outputStream.toByteArray()

        // close all streams
        inputStream.close()
        outputStream.close()
        imageOutputStream.close()
        imageWriter.dispose()
        return imageBytes
    }
}