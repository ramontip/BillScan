package net.billscan.billscan.repository

import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Product
import net.billscan.billscan.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface ProductRepository : JpaRepository<Product, Int> {

    @Query("FROM Product WHERE id = :id")
    fun findProductById(@Param("id") id: UUID): Product?

    @Query("FROM Product p JOIN p.bill b JOIN b.household h JOIN h.users u WHERE u = :user AND p.title LIKE CONCAT('%', LOWER(COALESCE(:term, '')), '%') ORDER BY p.createdAt DESC")
    fun findByUserAndTitle(@Param("user") user: User, @Param("term") term: String): List<Product>?

    @Transactional
    @Modifying
    @Query("DELETE FROM Product where bill = :bill")
    fun deleteByBill(@Param("bill") bill: Bill)
}
