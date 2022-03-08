package net.billscan.billscan.controller.auth

import net.billscan.billscan.dto.UserDto
import net.billscan.billscan.service.UserService
import org.springframework.context.MessageSource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import java.util.regex.Pattern
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@Controller
class RegistrationController(
    val userService: UserService,
    val messageSource: MessageSource
) {

    fun populateRegistrationForm(model: Model, userDto: UserDto? = null): String {
        model["userDto"] = userDto ?: UserDto(username = "", password = "")
        return "auth/registration"
    }

    @RequestMapping("/registration", method = [RequestMethod.GET])
    fun showRegistrationForm(model: Model): String {
        if (userService.getCurrentUser() != null) {
            return "redirect:dashboard"
        }
        return populateRegistrationForm(model)
    }

    @RequestMapping("/registration", method = [RequestMethod.POST])
    fun registerUser(
        @ModelAttribute @Valid userDto: UserDto,
        bindingResult: BindingResult,
        model: Model,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        // check validation errors
        if (bindingResult.hasErrors()) {
            return populateRegistrationForm(model, userDto)
        }

        // check username
        if (userService.findByUsername(userDto.username) != null) {
            bindingResult.rejectValue("username", "usernameAlreadyInUse", "Diese E-Mail wird bereits verwendet.");
            return populateRegistrationForm(model, userDto)
        }

        // check if passwords match
        if (userDto.password != userDto.confirmPassword) {
            bindingResult.rejectValue("password", "passwordsDoNotMatch", "Passwörter stimmen nicht überein.");
            return populateRegistrationForm(model, userDto)
        }

        // Check password complexity
        if (!userService.passwordIsComplex(userDto.password)) {
            bindingResult.rejectValue(
                "password",
                "passwordNotComplex",
                "Passwort ist nicht komplex genug. verwende Großbuchstaben, Zahlen, Sonderzeichen und mind. 6 Zeichen."
            );
            return populateRegistrationForm(model, userDto)
        }

        // save user
        try {
            val nonhashedPassword = userDto.password
            userDto.password = BCryptPasswordEncoder().encode(userDto.password)
            userService.save(userService.convertUserDtoToUser(userDto))
            httpServletRequest.login(userDto.username, nonhashedPassword)
        } catch (e: ServletException) {
            model["messageError"] = "Fehler. Bitte versuche es erneut."
            return populateRegistrationForm(model)
        }

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.welcome", null, locale)
        )

        return "redirect:dashboard"
    }
}