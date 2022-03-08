package net.billscan.billscan.repository

import net.billscan.billscan.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface UserRepository : JpaRepository<User, Int> {

    @Query("FROM User WHERE username = :username")
    fun findByUsername(@Param("username") username: String): User?

    @Query("FROM User WHERE id = :id")
    fun findByUserId(@Param("id") id: UUID): User?

    @Transactional
    @Modifying
    @Query("DELETE FROM User WHERE id = :id")
    fun deleteUserById(@Param("id") id: UUID)
}
