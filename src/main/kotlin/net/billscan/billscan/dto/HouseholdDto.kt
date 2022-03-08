package net.billscan.billscan.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class HouseholdDto(
    @field:NotNull
    @field:NotEmpty
    @field:Length(min = 3, max = 100)
    var title: String? = null
) {
}