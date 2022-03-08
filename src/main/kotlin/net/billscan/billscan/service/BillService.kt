package net.billscan.billscan.service

import net.billscan.billscan.dto.BillDto
import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Household
import net.billscan.billscan.entity.User
import net.billscan.billscan.repository.BillRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.transaction.Transactional

@Service
class BillService(
    val billRepository: BillRepository,
    val userService: UserService,
    val storeService: StoreService,
    val productService: ProductService,
) {
    @Transactional
    fun saveStoreBillProduct(user: User, household: Household, billDto: BillDto): Bill? {

        // create store if not exists
        val store = storeService.getOrCreate(billDto.store!!)

        // create bill
        val bill = save(
            Bill(
                household = household,
                user = user,
                store = store,
                purchaseDate = LocalDate.parse(billDto.date, DateTimeFormatter.ISO_DATE),
                purchasePrice = billDto.purchasePrice,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )

        // save products
        val productTitles = billDto.productTitles?.toList()?.filter { !it.isNullOrEmpty() }
        val productPrices = billDto.productPrices?.toList()?.filter { !it.isNullOrEmpty() }
        productService.saveProductList(bill, productTitles, productPrices)

        return bill
    }

    @Transactional
    fun updateStoreBillProduct(user: User, household: Household, billDto: BillDto, bill: Bill): Bill? {

        // create store if not exists
        val store = storeService.getOrCreate(billDto.store!!)

        // update bill
        bill.household = household
        bill.user = user
        bill.store = store
        bill.purchaseDate = LocalDate.parse(billDto.date, DateTimeFormatter.ISO_DATE)
        bill.purchasePrice = billDto.purchasePrice
        bill.updatedAt = LocalDateTime.now()
        save(bill)

        // delete old products
        productService.deleteByBill(bill)

        // save products
        val productTitles = billDto.productTitles?.toList()?.filter { !it.isNullOrEmpty() }
        val productPrices = billDto.productPrices?.toList()?.filter { !it.isNullOrEmpty() }
        productService.saveProductList(bill, productTitles, productPrices)

        return bill
    }

    @Transactional
    fun save(bill: Bill): Bill {
        billRepository.save(bill)
        return bill
    }

    fun convertBillToBillDto(bill: Bill): BillDto {
        return BillDto(
            store = bill.store!!.title,
            date = bill.purchaseDate.toString(),
            purchasePrice = bill.purchasePrice,
            productTitles = bill.products?.map { it.title!! },
            productPrices = bill.products?.map { it.price!!.toString() },
        )
    }

    fun findById(id: UUID): Bill? {
        return billRepository.findBillById(id)
    }

    fun findPurchasePeriodsByUser(user: User): Set<LocalDate>? {
        // Get all bill purchase dates
        val purchaseDates = billRepository.findPurchaseDatesByUser(user)

        // make set of first of month with purchases
        return purchaseDates?.map { it.withDayOfMonth(1) }?.toSet()
    }

    fun findPurchasePeriodsByHousehold(household: Household): Set<LocalDate>? {
        // Get all bill purchase dates
        val purchaseDates = billRepository.findPurchasePeriodsByHousehold(household)

        // make set of first of month with purchases
        return purchaseDates?.map { it.withDayOfMonth(1) }?.toSet()
    }

    fun findByUserAndRange(user: User, dateTo: LocalDate, dateFrom: LocalDate): List<Bill>? {
        return billRepository.findByUserAndRange(user, dateTo, dateFrom)
    }

    fun findByHouseholdAndRange(household: Household, dateTo: LocalDate, dateFrom: LocalDate): List<Bill>? {
        return billRepository.findByHouseholdAndRange(household, dateTo, dateFrom)
    }

    fun findByUserAndStoreTitle(user: User, term: String): List<Bill>? {
        return billRepository.findByUserAndStoreTitle(user, term)
    }

    @Transactional
    fun delete(bill: Bill) {
        billRepository.deleteBillById(bill.id!!)
        return
    }
}