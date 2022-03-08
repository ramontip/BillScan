package net.billscan.billscan.controller.user

import net.billscan.billscan.service.BillService
import net.billscan.billscan.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Controller
class ExpensesController(
    val userService: UserService,
    val billService: BillService
) {

    /*
     * Expenses
     * display expenses page
     */
    @RequestMapping(*["/expenses"], method = [RequestMethod.GET])
    fun expenses(
        @RequestParam(required = false) period: LocalDate?,
        @RequestParam(required = false) sort: Int?,
        model: Model,
        locale: Locale
    ): String {
        val user = userService.getCurrentUser()!!

        // get purchase periods for list
        val purchasePeriods = billService.findPurchasePeriodsByUser(user) ?: emptyList()
        val purchasePeriodsPair = mutableListOf<Pair<LocalDate, String>>()
        for (purchasePeriod in purchasePeriods) {
            val purchasePeriodFormatted = purchasePeriod.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMAN)
                .toString() + " " + purchasePeriod.format(DateTimeFormatter.ofPattern("yyyy")).toString()

            purchasePeriodsPair.add(Pair(purchasePeriod, purchasePeriodFormatted))
        }
        model["purchasePeriods"] = purchasePeriodsPair.ifEmpty {
            ""
        }

        // set selected purchase period
        var dateFrom: LocalDate? = null
        var dateTo: LocalDate? = null
        val purchasePeriodSelected: Pair<LocalDate, String>
        if (period != null) {
            // set period by selected date
            dateFrom = period.withDayOfMonth(1)
            dateTo = period.withDayOfMonth(dateFrom.lengthOfMonth())

            purchasePeriodSelected = purchasePeriodsPair.find { it.first == period }!!
        } else {
            // set period by current date
            dateFrom = LocalDate.now().withDayOfMonth(1)
            dateTo = LocalDate.now().withDayOfMonth(dateFrom.lengthOfMonth())
            val purchasePeriodSelectedFormatted =
                dateFrom.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMAN)
                    .toString() + " " + dateFrom.format(DateTimeFormatter.ofPattern("yyyy")).toString()

            purchasePeriodSelected = Pair(dateFrom, purchasePeriodSelectedFormatted)
        }
        model["purchasePeriodSelected"] = purchasePeriodSelected ?: ""

        // get and sort bills
        var bills = billService.findByUserAndRange(user, dateFrom, dateTo)
        var totalExpenses = 0.0F
        if (bills != null) {
            totalExpenses = bills.map { it.purchasePrice }.sumOf { it!!.toDouble() }.toFloat()

            bills = when (sort) {
                1 -> bills.sortedBy { it.purchasePrice }
                2 -> bills.sortedBy { it.store?.title }
                else -> bills
            }
        }

        model["bills"] = bills ?: ""
        model["totalExpenses"] = totalExpenses.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

        // get Household and Prices
        val householdPurchasePrice = HashMap<String, Float>()

        for (bill in bills!!) {
            if (householdPurchasePrice.containsKey(bill.household!!.title!!)) {
                householdPurchasePrice[bill.household!!.title!!] =
                    householdPurchasePrice[bill.household!!.title!!]!! + bill.purchasePrice!!
            } else {
                householdPurchasePrice[bill.household!!.title!!] = bill.purchasePrice!!.toFloat()
            }
        }

        var labels = householdPurchasePrice.map { it.key }.toList()
        var newLabel = listOf<String>()

        labels.forEach {
            var label = it
            label = '"' + label + '"'
            newLabel = newLabel.plus(label)
        }

        val data = householdPurchasePrice.map { it.value }.toList()

        model["labels"] = newLabel
        model["data"] = data

        householdPurchasePrice.forEach { (k, v) -> println("$k = $v") }

        return "user/expenses"
    }

}