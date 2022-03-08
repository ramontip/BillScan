package net.billscan.billscan.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*


@Entity
class Bill(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var household: Household? = null,

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User? = null,

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var store: Store? = null,

    var purchaseDate: LocalDate? = null,

    var purchasePrice: Float? = null,

    var createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null,

    // Other Relationships (no db column is created)
    @OneToMany(mappedBy = "bill")
    var products: List<Product>? = null,

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    var files: Set<File>? = null,

    ) : Comparable<Bill>, Serializable {

    override fun compareTo(other: Bill): Int {
        return compareValues(id, other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Bill
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun getCreatedAtFormatted(): String {
        val dateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return dateTimeFormat.format(createdAt)
    }

    fun getPurchaseDateFormatted(): String {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateFormat.format(purchaseDate)
    }
}