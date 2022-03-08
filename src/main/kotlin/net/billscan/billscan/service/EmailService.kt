package net.billscan.billscan.service

import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.mail.javamail.MimeMessageHelper
import javax.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*
import org.springframework.util.ResourceUtils

@Service
class EmailService (val messageSource: MessageSource) {

    // Create JavaMailSender
    fun getJavaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = "ramontippl.com"
        mailSender.port = 465
        mailSender.username = "billscan@ramontippl.com"
        mailSender.password = "25}Z[w=KHE6ejb"

        val mailProperties: Properties = mailSender.javaMailProperties
        mailProperties["mail.transport.protocol"] = "smtp"
        mailProperties["mail.smtp.auth"] = "true"
        mailProperties["mail.smtp.starttls.enable"] = "true"
        mailProperties["mail.smtp.starttls.required"] = "true"
        mailProperties["mail.smtp.ssl.enable"] = "true"
        /*
         * Debugging
         *
        mailProperties["mail.debug"] = "true"
        mailProperties["mail.test-connection"] = "true"
        */

        return mailSender
    }


    fun sendPasswordResetMail(username: String, resetUrl: String, locale: Locale) {

        try {
            val mailSender = getJavaMailSender()

            val subject =  messageSource.getMessage("email.passwordReset.subject", null, locale)
            val text = messageSource.getMessage("email.passwordReset.text", null, locale)
            val actionButtonText =  messageSource.getMessage("email.passwordReset.action", null, locale)
            val htmlMail =
                ResourceUtils.getFile("classpath:templates/password-reset-mail.html")
                    .readText()
                    .replace("placeholderSubject", subject)
                    .replace("placeholderText", text)
                    .replace("placeholderActionButtonText", actionButtonText)
                    .replace("placeholderResetUrl", resetUrl)

            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, "UTF-8")
            helper.setTo(username)
            helper.setFrom("BillScan <billscan@ramontippl.com>");
            helper.setSubject(subject)
            helper.setText(htmlMail, true)
            mailSender.send(message)
        } catch (me: MailException) {

        }
    }
}