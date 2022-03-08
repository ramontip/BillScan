package net.billscan.billscan.repository

import net.billscan.billscan.entity.PasswordResetToken
import net.billscan.billscan.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Int> {

    @Query("FROM PasswordResetToken where token = :token")
    fun findEntryByToken(@Param("token") token: String): PasswordResetToken?

    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken where user = :user")
    fun deleteByUser(user: User)

}
