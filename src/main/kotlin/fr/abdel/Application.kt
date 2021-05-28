package fr.abdel

import com.apurebase.kgraphql.GraphQL
import fr.abdel.di.mainModule
import fr.abdel.graphql.authSchema
import fr.abdel.graphql.dessertSchema
import fr.abdel.graphql.profileSchema
import fr.abdel.graphql.reviewSchema
import fr.abdel.serices.AuthService
import fr.abdel.serices.DessertService
import fr.abdel.serices.ProfileService
import fr.abdel.serices.ReviewService
import io.ktor.application.*
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    startKoin {
        modules(mainModule)
    }

    install(GraphQL) {

        val dessertService = DessertService()
        val reviewService = ReviewService()
        val authService = AuthService()
        val profileService = ProfileService()
        playground = true

        context {call ->
            authService.verifyToken(call)?.let { +it }
            +log
            +call
        }

        schema {
            dessertSchema(dessertService)
            reviewSchema(reviewService)
            authSchema(authService)
            profileSchema(profileService)
        }
    }

}
