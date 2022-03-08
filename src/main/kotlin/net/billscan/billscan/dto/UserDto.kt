package net.billscan.billscan.dto

import javax.validation.constraints.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class UserDto(
    @field:NotNull
    @field:Size(min = 1, max = 90)
    var firstname: String? = null,

    @field:NotNull
    @field:Size(min = 1, max = 90)
    var lastname: String? = null,

    @field:NotNull
    @field:Size(min = 1, max = 90)
    @field:Email
    var username: String,

    @field:NotNull
    @field:Size(min = 6, max = 90)
    var password: String,

    @field:NotNull
    @field:Size(min = 6, max = 90)
    var confirmPassword: String? = null,
) {
}