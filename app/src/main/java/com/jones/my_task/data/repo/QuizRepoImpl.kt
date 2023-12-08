package com.jones.my_task.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jones.my_task.core.service.AuthService
import com.jones.my_task.data.model.Quiz
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

) : QuizRepo {

    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("quizzes")
        } ?: throw Exception("No authentic user found")
    }

    override suspend fun getQuizzes() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val quizzes = mutableListOf<Quiz>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        quizzes.add(Quiz.fromHashMap(it))
                    }
                }
                trySend(quizzes)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addQuiz(quiz: Quiz) {
        getDbRef().add(quiz.toHashMap()).await()
    }

    override suspend fun getQuiz(id: String): Quiz? {
        val doc = getDbRef().document(id).get().await()
        return doc.data?.let {
            it["id"] = doc.id
            Quiz.fromHashMap(it)
        }
    }

    override suspend fun deleteQuiz(id: String) {
        getDbRef().document(id).delete().await()
    }

    override suspend fun updateQuiz(quiz: Quiz) {
        getDbRef().document(quiz.id ?: "").set(quiz.toHashMap()).await()
    }
}