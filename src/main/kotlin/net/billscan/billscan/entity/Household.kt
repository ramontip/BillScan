package net.billscan.billscan.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*


@Entity
class Household(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "createdByUserId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var createdByUser: User? = null,

    var title: String? = null,

    @Column(nullable = false, unique = true)
    var invitationCode: String,

    var createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null,

    // Other Relationships
    @ManyToMany(mappedBy = "households")
    var users: Set<User>? = null,

    // Other Relationships (no db column is created)
    @OneToMany(mappedBy = "household")
    var bills: List<Bill>? = null,

    ) : Comparable<Household>, Serializable {

    override fun compareTo(other: Household): Int {
        return compareValues(id, other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Household
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun getCreatedAtFormatted(): String {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return dateFormat.format(createdAt)
    }
}