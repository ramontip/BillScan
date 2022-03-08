package net.billscan.billscan.controller.other

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest


@Controller
class MyErrorController : ErrorController {

    @RequestMapping("/error", method = [RequestMethod.GET])
    fun handleError(httpServletRequest: HttpServletRequest, model: Model): String {
        val status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            model["statusCode"] = Integer.valueOf(status.toString())
        }
        return "other/error"
    }

}