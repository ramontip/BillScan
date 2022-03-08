package net.billscan.billscan.repository

import net.billscan.billscan.entity.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface FileRepository : JpaRepository<File, Int> {

    @Query("FROM File WHERE id = :id")
    fun findFileById(@Param("id") id: UUID): File?

    @Transactional
    @Modifying
    @Query("DELETE FROM File WHERE id = :id")
    fun deleteFileById(@Param("id") id: UUID)
}
