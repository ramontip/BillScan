package net.billscan.billscan.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
class PasswordResetToken(
    @Id
    @GeneratedValue
    @Column(length = 16)
    var id: UUID? = null,

    var token: String? = null,

    @OneToOne
    var user: User? = null,

    var expiryDate: LocalDateTime = LocalDateTime.now().plusHours(24)
) {
}