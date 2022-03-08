package net.billscan.billscan.controller.user

import com.azure.ai.formrecognizer.FormRecognizerClientBuilder
import com.azure.ai.formrecognizer.models.FieldValueType
import com.azure.ai.formrecognizer.models.FormField
import com.azure.ai.formrecognizer.models.RecognizedForm
import com.azure.core.credential.AzureKeyCredential
import net.billscan.billscan.dto.BillDto
import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Household
import net.billscan.billscan.service.BillService
import net.billscan.billscan.service.FileService
import net.billscan.billscan.service.HouseholdService
import net.billscan.billscan.service.UserService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@Controller
class BillController(
    val userService: UserService,
    val householdService: HouseholdService,
    val billService: BillService,
    val fileService: FileService,
    val messageSource: MessageSource,
) {
    companion object {
        const val DEFAULT_PRODUCTS_LIMIT = 5
        const val PRODUCT_CONFIDENCE_LIMIT = 0.4
        const val VISION_API_ENDPOINT = "https://example.cognitiveservices.azure.com/"
        const val VISION_API_KEY = "00000"
    }

    /*
     * Populate Manage Form
     * insert model data
     */
    fun populateManageForm(
        model: Model,
        household: Household,
        bill: Bill? = null,
        billDto: BillDto = BillDto(),
        type: Int,
        variant: Int,
        productsLimit: Int = DEFAULT_PRODUCTS_LIMIT
    ): String {
        model["household"] = household
        model["bill"] = bill ?: ""
        model["billDto"] = billDto
        model["type"] = type
        model["variant"] = variant
        model["productsLimit"] = if (productsLimit < DEFAULT_PRODUCTS_LIMIT) {
            DEFAULT_PRODUCTS_LIMIT
        } else {
            productsLimit
        }
        return "user/bill/manage"
    }

    /*
     * Upload
     * show upload bill form
     */
    @RequestMapping(*["/household/{householdId}/bill/upload"], method = [RequestMethod.GET])
    fun create(
        @PathVariable("householdId") householdId: UUID,
        redirectAttributes: RedirectAttributes,
        model: Model,
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

        model["household"] = household
        model["type"] = 1
        model["variant"] = 2

        return "user/bill/manage"
    }

    /*
     * Store
     * save bill to database and storage
     */
    @RequestMapping("/household/{householdId}/bill/upload", method = [RequestMethod.POST])
    fun upload(
        @PathVariable("householdId") householdId: UUID,
        @RequestParam("file") file: MultipartFile,
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

        // Check If File is Image
        if (file.contentType?.startsWith("image/") != true) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notImage", null, locale)
            )
            return "redirect:/dashboard"
        }

        // Initialize Variables For API Response
        var store: String? = null
        var storeConfidence: Boolean? = null
        var date: String? = null
        var dateConfidence: Boolean? = null
        var purchasePrice: Float? = null
        var purchasePriceConfidence: Boolean? = null
        val productTitles = emptyList<String>().toMutableList()
        val productPrices = emptyList<String>().toMutableList()
        val productConfidences = emptyList<Boolean>().toMutableList()

        try {

            /*
             * API CALL
             * MICROSOFT AZURE / COGNITIVE SERVICES / VISION API / FORM RECOGNIZER
             * Implementation by rt
             */
            val formRecognizerClient = FormRecognizerClientBuilder()
                .credential(AzureKeyCredential(VISION_API_KEY))
                .endpoint(VISION_API_ENDPOINT)
                .buildClient()

            // System Logging
            System.out.printf("=============== BillScan Recognized Invoice =============== %n")

            // Analyze Bill from Uploaded File
            val poller =
                formRecognizerClient.beginRecognizeInvoices(file.inputStream, file.inputStream.available().toLong())
            val invoice: RecognizedForm = poller.finalResult.first()
            val recognizedInvoice: RecognizedForm = invoice
            val recognizedFields = recognizedInvoice.fields

            // Get Invoice Store
            val vendorNameField = recognizedFields["VendorName"]
            if (vendorNameField != null) {
                if (FieldValueType.STRING === vendorNameField.value.valueType) {
                    val merchantName = vendorNameField.value.asString()
                    // System Logging
                    System.out.printf("Vendor Name: %s, confidence: %.2f %n", merchantName, vendorNameField.confidence)
                    // Assign Values
                    store = merchantName
                    storeConfidence = vendorNameField.confidence >= PRODUCT_CONFIDENCE_LIMIT
                }
            }

            // Get Invoice Date
            val invoiceDateField = recognizedFields["InvoiceDate"]
            if (invoiceDateField != null) {
                if (FieldValueType.DATE === invoiceDateField.value.valueType) {
                    val invoiceDate = invoiceDateField.value.asDate()
                    // System Logging
                    System.out.printf("Invoice Date: %s, confidence: %.2f %n", invoiceDate, invoiceDateField.confidence)
                    // Assign Values
                    date = invoiceDate?.toString() ?: ""
                    dateConfidence = invoiceDateField.confidence >= PRODUCT_CONFIDENCE_LIMIT && date.isNotEmpty()
                }
            }

            // Get Invoice Price
            val invoiceTotalField = recognizedFields["InvoiceTotal"]
            if (invoiceTotalField != null) {
                if (FieldValueType.FLOAT === invoiceTotalField.value.valueType) {
                    val invoiceTotal = invoiceTotalField.value.asFloat()
                    // System Logging
                    System.out.printf(
                        "Invoice Total: %.2f, confidence: %.2f %n",
                        invoiceTotal,
                        invoiceTotalField.confidence
                    )
                    // Assign Values
                    purchasePrice = invoiceTotal
                    purchasePriceConfidence = invoiceTotalField.confidence >= PRODUCT_CONFIDENCE_LIMIT
                }
            }

            // Get Invoice Items
            val invoiceItemsField = recognizedFields["Items"]
            if (invoiceItemsField != null) {
                // System Logging
                System.out.printf("----- INVOICE ITEMS: ----- %n")
                if (FieldValueType.LIST == invoiceItemsField.value.valueType) {
                    // Convert API Response, Select Product Title (Description) and Price (Amount)
                    val invoiceItems: List<FormField> = invoiceItemsField.value.asList()
                    invoiceItems.stream()
                        .filter { FieldValueType.MAP == it.value.valueType }
                        .map { it.value.asMap() }
                        .forEach { formFieldMap ->
                            var description: String? = null
                            var descriptionConfidence: Float? = null
                            var amount: Float? = null
                            var amountConfidence: Float? = null
                            formFieldMap.forEach { (key, formField) ->
                                if ("Description" == key) {
                                    if (FieldValueType.STRING === formField.value.valueType) {
                                        // Assign Values
                                        description = formField.value.asString()
                                        descriptionConfidence = formField.confidence
                                        // System Logging
                                        System.out.printf(
                                            "%s [%.2f]",
                                            description,
                                            formField.confidence
                                        )
                                    }
                                } else if ("Amount" == key) {
                                    if (FieldValueType.FLOAT === formField.value.valueType) {
                                        // Assign Values
                                        amount = formField.value.asFloat()
                                        amountConfidence = formField.confidence
                                        // System Logging
                                        System.out.printf(
                                            "%.2f € [%.2f] - ",
                                            amount,
                                            formField.confidence
                                        )
                                    }
                                }
                            }
                            // Only Insert Product Into List, If Both Are Not Null
                            if (description != null && amount != null) {
                                productTitles.add(description!!)
                                productPrices.add(amount.toString())
                                if (descriptionConfidence!! < PRODUCT_CONFIDENCE_LIMIT || amountConfidence!! < PRODUCT_CONFIDENCE_LIMIT) {
                                    productConfidences.add(false)
                                } else {
                                    productConfidences.add(true)
                                }
                            }
                            // System Logging
                            System.out.printf("%n - %n")
                        }
                }
            }
            // System Logging
            System.out.printf("%n =============== END =============== %n")

        } catch (e: Exception) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.file", null, locale)
            )
            return "redirect:/dashboard"
        }

        // Convert API Data to BillDto
        val billDto = BillDto(
            store = store,
            date = date,
            purchasePrice = purchasePrice,
            productTitles = productTitles,
            productPrices = productPrices,
        )

        // Store Uploaded Bill
        val storedFile = fileService.store(file, user)

        // Add File ID, Confidence Values and Message Warning to Model
        model["fileId"] = storedFile?.id ?: ""
        model["storeConfidence"] = storeConfidence ?: ""
        // Set Date Confidence to False because of unsupported DE-de locale
        model["dateConfidence"] = false
        model["purchasePriceConfidence"] = purchasePriceConfidence ?: ""
        model["productConfidences"] = productConfidences

        // Set Message
        if (store.isNullOrEmpty() && date.isNullOrEmpty() && purchasePrice == null && productTitles.isEmpty() && productPrices.isEmpty()) {
            model["messageWarning"] = messageSource.getMessage("bill.upload.warningNoResult", null, locale)
        } else {
            model["messageWarning"] = messageSource.getMessage("bill.upload.warningCheck", null, locale)
        }

        // Populate Manage Form
        return populateManageForm(
            household = household,
            billDto = billDto,
            type = 2,
            variant = 2,
            productsLimit = billDto.productTitles?.count { it.isNotEmpty() } ?: DEFAULT_PRODUCTS_LIMIT,
            model = model
        )
    }

    /*
     * Manage
     * manually insert bill details
     */
    @RequestMapping(*["/household/{householdId}/bill/manage"], method = [RequestMethod.GET])
    fun manage(
        @PathVariable("householdId") householdId: UUID,
        model: Model,
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

        // Populate Manage Form
        return populateManageForm(
            household = household,
            type = 2,
            variant = 1,
            model = model
        )
    }

    /*
     * Store
     * save bill to database and storage
     */
    @RequestMapping("/household/{householdId}/bill/manage", method = [RequestMethod.POST])
    fun store(
        @PathVariable("householdId") householdId: UUID,
        @RequestParam("fileId") fileId: UUID?,
        @ModelAttribute @Valid billDto: BillDto,
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
            println("bill binding has errors")

            // Add fileId to model
            model["fileId"] = fileId ?: ""

            // Populate Manage Form
            return populateManageForm(
                household = household,
                billDto = billDto,
                type = 2,
                variant = 1,
                productsLimit = billDto.productTitles?.count { it.isNotEmpty() } ?: DEFAULT_PRODUCTS_LIMIT,
                model = model
            )
        }

        // check if purchasePrice equals sum of productPrices
        if (billDto.productPrices != null) {
            val productPrices = billDto.productPrices!!.toList().filter { !it.isNullOrEmpty() }
            val sum = productPrices.sumOf { it.toDouble() }.toFloat()
            if ((billDto.purchasePrice != sum) && (sum > 0.0)) {
                bindingResult.rejectValue(
                    "purchasePrice",
                    "purchasePriceError",
                    messageSource.getMessage("message.error.purchasePrice", null, locale)
                )

                // Add fileId to model
                model["fileId"] = fileId ?: ""

                // Populate Manage Form
                return populateManageForm(
                    household = household,
                    billDto = billDto,
                    type = 2,
                    variant = 1,
                    productsLimit = billDto.productTitles?.count { it.isNotEmpty() } ?: DEFAULT_PRODUCTS_LIMIT,
                    model = model
                )
            }
        }

        // Create Store, Bill, Products
        val bill = billService.saveStoreBillProduct(user, household, billDto)

        if (bill == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.tryAgain", null, locale)
            )
            return "user/dashboard"
        }

        // Process Bill File and Safety Check
        if (fileId != null) {
            val file = fileService.findById(fileId)
            if (file?.user?.id == user.id && file?.bill?.id == null) {
                if (file != null) {
                    file.bill = bill
                    fileService.save(file)
                    println("file connected with bill")
                }
            }
        }

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.billCreated", null, locale)
        )
        return "redirect:/household/${household.id.toString()}/bill/${bill!!.id.toString()}/show"
    }


    /*
     * Show
     * display a specific bill
     */
    @RequestMapping("/household/{householdId}/bill/{billId}/show", method = [RequestMethod.GET])
    fun show(
        @PathVariable("householdId") householdId: UUID,
        @PathVariable("billId") billId: UUID,
        @RequestParam(required = false) sort: Int?,
        redirectAttributes: RedirectAttributes,
        locale: Locale,
        model: Model
    ): String {
        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        val bill = household?.bills?.find { it.id == billId }

        if (household == null || bill == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        // sort
        bill.products = when (sort) {
            1 -> bill.products?.sortedBy { it.title }
            else -> bill.products?.sortedBy { it.price }
        }

        // models
        model["household"] = household
        model["bill"] = bill

        return "user/bill/show"
    }

    /*
     * Edit
     * show edit form
     */
    @RequestMapping("/household/{householdId}/bill/{billId}/edit", method = [RequestMethod.GET])
    fun edit(
        @PathVariable("householdId") householdId: UUID,
        @PathVariable("billId") billId: UUID,
        model: Model,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        val bill = household?.bills?.find { it.id == billId }

        if (household == null || bill == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        val billDto = billService.convertBillToBillDto(bill)

        // Populate Manage Form
        return populateManageForm(
            household = household,
            bill = bill,
            billDto = billDto,
            type = 2,
            variant = 3,
            productsLimit = billDto.productTitles?.count() ?: DEFAULT_PRODUCTS_LIMIT,
            model = model
        )
    }

    /*
     * Update
     * save updated bill
     */
    @RequestMapping("/household/{householdId}/bill/{billId}/edit", method = [RequestMethod.POST])
    fun update(
        @PathVariable("householdId") householdId: UUID,
        @PathVariable("billId") billId: UUID,
        @ModelAttribute @Valid billDto: BillDto,
        bindingResult: BindingResult,
        model: Model,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        val bill = household?.bills?.find { it.id == billId }

        if (household == null || bill == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        // check validation errors
        if (bindingResult.hasErrors()) {
            println("bill binding has errors")

            // Populate Manage Form
            return populateManageForm(
                household = household,
                bill = bill,
                billDto = billDto,
                type = 2,
                variant = 3,
                productsLimit = billDto.productTitles?.count { it.isNotEmpty() } ?: DEFAULT_PRODUCTS_LIMIT,
                model = model
            )
        }

        // check if purchasePrice equals sum of productPrices
        if (billDto.productPrices != null) {
            val productPrices = billDto.productPrices!!.toList().filter { !it.isNullOrEmpty() }
            val sum = productPrices.sumOf { it.toDouble() }.toFloat()
            if ((billDto.purchasePrice != sum) && (sum > 0.0)) {

                bindingResult.rejectValue(
                    "purchasePrice",
                    "purchasePriceError",
                    messageSource.getMessage("message.error.purchasePrice", null, locale)
                )

                // Populate Manage Form
                return populateManageForm(
                    household = household,
                    bill = bill,
                    billDto = billDto,
                    type = 2,
                    variant = 3,
                    productsLimit = billDto.productTitles?.count { it.isNotEmpty() } ?: DEFAULT_PRODUCTS_LIMIT,
                    model = model
                )
            }
        }

        // Update Bill
        billService.updateStoreBillProduct(user, household, billDto, bill)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )
        return "redirect:/household/${household.id.toString()}/bill/${bill!!.id.toString()}/show"
    }

    /*
     * Delete
     * remove resource from database
     */
    @RequestMapping("/household/{householdId}/bill/{billId}/delete", method = [RequestMethod.GET])
    fun delete(
        @PathVariable("householdId") householdId: UUID,
        @PathVariable("billId") billId: UUID,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        // Check permission
        val user = userService.getCurrentUser()!!
        val household = householdService.findByIdAndUser(householdId, user)
        val bill = household?.bills?.find { it.id == billId }

        if (household == null || bill == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        // Delete Bill
        billService.delete(bill)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )
        return "redirect:/household/${household.id.toString()}/show"
    }

}