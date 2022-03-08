package net.billscan.billscan.service

import net.billscan.billscan.entity.Thumbnail
import net.billscan.billscan.entity.User
import net.billscan.billscan.repository.ThumbnailRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional


@Service
class ThumbnailService(
    val thumbnailRepository: ThumbnailRepository,
    val mainService: MainService,
    val userService: UserService,
) {
    fun findById(id: UUID): Thumbnail? {
        return thumbnailRepository.findFileById(id)
    }

    @Transactional
    fun store(file: MultipartFile, user: User): Thumbnail? {
        return save(
            Thumbnail(
                user = user,
                name = file.originalFilename,
                type = file.contentType,
                size = file.size,
                data = mainService.compressImage(file),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ), user
        )
    }

    @Transactional
    fun save(file: Thumbnail, user: User): Thumbnail {
        val thumbnail = thumbnailRepository.save(file)
        user.thumbnail = thumbnail
        userService.save(user)
        return thumbnail
    }

    @Transactional
    fun delete(thumbnail: Thumbnail) {
        thumbnailRepository.deleteThumbnailById(thumbnail.id!!)
        return
    }
}