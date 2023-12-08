package com.jones.my_task.data.model


data class Quiz(
    val id: String? = null,
    val title: String,
    val questions: List<String>,
    val createdBy: String? = null,
    val options: List<String>,
    val second: Long,
    var correctOption: List<Int>
) {
    fun toHashMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "questions" to questions,
            "createdBy" to createdBy.toString(),
            "options" to options,
            "second" to second,
            "correctOption" to correctOption
        )
    }

    companion object {
        fun fromHashMap(hash: Map<String, Any>): Quiz {
            return Quiz(


                id = hash["id"].toString(),
                title = hash["title"].toString(),
                questions = (hash["questions"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                options = (hash["options"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                correctOption = (hash["correctOption"] as ArrayList<*>?)?.map {
                    it.toString().toInt()
                }?.toList() ?: emptyList(),
                second = hash["second"].toString().toLong(),
                createdBy = hash["createdBy"].toString(),
            )
        }
    }
}
