package net.billscan.billscan.service

import net.billscan.billscan.entity.Bill
import net.billscan.billscan.entity.Product
import net.billscan.billscan.entity.User
import net.billscan.billscan.repository.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class ProductService(
    val productRepository: ProductRepository,
) {

    @Transactional
    fun save(product: Product): Product {
        productRepository.save(product)
        return product
    }

    @Transactional
    fun deleteByBill(bill: Bill) {
        productRepository.deleteByBill(bill)
    }

    @Transactional
    fun saveProductList(bill: Bill, productTitles: List<String>?, productPrices: List<String>?) {
        if (!productTitles.isNullOrEmpty() && !productPrices.isNullOrEmpty()) {
            // create map
            val products: List<Pair<String, Float>> =
                productTitles.zip(productPrices.map { it.toFloat() })
                    .toList()

            // iterate and save
            for ((title, price) in products) {
                save(
                    Product(
                        bill = bill,
                        title = title,
                        price = price,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now(),
                    )
                )
            }
        }
    }

    fun findByUserAndTitle(user: User, term: String): List<Product>? {
        return productRepository.findByUserAndTitle(user, term)
    }
}