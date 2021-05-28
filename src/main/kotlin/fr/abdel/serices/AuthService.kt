package fr.abdel.serices

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.MongoClient
import fr.abdel.models.User
import fr.abdel.models.UserInput
import fr.abdel.models.UserResponse
import fr.abdel.repository.UserRepository
import io.ktor.application.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.nio.charset.StandardCharsets
import java.util.*

class AuthService: KoinComponent {

    private val client: MongoClient by inject()
    private val repo: UserRepository = UserRepository(client)

    private val secret: String = "secret"
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)
    private val verifier: JWTVerifier = JWT.require(algorithm).build()

    private fun signAccessToken(id: String): String {
        return JWT.create()
                .withIssuer("example")
                .withClaim("userId", id)
                .sign(algorithm)

    }

    fun signIn(userInput: UserInput): UserResponse? {
        val user = repo.getUserByEmail(userInput.email) ?: error("No such user by that email")
        if (BCrypt.verifyer().verify(
                        userInput.password.toByteArray(Charsets.UTF_8),
                        user.hashPass
                ).verified) {
            val token = signAccessToken(user.id)
            return UserResponse(token, user)
        }
        error("password is incorrect")
    }

    fun signUp(userInput: UserInput): UserResponse? {
        val hashedPassword = BCrypt.withDefaults().hash(10,userInput.password.toByteArray(StandardCharsets.UTF_8))
        val id = UUID.randomUUID().toString()
        val emailUser = repo.getUserByEmail(userInput.email)
        if(emailUser != null) {
            error("Email already in use")
        }
        val newUser = repo.add(
                User(
                   id = id,
                   email = userInput.email,
                   hashPass = hashedPassword
                )
        )
        val token = signAccessToken(newUser.id)
        return UserResponse(token, newUser)

    }

    fun verifyToken(call: ApplicationCall): User? {
        return try {
            val authHeader = call.request.headers["Authorization"] ?: ""
            val token = authHeader.split("Bearer ").last()
            val accessToken = verifier.verify(JWT.decode(token))
            val userid = accessToken.getClaim("userId").asString()
            return User(id = userid, email = "", hashPass = ByteArray(0))
        }catch (e: Exception) {
            println(e.message)
            null
        }
    }


}