package fr.abdel.graphql

import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import fr.abdel.models.User
import fr.abdel.serices.ProfileService

fun SchemaBuilder.profileSchema(profileService: ProfileService) {

    query("getProfile") {
        description = ""
        resolver { ctx: Context ->
          try {
              val userId = ctx.get<User>()?.id ?: error("Not signed in")
              profileService.getProfile(userId)
          }catch (e: Exception) {
              null
          }
        }
    }
}