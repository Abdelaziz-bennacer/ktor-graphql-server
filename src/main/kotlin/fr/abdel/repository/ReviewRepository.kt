package fr.abdel.repository

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import fr.abdel.models.Review
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

class ReviewRepository(client: MongoClient): RepositoryInterface<Review> {

    override lateinit var col: MongoCollection<Review>

    init {
        val database = client.getDatabase("test")
        col = database.getCollection<Review>("Review")
    }

    fun getReviewByDessertId(dessertId: String): List<Review> {

        return try {
            val res = col.find(Review::dessertId eq dessertId) ?: throw Exception("No review with that ID exists")
            res.asIterable().map { it }
        }catch (t: Throwable){
            throw Exception("Cannot find reviews")
        }
    }
}