package net.billscan.billscan.advice

import net.billscan.billscan.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute


@ControllerAdvice
class CurrentUserControllerAdvice(val userRepository: UserRepository) {

    @ModelAttribute
    fun addCurrentUser(model: Model) {

        val auth = SecurityContextHolder.getContext().authentication
        val username = auth.name

        val currentUser = userRepository.findByUsername(username)
        model.addAttribute("currentUser", currentUser)
    }
}
