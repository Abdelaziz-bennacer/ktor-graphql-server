package fr.abdel.graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import fr.abdel.models.User
import fr.abdel.models.UserInput
import fr.abdel.serices.AuthService

fun SchemaBuilder.authSchema(authService: AuthService) {

    mutation("signIn") {
        description = "Authenticate an existing user"
        resolver { userInput: UserInput ->
            try {
                authService.signIn(userInput)
            }catch (e: Exception) {
                null
            }

        }
    }

    mutation("signUp") {
        description = "Authenticate a new user"
        resolver { userInput: UserInput ->
            try {
                authService.signUp(userInput)
            }catch (e: Exception) {
                null
            }
        }
    }

    type<User> {
        User::hashPass.ignore()
    }
}