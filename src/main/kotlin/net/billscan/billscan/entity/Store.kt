package net.billscan.billscan.entity

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*


@Entity
class Store(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    var title: String? = null,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now(),

    var isDeleted: Boolean? = null,

    ) : Comparable<Store>, Serializable {

    override fun compareTo(other: Store): Int {
        return compareValues(id, other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Store
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}