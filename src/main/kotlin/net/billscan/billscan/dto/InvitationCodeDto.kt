package net.billscan.billscan.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class InvitationCodeDto(
    @field:NotNull
    @field:NotEmpty
    var invitationCode: String? = null
) {
}