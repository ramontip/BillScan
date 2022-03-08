package net.billscan.billscan.service

import net.billscan.billscan.dto.UserDto
import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.PasswordResetToken
import net.billscan.billscan.entity.User
import net.billscan.billscan.repository.PasswordResetTokenRepository
import net.billscan.billscan.repository.UserRepository
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional


@Service
class UserService(
    val userRepository: UserRepository,
    val passwordResetTokenRepository: PasswordResetTokenRepository
) {

    fun getCurrentUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication == null || AnonymousAuthenticationToken::class.java.isAssignableFrom(authentication.javaClass)) {
            null
        } else {
            findByUsername(authentication.name)
        }
    }

    fun convertUserDtoToUser(userDto: UserDto): User {
        return User(
            firstname = userDto.firstname,
            lastname = userDto.lastname,
            username = userDto.username,
            password = userDto.password,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }

    @Transactional
    fun save(user: User) {
        user.updatedAt = LocalDateTime.now()
        userRepository.save(user)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun findById(id: UUID): User? {
        return userRepository.findByUserId(id)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun passwordIsComplex(password: String): Boolean {
        val regex = "(^.*(?=.*[A-Z])(?=.*[0-9])(?=.*[!@.#$%^&*_]).*$)".toRegex()
        return regex.containsMatchIn(password)
    }

    fun createPasswordResetToken(user: User): String {
        val passwordToken = UUID.randomUUID().toString()
        val passwordResetToken = PasswordResetToken(token = passwordToken, user = user)
        passwordResetTokenRepository.save(passwordResetToken)
        return passwordToken
    }

    fun passwordResetTokenIsValid(token: String): Boolean {
        val passwordResetToken = passwordResetTokenRepository.findEntryByToken(token)
        if (passwordResetToken != null) {
            if (passwordResetToken.expiryDate > LocalDateTime.now()) {
                return true
            }
        }
        return false
    }

    fun findUserFromToken(token: String): User? {
        val passwordResetToken = passwordResetTokenRepository.findEntryByToken(token)
        return passwordResetToken?.user
    }

    fun deleteUserPasswordResetTokens(user: User) {
        passwordResetTokenRepository.deleteByUser(user)
    }

    fun updatePassword(user: User?, password: String): Boolean {
        return if (user == null) {
            false
        } else {
            user.password = BCryptPasswordEncoder().encode(password)
            userRepository.save(user)
            true
        }
    }

    @Transactional
    fun addHousehold(user: User, household: Household?) {
        if (user.households != null) {
            // merge old list with new
            user.households = user.households!! + setOf(household!!)
        } else {
            user.households = setOf(household!!)
        }
        save(user)
        return
    }

    @Transactional
    fun removeHousehold(user: User, household: Household?) {
        if (user.households != null) {
            // merge old list with new
            user.households = user.households!! - setOf(household!!)
        }
        save(user)
        return
    }

    @Transactional
    fun delete(user: User) {
        userRepository.deleteUserById(user.id!!)
        return
    }
}