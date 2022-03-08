package net.billscan.billscan.controller.user

import net.billscan.billscan.service.BillService
import net.billscan.billscan.service.ProductService
import net.billscan.billscan.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SearchController(
    val userService: UserService,
    val billService: BillService,
    val productService: ProductService
) {
    /*
     * Search
     * display search results
     */
    @RequestMapping("/search", method = [RequestMethod.GET])
    fun search(
        @RequestParam("term") term: String?,
        model: Model,
    ): String {
        val user = userService.getCurrentUser()!!

        if (term == null || term == "") {
            return "redirect:dashboard"
        }

        // search for products
        val products = productService.findByUserAndTitle(user, term)

        // search for stores
        val bills = billService.findByUserAndStoreTitle(user, term)

        model["activeSearch"] = term
        model["products"] = products ?: ""
        model["bills"] = bills ?: ""

        return "user/search"
    }

}