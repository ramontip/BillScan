package net.billscan.billscan.controller.user

import net.billscan.billscan.service.FileService
import net.billscan.billscan.service.UserService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class FilesController(
    val userService: UserService,
    val fileService: FileService,
    val messageSource: MessageSource,
) {
    /*
     * Files
     * display files page
     */
    @RequestMapping(*["/files"], method = [RequestMethod.GET])
    fun expenses(
        model: Model,
        locale: Locale
    ): String {
        val user = userService.getCurrentUser()!!

        model["files"] = user.files?.sortedByDescending { it.createdAt } ?: ""

        return "user/files"
    }

    /*
     * Delete
     * remove bill from database and storage
     */
    @RequestMapping("/files/{fileId}/delete", method = [RequestMethod.GET])
    fun delete(
        @PathVariable("fileId") fileId: UUID,
        httpServletRequest: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
        locale: Locale
    ): String {
        val user = userService.getCurrentUser()!!

        val file = user.files?.find { it.id == fileId }

        if (file == null) {
            // Return with Error Toast
            redirectAttributes.addFlashAttribute(
                "toastError",
                messageSource.getMessage("message.error.notAvailable", null, locale)
            )
            return "redirect:/dashboard"
        }

        // Delete Bill
        fileService.delete(file)

        // Return with Success Toast
        redirectAttributes.addFlashAttribute(
            "toastSuccess",
            messageSource.getMessage("message.success.changesSaved", null, locale)
        )
        return "redirect:/files"
    }
}