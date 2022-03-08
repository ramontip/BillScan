package net.billscan.billscan.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
class User(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    var firstname: String? = null,

    var lastname: String? = null,

    @Column(nullable = false, unique = true)
    var username: String,

    var password: String,

    @OneToOne
    @JoinColumn(name = "thumbnail_id", referencedColumnName = "id")
    var thumbnail: Thumbnail? = null,

    var createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null,

    // Other Relationships (connecting db table is created)
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "title ASC")
    var households: Set<Household>? = null,

    @OneToMany(mappedBy = "createdByUser")
    var householdsCreated: List<Household>? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var files: List<File>? = null,

    ) : Comparable<User>, Serializable {

    override fun compareTo(other: User): Int {
        return compareValues(id, other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun getFullName(): String {
        return "$firstname $lastname"
    }
}