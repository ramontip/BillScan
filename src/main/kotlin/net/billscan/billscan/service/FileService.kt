package net.billscan.billscan.service

import net.billscan.billscan.entity.File
import net.billscan.billscan.entity.User
import net.billscan.billscan.repository.FileRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional


@Service
class FileService(
    val fileRepository: FileRepository,
    val mainService: MainService,
) {
    fun findById(id: UUID): File? {
        return fileRepository.findFileById(id)
    }

    @Transactional
    fun store(file: MultipartFile, user: User): File? {
        return save(
            File(
                user = user,
                name = file.originalFilename,
                type = file.contentType,
                size = file.size,
                data = mainService.compressImage(file),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )
    }

    @Transactional
    fun save(file: File): File {
        fileRepository.save(file)
        return file
    }

    @Transactional
    fun delete(file: File) {
        fileRepository.deleteFileById(file.id!!)
        return
    }
}