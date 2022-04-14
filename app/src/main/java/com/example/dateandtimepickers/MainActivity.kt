package com.example.dateandtimepickers

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var timePicker: EditText
    private lateinit var datePicker: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timePicker = findViewById(R.id.timePicker)
        timePicker.setOnClickListener { showTimePicker() }

        datePicker = findViewById(R.id.datePicker)
        datePicker.setOnClickListener { showDatePicker() }
    }

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTitleText("Select time")
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .build()

        picker.show(supportFragmentManager, this::class.java.simpleName)

        picker.addOnPositiveButtonClickListener {
            timePicker.setText(getFormattedTime(picker))
        }
    }

    private fun getFormattedTime(picker: MaterialTimePicker): String {
        val hour = if (picker.hour > 12) picker.hour - 12 else picker.hour
        val minute = if (picker.minute < 10) "0${picker.minute}" else picker.minute
        val format = if (hour >= 12) "PM" else "AM"

        return "${hour}:${minute} $format"
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select a date range")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

        picker.show(supportFragmentManager, this::class.java.simpleName)

        picker.addOnPositiveButtonClickListener { pair ->
            datePicker.setText(getFormattedDateRange(pair))
        }
    }

    private fun getFormattedDateRange(
        pair: Pair<Long, Long>
    ): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = pair.first
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val formattedStart = formatter.format(utc.time)
        utc.timeInMillis = pair.second
        val formattedEnd = formatter.format(utc.time)

        return "$formattedStart - $formattedEnd"
    }
}