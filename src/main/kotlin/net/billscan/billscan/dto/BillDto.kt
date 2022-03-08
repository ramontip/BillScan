package net.billscan.billscan.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

class BillDto(
    @field:NotNull
    @field:NotEmpty
    @field:Length(min = 1, max = 100)
    var store: String? = null,

    @field:NotNull
    @field:NotEmpty
    var date: String? = null,

    @field:NotNull
    @field:Positive
    var purchasePrice: Float? = null,

    var productTitles: List<String>? = null,

    var productPrices: List<String>? = null,
) {

}