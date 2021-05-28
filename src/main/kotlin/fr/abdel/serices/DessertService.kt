package fr.abdel.serices

import com.mongodb.client.MongoClient
import fr.abdel.models.Dessert
import fr.abdel.models.DessertInput
import fr.abdel.models.DessertsPage
import fr.abdel.repository.DessertRepository
import fr.abdel.repository.ReviewRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class DessertService: KoinComponent {

    private val client: MongoClient by inject()
    private val repo: DessertRepository = DessertRepository(client)
    private val reviewRepo: ReviewRepository = ReviewRepository(client)

    fun getDessertsPage(page: Int, size: Int): DessertsPage {
        return repo.getDessertsPage(page, size)
    }

    fun getDessert(id: String): Dessert {
        val dessert = repo.getById(id)
        dessert.reviews = reviewRepo.getReviewByDessertId(id)
        return dessert
    }

    fun createDessert(dessertInput: DessertInput, userId: String): Dessert {
        val uid = UUID.randomUUID().toString()
        val dessert = Dessert(
                id = uid,
                userId = userId,
                name = dessertInput.name,
                description = dessertInput.description,
                imageUrl = dessertInput.imageUrl
        )
        return repo.add(dessert)
    }

    fun updateDessert(userId: String, dessertId: String, dessertInput: DessertInput): Dessert {
        val dessert = repo.getById(dessertId)
        if(dessert.userId == userId) {
            val updates = Dessert(
                    id = dessertId,
                    userId = userId,
                    name = dessertInput.name,
                    description = dessertInput.description,
                    imageUrl = dessertInput.imageUrl
            )
            return repo.update(updates)
        }
        error("Cannot update dessert")
    }

    fun deleteDessert(userId: String, dessertId: String): Boolean {
        val dessert = repo.getById(dessertId)
        if(dessert.userId == userId){
            return repo.delete(dessertId)
        }
        error("Cannot delete dessert")
    }
}