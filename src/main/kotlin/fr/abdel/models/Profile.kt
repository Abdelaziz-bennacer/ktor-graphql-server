package fr.abdel.models

import fr.abdel.models.Dessert
import fr.abdel.models.User

data class Profile(val user: User, val desserts: List<Dessert> = emptyList())