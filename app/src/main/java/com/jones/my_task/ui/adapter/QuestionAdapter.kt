import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jones.my_task.databinding.ItemLayoutQuestionBinding

class QuestionAdapter(
    private val userAnswers: MutableMap<Int, String>,

    private var questions: List<String>,
    private var options: List<String>,

    private var correctAnswers: List<Int> = emptyList()

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionItemViewHolder(binding)
    }

    override fun getItemCount() = questions.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = questions[position]
        if (holder is QuestionItemViewHolder) {
            val startIndex = position * 4
            val questionOptions = options.subList(startIndex, startIndex + 4)
            holder.bind(question, questionOptions)
        }
    }

    fun setQuestionsAndOptions(questions: List<String>, options: List<String>) {
        this.questions = questions
        this.options = options
        notifyDataSetChanged()
    }


    fun isAllQuestionsAnswered(): Boolean {
        return userAnswers.size == questions.size
    }


    fun setCorrectAnswers(correctAnswers: List<Int>) {
        this.correctAnswers = correctAnswers
    }

    fun getCorrectAnswers(): List<Int> {
        return correctAnswers
    }

    fun calculateScore(): Int {
        if (correctAnswers.isNullOrEmpty()) {
            return 0
        }

        var score = 0
        for (entry in userAnswers.entries) {
            val questionIndex = entry.key
            val selectedOptionIndex = options.indexOf(entry.value)
            if (selectedOptionIndex == correctAnswers[questionIndex]) {
                score++
            }
        }
        return score
    }


    inner class QuestionItemViewHolder(
        private val binding: ItemLayoutQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: String, options: List<String>) {
            binding.run {
                tvQuestion.text = question

                rbOption1.setOnClickListener { onOptionSelected(0) }
                rbOption2.setOnClickListener { onOptionSelected(1) }
                rbOption3.setOnClickListener { onOptionSelected(2) }
                rbOption4.setOnClickListener { onOptionSelected(3) }

                // Update the UI based on the user's answer
                val selectedOption = userAnswers[adapterPosition]
                when (options.indexOf(selectedOption)) {
                    0 -> rbOption1.isChecked = true
                    1 -> rbOption2.isChecked = true
                    2 -> rbOption3.isChecked = true
                    3 -> rbOption4.isChecked = true
                }
            }
        }

        private fun onOptionSelected(optionIndex: Int) {
            userAnswers[adapterPosition] = options[optionIndex]
            notifyDataSetChanged()
        }
    }


}
