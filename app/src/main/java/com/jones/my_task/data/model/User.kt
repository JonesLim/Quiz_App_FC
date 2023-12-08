package com.jones.my_task.data.model

data class User(
    val id: String? = null,
    val name: String,
    val email: String
) {
    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            "name" to name,
            "email" to email
        )
    }

    companion object {
        fun fromHashMap(hash: Map<String, Any>): User {
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
            )
        }

    }
}