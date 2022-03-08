package net.billscan.billscan.service

import net.billscan.billscan.dto.HouseholdDto
import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.User
import net.billscan.billscan.repository.HouseholdRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class HouseholdService(
    val householdRepository: HouseholdRepository,
    val userService: UserService,
) {

    fun geRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun convertHouseholdDtoToHousehold(householdDto: HouseholdDto): Household {

        // generate unique invitationCode
        var invitationCode: String
        do {
            invitationCode = geRandomString(6)
        } while (findByInvitationCode(invitationCode) != null)

        return Household(
            createdByUser = userService.getCurrentUser(),
            title = householdDto.title,
            invitationCode = invitationCode,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }

    fun convertHouseholdToHouseholdDto(household: Household): HouseholdDto {
        return HouseholdDto(
            title = household.title,
        )
    }

    @Transactional
    fun save(household: Household): Household {
        householdRepository.save(household)
        return household
    }

    @Transactional
    fun update(household: Household, offeringDto: HouseholdDto): Household {
        household.title = offeringDto.title
        household.updatedAt = LocalDateTime.now()
        householdRepository.save(household)
        return household
    }

    fun findById(id: UUID): Household? {
        return householdRepository.findHouseholdById(id)
    }

    fun findByIdAndUser(id: UUID, user: User): Household? {
        return householdRepository.findHouseholdByIdAndUser(id, user)
    }

    fun findByInvitationCode(invitationCode: String): Household? {
        return householdRepository.findByInvitationCode(invitationCode)
    }

    @Transactional
    fun delete(household: Household) {
        householdRepository.deleteHouseholdById(household.id!!)
        return
    }
}