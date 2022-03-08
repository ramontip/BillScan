package net.billscan.billscan.controller.user

import net.billscan.billscan.dto.HouseholdDto
import net.billscan.billscan.dto.InvitationCodeDto
import net.billscan.billscan.entity.Household
import net.billscan.billscan.service.BillService
import net.billscan.billscan.service.HouseholdService
import net.billscan.billscan.service.UserService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
class HouseholdController(
    val userService: UserService,
    val householdService: HouseholdService,
    val billService: BillService,
    val messageSource: MessageSource
) {

    /*
     * Dashboard
     * display all households
     */
    @RequestMapping(*["/dashboard"], method = [RequestMethod.GET])
    fun dashboard(
        model: Model
    ): String {
        val user = userService.getCurrentUser()!!

        model["households"] = user.households ?: ""
        return "user/dashboard"
    }

    /*
     * Join Create
     * show join household form
     */
    @RequestMapping(*["/household/join"], method = [RequestMethod.GET])
    fun joinCreate(
        model: Model
    ): String {
        model["invitationCodeDto"] = InvitationCodeDto()
        return "user/household/join"
    }

    /*
     * Join Store
     * connect user with household
     */
    @RequestMapping("/household/join", method = [RequestMethod.POST])
    fun joinStore(
        @ModelAttribute @Valid invitationCodeDto: InvitationCodeDto,
        bindingResult: BindingResult,
        model: Model,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {

        val user = userService.getCurrentUser()!!

        // check validation errors
        if (bindingResult.hasErrors()) {
            println("household join binding has errors")
            return "user/household/join"
        }

        // check if invitationCode is valid
        val household: Household? = householdService.findByInvitationCode(invitationCodeDto.invitationCode!!)
        if (household == null) {
            model["messageError"] = messageSource.getMessage("message.error.invitationCodeInvalid", null, locale)
            return "user/household/join"
        }

        // check if user already in household
        if (user.households?.contains(household) == true) {
            model["messageError"] = messageSource.getMessage("message.error.alreadyInHousehold", null, locale)
            return "user/household/join"
        }

        // add household to user
        userService.addHousehold(user, household)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.householdEntered", null, locale)
        )
        return "redirect:/household/${household.id.toString()}/show"
    }

    /*
     * Create
     * show create form
     */
    @RequestMapping(*["/household/create"], method = [RequestMethod.GET])
    fun create(
        model: Model
    ): String {
        model["householdDto"] = HouseholdDto()
        return "user/household/create"
    }

    /*
     * Store
     * save household to database
     */
    @RequestMapping("/household/create", method = [RequestMethod.POST])
    fun store(
        @ModelAttribute @Valid householdDto: HouseholdDto,
        bindingResult: BindingResult,
        model: Model,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {

        val user = userService.getCurrentUser()!!

        // check validation errors
        if (bindingResult.hasErrors()) {
            println("household binding has errors")
            model["householdDto"] = householdDto
            return "user/household/create"
        }

        // store household
        val household = householdService.save(householdService.convertHouseholdDtoToHousehold(householdDto))

        // add household to user
        userService.addHousehold(user, household)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.householdCreated", null, locale)
        )
        return "redirect:/household/${household.id.toString()}/show"
    }

    /*
     * Show
     * display a specific household
     */
    @RequestMapping("/household/{householdId}/show", method = [RequestMethod.GET])
    fun show(
        @PathVariable("householdId") householdId: UUID,
        @RequestParam(required = false) period: LocalDate?,
        @RequestParam(required = false) sort: Int?,
        redirectAttributes: RedirectAttributes,
        model: Model,
        locale: Locale,
    ): String {
        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        if (household == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }
        model["household"] = household

        // get purchase periods for list
        val purchasePeriods = billService.findPurchasePeriodsByHousehold(household) ?: emptyList()
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
        var bills = billService.findByHouseholdAndRange(household, dateFrom, dateTo)
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

        // get Shops and Prices
        val storesPurchasePrice = HashMap<String, Float>()

        for (bill in bills!!) {
            if (storesPurchasePrice.containsKey(bill.store!!.title!!)) {
                storesPurchasePrice[bill.store!!.title!!] =
                    storesPurchasePrice[bill.store!!.title!!]!! + bill.purchasePrice!!
            } else {
                storesPurchasePrice[bill.store!!.title!!] = bill.purchasePrice!!.toFloat()
            }
        }

        var labels = storesPurchasePrice.map { it.key }.toList()
        var newLabel = listOf<String>()

        labels.forEach {
            var label = it
            label = '"' + label + '"'
            newLabel = newLabel.plus(label)
        }

        val data = storesPurchasePrice.map { it.value }.toList()

        model["labels"] = newLabel
        model["data"] = data

        storesPurchasePrice.forEach { (k, v) -> println("$k = $v") }




        return "user/household/show"
    }

    /*
     * Edit
     * show edit form
     */
    @RequestMapping("/household/{householdId}/edit", method = [RequestMethod.GET])
    fun edit(
        @PathVariable("householdId") householdId: UUID,
        redirectAttributes: RedirectAttributes,
        locale: Locale,
        model: Model,
    ): String {

        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        if (household == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        model["household"] = household
        model["householdDto"] = HouseholdDto()

        return "user/household/manage"
    }

    /*
     * Update
     * save updated household
     */
    @RequestMapping("/household/{householdId}/updateTitle", method = [RequestMethod.POST])
    fun updateTitle(
        @PathVariable("householdId") householdId: UUID,
        @ModelAttribute @Valid householdDto: HouseholdDto,
        bindingResult: BindingResult,
        model: Model,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {

        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        if (household == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        // check validation errors
        if (bindingResult.hasErrors()) {
            println("household binding has errors")
            model["household"] = household
            model["householdDto"] = householdDto
            return "user/household/manage"
        }

        // update household
        household.title = householdDto.title
        householdService.save(household)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )
        return "redirect:/household/${household.id.toString()}/show"
    }

    /*
     * Delete User
     * remove user from household
     */
    @RequestMapping("/household/{householdId}/delete-user/{userId}", method = [RequestMethod.GET])
    fun deleteUser(
        @PathVariable("householdId") householdId: UUID,
        @PathVariable("userId") userId: UUID,
        model: Model,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        // check permission
        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        if (household == null || household.createdByUser?.id != user.id) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        val deleteUser = userService.findById(userId)

        if (deleteUser != null) {
            // delete user from household
            userService.removeHousehold(deleteUser, household)

            // Return with Success Toast
            redirectAttributes.addFlashAttribute(
                "toastSuccess",
                messageSource.getMessage("message.success.changesSaved", null, locale)
            )
        }
        return "redirect:/household/${household.id.toString()}/show"
    }

    /*
     * Delete
     * remove resource from database
     */
    @RequestMapping("/household/{householdId}/delete", method = [RequestMethod.GET])
    fun delete(
        @PathVariable("householdId") householdId: UUID,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        // Check permission
        val user = userService.getCurrentUser()!!
        val household = householdService.findByIdAndUser(householdId, user)

        if (household?.createdByUser != user) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        // delete household
        householdService.delete(household)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )

        return "redirect:/dashboard"
    }
}