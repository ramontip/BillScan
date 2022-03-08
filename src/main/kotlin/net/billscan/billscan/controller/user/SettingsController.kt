package net.billscan.billscan.controller.user

import net.billscan.billscan.dto.PasswordChangeDto
import net.billscan.billscan.service.ThumbnailService
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
class SettingsController(
    val userService: UserService,
    val thumbnailService: ThumbnailService,
    val messageSource: MessageSource
) {

    /*
     * Populate Settings Form
     * pass dto to view
     */
    fun populateSettingsForm(
        model: Model,
        passwordChangeDto: PasswordChangeDto? = null,
    ): String {
        model["passwordChangeDto"] = passwordChangeDto ?: PasswordChangeDto()
        return "user/settings"
    }

    /*
     * Settings
     * display settings
     */
    @RequestMapping("/settings", method = [RequestMethod.GET])
    fun settings(model: Model): String {
        return populateSettingsForm(model)
    }

    /*
     * Update Thumbnail
     * save updated thumbnail
     */
    @RequestMapping("/settings/update/thumbnail", method = [RequestMethod.POST])
    fun updateThumbnail(
        model: Model,
        @RequestParam("thumbnail") thumbnail: MultipartFile,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        val user = userService.getCurrentUser()!!

        // Check If File is Image
        if (thumbnail.contentType?.startsWith("image/") != true) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notImage", null, locale)
            )
            return "redirect:/dashboard"
        }

        // Update Thumbnail and Check for Errors
        if (thumbnailService.store(thumbnail, user) == null) {
            model["messageError"] = messageSource.getMessage("message.error.thumbnail", null, locale)
            populateSettingsForm(model)
        }

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )
        return "redirect:/settings"
    }

    /*
     * Update Password
     * save updated password
     */
    @RequestMapping("/settings/update/password", method = [RequestMethod.POST])
    fun updatePassword(
        @ModelAttribute @Valid passwordChangeDto: PasswordChangeDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        if (bindingResult.hasErrors()) {
            return populateSettingsForm(model = model, passwordChangeDto = passwordChangeDto)
        }

        val user = userService.getCurrentUser()!!
        val inputPassword = passwordChangeDto.currentPassword
        val newPassword = passwordChangeDto.newPassword
        val confirmPassword = passwordChangeDto.confirmPassword

        // Check if Current Password is Correct
        if (!BCryptPasswordEncoder().matches(inputPassword, user.password)) {
            bindingResult.rejectValue(
                "currentPassword",
                "passwordInvalid",
                messageSource.getMessage("message.error.passwordInvalid", null, locale)
            )
            return populateSettingsForm(model = model, passwordChangeDto = passwordChangeDto)
        }

        // Check if Passwords Match
        if (newPassword != confirmPassword) {
            bindingResult.rejectValue(
                "newPassword",
                "passwordsDoNotMatch",
                messageSource.getMessage("message.error.passwordMatching", null, locale)
            )
            return populateSettingsForm(model = model, passwordChangeDto = passwordChangeDto)
        }

        // Check Password Complexity
        if (!userService.passwordIsComplex(newPassword!!)) {
            bindingResult.rejectValue(
                "newPassword",
                "passwordNotComplex",
                messageSource.getMessage("message.error.passwordComplexity", null, locale)
            );
            return populateSettingsForm(model = model, passwordChangeDto = passwordChangeDto)
        }

        // Update Password
        user.password = BCryptPasswordEncoder().encode(newPassword)
        userService.save(user)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )
        return "redirect:/settings"
    }

    /*
     * Delete
     * remove resource from database
     */
    @RequestMapping("/settings/deleteProfile", method = [RequestMethod.GET])
    fun delete(
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        // Check permission
        val user = userService.getCurrentUser()!!

        // Delete user and thumbnail
        userService.delete(user)
        if (user.thumbnail != null) {
            thumbnailService.delete(user.thumbnail!!)
        }

        // Delete Session
        httpServletRequest.session.invalidate();

        // Return
        return "redirect:/login?deleted=true"
    }
}