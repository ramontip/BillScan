package net.billscan.billscan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
class BillscanApplication {
	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	protected fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder? {
		return application.sources(BillscanApplication::class.java)
	}

}

fun main(args: Array<String>) {
	runApplication<BillscanApplication>(*args)
}
