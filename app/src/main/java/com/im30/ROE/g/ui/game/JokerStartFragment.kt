package com.im30.ROE.g.ui.game

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.im30.ROE.g.R
import com.im30.ROE.g.databinding.FragmentStartJokerBinding
import kotlin.random.Random

const val START_VALUE = 200
const val ROUND_COST = 20
const val PRIZE_FULL = 100
const val PRIZE_HALF = 30

class JokerStartFragment : Fragment() {

    private lateinit var binding: FragmentStartJokerBinding
    private var listArray: Array<Symbols> =
        arrayOf(Symbols.STAR, Symbols.BAR, Symbols.SEVEN, Symbols.CHERRY, Symbols.CROWN)
    private var count: Int = START_VALUE

    private var symbol1: Symbols? = null
    private var symbol2: Symbols? = null
    private var symbol3: Symbols? = null

    companion object {

        fun newInstance(): JokerStartFragment {
            val detailsFragment = JokerStartFragment()
            val args = Bundle()
            detailsFragment.arguments = args
            return detailsFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartJokerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCount.text = count.toString()
        binding.cardView1.setOnClickListener {
            it.isEnabled = false
            symbol1 = getSymbol()
            setImage(binding.view1, binding.iv1, symbol1!!)
        }
        binding.cardView2.setOnClickListener {
            it.isEnabled = false
            symbol2 = getSymbol()
            setImage(binding.view2, binding.iv2, symbol2!!)
        }
        binding.cardView3.setOnClickListener {
            it.isEnabled = false
            symbol3 = getSymbol()
            setImage(binding.view3, binding.iv3, symbol3!!)
        }
        binding.startButton.visibility = View.INVISIBLE
        binding.startButton.setOnClickListener {
            binding.startButton.visibility = View.INVISIBLE
            activateRound()
            bet()
        }
    }

    private fun setScore() {
        if (symbol1 == null || symbol2 == null || symbol3 == null) return

        val countNew = when {
            symbol1 == symbol2 && symbol1 == symbol3 -> {
                count + PRIZE_FULL
            }
            symbol1 == symbol2 || symbol1 == symbol3 || symbol2 == symbol3 -> {
                count + PRIZE_HALF
            }
            else -> {
                count
            }
        }

        count = countNew
        binding.tvCount.text = count.toString()
        checkScore()
    }

    private fun checkScore() {
        if (count - ROUND_COST < 0) {
            binding.startButton.visibility = View.INVISIBLE
            showEndGameDialog()
        } else {
            binding.startButton.visibility = View.VISIBLE
        }
    }

    private fun bet() {
        if (count - ROUND_COST >= 0) {
            count -= ROUND_COST
            binding.tvCount.text = count.toString()
        } else {
            showEndGameDialog()
        }
    }

    private fun setImage(background: View, imageView: ImageView, currentIcon: Symbols) {
        background.setBackgroundColor(ContextCompat.getColor(background.context, R.color.white))
        imageView.setImageResource(currentIcon.imageRes)
        imageView.visibility = View.VISIBLE
        setScore()
    }

    private fun activateRound() {
        binding.cardView1.isEnabled = true
        binding.cardView2.isEnabled = true
        binding.cardView3.isEnabled = true

        binding.iv1.visibility = View.INVISIBLE
        binding.iv2.visibility = View.INVISIBLE
        binding.iv3.visibility = View.INVISIBLE

        symbol1 = null
        symbol2 = null
        symbol3 = null

        binding.view1.background =
            ContextCompat.getDrawable(binding.cardView1.context, R.drawable.card)
        binding.view2.background =
            ContextCompat.getDrawable(binding.cardView2.context, R.drawable.card)
        binding.view3.background =
            ContextCompat.getDrawable(binding.cardView3.context, R.drawable.card)

        binding.tvCount.text = count.toString()

    }

    private fun showEndGameDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("YOU LOSE! Start new game?")
        alertDialogBuilder.setPositiveButton("Start", DialogInterface.OnClickListener(
            object : DialogInterface.OnClickListener, (DialogInterface, Int) -> Unit {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }

                override fun invoke(p1: DialogInterface, p2: Int) {
                    count = START_VALUE
                    activateRound()
                }
            }
        ))
        alertDialogBuilder.setNegativeButton("Finish", DialogInterface.OnClickListener(
            object : DialogInterface.OnClickListener, (DialogInterface, Int) -> Unit {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }

                override fun invoke(p1: DialogInterface, p2: Int) {
                    requireActivity().finish()
                }
            }
        ))
        alertDialogBuilder.show()
    }

    private fun getSymbol(): Symbols {
        val index = Random.nextInt(0, listArray.size)
        return listArray[index]
    }

}