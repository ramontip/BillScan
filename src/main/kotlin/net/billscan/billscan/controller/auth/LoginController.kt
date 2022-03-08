package net.billscan.billscan.controller.auth

import net.billscan.billscan.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class LoginController(val userService: UserService) {

    @RequestMapping("/login", method = [RequestMethod.GET])
    fun login(model: Model): String {
        
        if (userService.getCurrentUser() != null) {
            return "redirect:dashboard"
        }

        return "auth/login"
    }
}