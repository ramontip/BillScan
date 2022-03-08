package net.billscan.billscan.repository

import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface StoreRepository : JpaRepository<Store, Int> {

    @Query("FROM Store WHERE id = :id")
    fun findStoreById(@Param("id") id: UUID): Store?

    @Query("FROM Store WHERE title = :title")
    fun findStoreByTitle(@Param("title") title: String): Store?

}
