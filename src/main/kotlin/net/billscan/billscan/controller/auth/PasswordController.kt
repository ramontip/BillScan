package net.billscan.billscan.controller.auth

import net.billscan.billscan.service.EmailService
import net.billscan.billscan.service.UserService
import org.springframework.context.MessageSource
import org.springframework.mail.MailSender
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest


@Controller
class PasswordController(
    val userService: UserService,
    val emailService: EmailService,
    val mailSender: MailSender,
    val messageSource: MessageSource
) {

    @RequestMapping("/forgot-password", method = [RequestMethod.GET])
    fun showForgotPasswordForm(): String {
        return "auth/forgot-password"
    }

    @RequestMapping("/forgot-password", method = [RequestMethod.POST])
    fun sendForgotPasswordMail(
        httpServletRequest: HttpServletRequest,
        @RequestParam username: String,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {

        val user = userService.findByUsername(username)

        // only send mail if user exists
        if (user != null) {
            // Create and store token
            val token = userService.createPasswordResetToken(user)
            val resetUrl =
                "http://" + httpServletRequest.getHeader("host").toString() + "/" + "reset-password?token=" + token
            emailService.sendPasswordResetMail(username, resetUrl, locale)
        }

        redirectAttributes.addFlashAttribute("forgotPasswordFormSubmitted", true)
        return "redirect:forgot-password"
    }

    @RequestMapping("/reset-password", method = [RequestMethod.GET])
    fun showResetPasswordForm(model: Model, @RequestParam token: String?): String {
        if (token != null) {
            if (userService.passwordResetTokenIsValid(token)) {
                model["validPasswordReset"] = true
                model["token"] = token
            } else {
                model["validPasswordReset"] = false
            }
        }
        return "auth/reset-password"
    }

    @RequestMapping("/reset-password", method = [RequestMethod.POST])
    fun resetPassword(
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        @RequestParam token: String,
        @RequestParam password: String,
        @RequestParam confirmPassword: String,
        locale: Locale,
        model: Model
    ): String {

        if (!userService.passwordResetTokenIsValid(token)) {
            model["token"] = token
            model["validPasswordReset"] = false
            return "auth/reset-password"
        }

        // check if passwords match
        if (password != confirmPassword) {
            model["token"] = token
            model["messageError"] = "Passwörter stimmen nicht überein."
            return "auth/reset-password"
        }

        // Check password complexity
        if (!userService.passwordIsComplex(password) || password.length < 6) {
            model["token"] = token
            model["messageError"] =
                "Passwort ist nicht komplex genug. verwende Großbuchstaben, Zahlen, Sonderzeichen und mind. 6 Zeichen."
            return "auth/reset-password"
        }

        val user = userService.findUserFromToken(token)
        if (userService.updatePassword(user, password)) {
            if (user != null) {
                userService.deleteUserPasswordResetTokens(user)
            }
            return try {
                httpServletRequest.login(user!!.username, password)
                // Return with Success Toast
                redirectAttributes.addFlashAttribute(
                    "toastSuccess",
                    messageSource.getMessage("message.success.changesSaved", null, locale)
                )
                "redirect:dashboard"
            } catch (e: ServletException) {
                println("directly login user failed")
                "redirect:login"
            }
        }

        model["messageError"] = "Fehler. Bitte versuche es erneut."
        return "auth/reset-password"
    }
}