package net.billscan.billscan.repository

import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface HouseholdRepository : JpaRepository<Household, Int> {

    @Query("FROM Household WHERE id = :id")
    fun findHouseholdById(@Param("id") id: UUID): Household?

    @Query("SELECT h FROM Household h JOIN h.users u WHERE h.id = :id AND u = :user")
    fun findHouseholdByIdAndUser(@Param("id") id: UUID, @Param("user") user: User): Household?

    @Query("FROM Household WHERE invitationCode = :invitationCode")
    fun findByInvitationCode(@Param("invitationCode") invitationCode: String): Household?

    @Transactional
    @Modifying
    @Query("DELETE FROM Household WHERE id = :id")
    fun deleteHouseholdById(@Param("id") id: UUID)
}
