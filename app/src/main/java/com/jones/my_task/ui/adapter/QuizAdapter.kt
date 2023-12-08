package com.jones.my_task.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jones.my_task.data.model.Quiz
import com.jones.my_task.databinding.ItemLayoutQuizBinding

class QuizAdapter(

    private val userId: String,

    private var quiz: List<Quiz>,
    private val onClick: (String) -> Unit,
    private val onClickQuiz: (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutQuizBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizItemViewHolder(binding)
    }


    override fun getItemCount() = quiz.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val quiz = quiz[position]
        if (holder is QuizItemViewHolder) {
            holder.bind(quiz)
        }
    }

    fun setQuizs(quiz: List<Quiz>) {
        this.quiz = quiz
        notifyDataSetChanged()
    }

    inner class QuizItemViewHolder(
        private val binding: ItemLayoutQuizBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.run {
                tvQuizTitle.text = quiz.title

                if (quiz.createdBy == userId) {
                    ivDelete.visibility = View.VISIBLE
                    ivDelete.setOnClickListener {
                        onClick(quiz.id!!)
                    }
                } else {
                    ivDelete.visibility = View.GONE
                }

                llQuiz.setOnClickListener {
                    onClickQuiz(quiz.id!!)
                }
            }
        }
    }
}