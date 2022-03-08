package net.billscan.billscan.repository

import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

@Repository
interface BillRepository : JpaRepository<Bill, Int> {

    @Query("FROM Bill WHERE id = :id")
    fun findBillById(@Param("id") id: UUID): Bill?

    @Query("SELECT b FROM Bill b JOIN b.household h JOIN h.users u WHERE u = :user AND b.purchaseDate BETWEEN :dateFrom AND :dateTo ORDER BY b.purchaseDate ASC")
    fun findByUserAndRange(
        @Param("user") user: User,
        @Param("dateFrom") dateFrom: LocalDate,
        @Param("dateTo") dateTo: LocalDate
    ): List<Bill>?

    @Query("SELECT b FROM Bill b JOIN b.household h WHERE h = :household AND b.purchaseDate BETWEEN :dateFrom AND :dateTo ORDER BY b.purchaseDate ASC")
    fun findByHouseholdAndRange(
        @Param("household") household: Household,
        @Param("dateFrom") dateFrom: LocalDate,
        @Param("dateTo") dateTo: LocalDate
    ): List<Bill>?

    @Query("SELECT b.purchaseDate FROM Bill b JOIN b.household h JOIN h.users u WHERE u = :user ORDER BY b.purchaseDate DESC")
    fun findPurchaseDatesByUser(
        @Param("user") user: User
    ): List<LocalDate>?

    @Query("SELECT b.purchaseDate FROM Bill b JOIN b.household h WHERE h = :household ORDER BY b.purchaseDate DESC")
    fun findPurchasePeriodsByHousehold(
        @Param("household") household: Household
    ): List<LocalDate>?

    @Query("FROM Bill b JOIN b.store s JOIN b.household h JOIN h.users u WHERE u = :user AND s.title LIKE CONCAT('%', LOWER(COALESCE(:term, '')), '%') ORDER BY b.purchaseDate DESC")
    fun findByUserAndStoreTitle(@Param("user") user: User, @Param("term") term: String): List<Bill>?

    @Transactional
    @Modifying
    @Query("DELETE FROM Bill WHERE id = :id")
    fun deleteBillById(@Param("id") id: UUID)
}
