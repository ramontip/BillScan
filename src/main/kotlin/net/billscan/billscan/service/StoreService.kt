package net.billscan.billscan.service

import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.Store
import net.billscan.billscan.repository.StoreRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class StoreService(
    val storeRepository: StoreRepository,
) {

    @Transactional
    fun save(store: Store): Store {
        storeRepository.save(store)
        return store
    }

    fun findByTitle(title: String): Store? {
        return storeRepository.findStoreByTitle(title)
    }

    fun getOrCreate(title: String): Store {
        return findByTitle(title) ?: save(
            Store(
                title = title,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )
    }
}