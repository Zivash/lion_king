package com.example.uidrillmovies

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        var date = ""
        var theater = ""
        var adultTickets = 0
        var childTickets = 0
        var total = 0
        val animDrawable: AnimationDrawable =
            findViewById<ImageView>(R.id.image).drawable as AnimationDrawable
        animDrawable.start()

        findViewById<Button>(R.id.order_button).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.order_layout, null)
            val totalPayment = dialogView.findViewById<TextView>(R.id.payment)
            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
            val alphaAnimator = ObjectAnimator.ofFloat(okButton, "alpha", 0f, 1f).setDuration(2000)
            alphaAnimator.repeatCount = ObjectAnimator.INFINITE
            alphaAnimator.repeatMode = ObjectAnimator.REVERSE
            val rotationAnimator =
                ObjectAnimator.ofFloat(okButton, "rotation", 0f, 360f).setDuration(1000)
            rotationAnimator.repeatCount = 2
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(alphaAnimator, rotationAnimator)

            date = ""
            theater = ""
            adultTickets = 0
            childTickets = 0
            total = 0
            totalPayment.text = getString(R.string.total_ils, total.toString())

            builder.setView(dialogView)

            val theaters = resources.getStringArray(R.array.theaters)
            val tickets = resources.getStringArray(R.array.tickets)

            val arrayAdapterTheater = ArrayAdapter(this, R.layout.dropdown_item, theaters)
            val arrayAdapterTickets = ArrayAdapter(this, R.layout.dropdown_item, tickets)

            val autoCompleteTheater = dialogView.findViewById<AutoCompleteTextView>(R.id.theater)
            val autoCompleteAdult = dialogView.findViewById<AutoCompleteTextView>(R.id.adult)
            val autoCompleteChild = dialogView.findViewById<AutoCompleteTextView>(R.id.child)

            autoCompleteTheater.setAdapter(arrayAdapterTheater)
            autoCompleteAdult.setAdapter(arrayAdapterTickets)
            autoCompleteChild.setAdapter(arrayAdapterTickets)

            autoCompleteTheater.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                    Toast.makeText(this, getString(R.string.theater_successful), Toast.LENGTH_LONG).show()
                    theater = adapterView.getItemAtPosition(i).toString()
                    startOrPauseAnimation(date, theater, adultTickets, childTickets, animatorSet)
                }

            autoCompleteAdult.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                    val itemSelect = (adapterView.getItemAtPosition(i) as String).toInt()

                    total += (itemSelect - adultTickets) * 20
                    adultTickets = itemSelect

                    totalPayment.text = getString(R.string.total_ils, total.toString())
                    startOrPauseAnimation(date, theater, adultTickets, childTickets, animatorSet)
                }

            autoCompleteChild.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                    val itemSelect = (adapterView.getItemAtPosition(i) as String).toInt()

                    total += (itemSelect - childTickets) * 10
                    childTickets = itemSelect
                    totalPayment.text = getString(R.string.total_ils, total.toString())
                    startOrPauseAnimation(date, theater, adultTickets, childTickets, animatorSet)
                }

            val dateButton = dialogView.findViewById<Button>(R.id.date_button)

            dateButton.setOnClickListener {
                val calendar = Calendar.getInstance()
                val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val monthDate = month + 1
                    date = "$day/$monthDate/$year"
                    dateButton.text = date
                    startOrPauseAnimation(date, theater, adultTickets, childTickets, animatorSet)
                }

                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DialogDatePickerTheme,
                    listener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.setCancelable(false)
                datePickerDialog.datePicker.minDate = calendar.timeInMillis
                datePickerDialog.show()
            }

            val dialog = builder.create()
            dialog.setCancelable(false)

            dialogView.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                date = ""
                theater = ""
                adultTickets = 0
                childTickets = 0
                total = 0
                dialog.dismiss()
            }

            okButton.setOnClickListener {
                if (date == "" || theater == "" || adultTickets == 0 && childTickets == 0) {
                    Toast.makeText(this, getString(R.string.error_input), Toast.LENGTH_LONG)
                        .show()
                    val rotateAnimator =
                        ObjectAnimator.ofFloat(
                            okButton,
                            "rotation",
                            0f,
                            12.25f,
                            12.25f,
                            -12.25f,
                            0f
                        ).setDuration(600)
                    rotateAnimator.start()
                } else {
                    Toast.makeText(this, getString(R.string.order_successful), Toast.LENGTH_LONG)
                        .show()
                    dialog.dismiss()
                }
            }

            dialog.show()
        }

        findViewById<Button>(R.id.get_tickets_button).setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.tickets_information)

            val theaterTextView = bottomSheetDialog.findViewById<TextView>(R.id.theater_selected)
            val adultTicketsTextView = bottomSheetDialog.findViewById<TextView>(R.id.adult_tickets)
            val childTicketsTextView = bottomSheetDialog.findViewById<TextView>(R.id.child_tickets)
            val dateTextView = bottomSheetDialog.findViewById<TextView>(R.id.date)
            val totalPayTextView = bottomSheetDialog.findViewById<TextView>(R.id.total)

            theaterTextView?.text = getString(R.string.theater_choice, theater)
            adultTicketsTextView?.text = getString(R.string.adult_choice, adultTickets.toString())
            childTicketsTextView?.text = getString(R.string.child_choice, childTickets.toString())
            dateTextView?.text = getString(R.string.date_choice, date)
            totalPayTextView?.text = getString(R.string.total_ils, total.toString())

            bottomSheetDialog.findViewById<Button>(R.id.buy_button)?.setOnClickListener {
                if (date == "" || theater == "" || adultTickets == 0 && childTickets == 0) {
                    Toast.makeText(
                        this,
                        getString(R.string.error_fill),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this, getString(R.string.purchase_successful), Toast.LENGTH_LONG).show()
                    bottomSheetDialog.dismiss()
                    date = ""
                    theater = ""
                    adultTickets = 0
                    childTickets = 0
                    total = 0
                }
            }

            bottomSheetDialog.show()
        }
    }

    private fun startOrPauseAnimation(
        date: String,
        theater: String,
        adultTickets: Int,
        childTickets: Int,
        animatorSet: AnimatorSet
    ) {

        if (date != "" && theater != "" && !(adultTickets == 0 && childTickets == 0)) {
            animatorSet.start()
        } else if (adultTickets == 0 && childTickets == 0) {
            animatorSet.end()
        }
    }
}