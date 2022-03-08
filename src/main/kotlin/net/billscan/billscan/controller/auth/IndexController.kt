package net.billscan.billscan.controller.auth

import net.billscan.billscan.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class IndexController(val userService: UserService) {

    @RequestMapping("/", method = [RequestMethod.GET])
    fun index(): String {
        if (userService.getCurrentUser() != null) {
            return "redirect:dashboard"
        }
        return "auth/index"
}
}