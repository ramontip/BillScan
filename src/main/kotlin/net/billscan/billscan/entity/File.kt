package net.billscan.billscan.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*


@Entity
class File(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var bill: Bill? = null,

    var name: String? = null,

    var type: String? = null,

    var size: Long? = null,

    @Lob
    var data: ByteArray? = null,

    var createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null,

    ) : Comparable<File>, Serializable {

    override fun compareTo(other: File): Int {
        return compareValues(id, other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as File
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

}