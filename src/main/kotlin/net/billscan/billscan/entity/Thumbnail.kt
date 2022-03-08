package net.billscan.billscan.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
class Thumbnail(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    @OneToOne(mappedBy = "thumbnail")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User? = null,

    var name: String? = null,

    var type: String? = null,

    var size: Long? = null,

    @Lob
    var data: ByteArray? = null,

    var createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null,

    ) : Comparable<Thumbnail>, Serializable {

    override fun compareTo(other: Thumbnail): Int {
        return compareValues(id, other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Thumbnail
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}