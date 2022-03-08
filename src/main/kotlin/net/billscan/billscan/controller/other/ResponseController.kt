package net.billscan.billscan.controller.other

import net.billscan.billscan.service.HouseholdService
import net.billscan.billscan.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*


@Controller
class ResponseController(val householdService: HouseholdService, val userService: UserService) {

    @RequestMapping("/user/thumbnail/{id}", method = [RequestMethod.GET])
    fun getUserThumbnail(@PathVariable("id") id: UUID): ResponseEntity<Any> {
        val user = userService.findById(id)
        if (user?.thumbnail != null) {
            val file = user.thumbnail!!
            // return file
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.name)
                .contentType(MediaType.parseMediaType(file.type!!))
                .body(file.data)
        }
        /*
        if (user != null) {
            val path = Paths.get("storage/${user.thumbnail}").toAbsolutePath()
            val fileSystemResource = FileSystemResource(path)
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + user.thumbnail)
                .contentType(MediaType.parseMediaType(user.thumbnailContentType!!))
                .body(fileSystemResource)
        }
        */
        return ResponseEntity.notFound().build()
    }

    @RequestMapping("/household/{householdId}/bill/{billId}/show/file", method = [RequestMethod.GET])
    fun showBillFile(
        @PathVariable("householdId") householdId: UUID,
        @PathVariable("billId") billId: UUID,
    ): ResponseEntity<Any> {
        val user = userService.getCurrentUser()!!

        // Get Household and Check Permission
        val household = householdService.findByIdAndUser(householdId, user)
        val bill = household?.bills?.find { it.id == billId }

        if (household != null && bill != null) {
            if (!bill.files.isNullOrEmpty()) {
                val file = bill.files!!.first()
                // return file
                return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.name)
                    .contentType(MediaType.parseMediaType(file.type!!))
                    .body(file.data)
            }
        }
        return ResponseEntity.notFound().build()
    }

    @RequestMapping("/files/{fileId}/download", method = [RequestMethod.GET])
    fun downloadFile(
        @PathVariable("fileId") fileId: UUID,
    ): ResponseEntity<Any> {
        val user = userService.getCurrentUser()!!

        val file = user.files?.find { it.id == fileId }

        if (file != null) {
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.name)
                .contentType(MediaType.parseMediaType(file.type!!))
                .body(file.data)
        }
        return ResponseEntity.notFound().build()
    }
}
