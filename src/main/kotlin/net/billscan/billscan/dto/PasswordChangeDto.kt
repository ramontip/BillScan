package net.billscan.billscan.dto

import com.sun.istack.NotNull
import javax.validation.constraints.Size

class PasswordChangeDto(
    @field:NotNull
    @field:Size(min = 6, max = 90)
    var currentPassword: String? = null,

    @field:NotNull
    @field:Size(min = 6, max = 90)
    var newPassword: String? = null,

    @field:NotNull
    @field:Size(min = 6, max = 90)
    var confirmPassword: String? = null
) {

}