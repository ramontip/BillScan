package net.billscan.billscan.repository

import net.billscan.billscan.entity.Thumbnail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface ThumbnailRepository : JpaRepository<Thumbnail, Int> {

    @Query("FROM Thumbnail WHERE id = :id")
    fun findFileById(@Param("id") id: UUID): Thumbnail?

    @Transactional
    @Modifying
    @Query("DELETE FROM Thumbnail WHERE id = :id")
    fun deleteThumbnailById(@Param("id") id: UUID)
}
