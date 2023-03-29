package com.example.myapplication.sys.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


fun String.checkIfIsValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.checkIfIsValidPassword(): Boolean {
    return this.length >= 8
}

fun TextInputLayout.showAndHideError(isError: Boolean, errorMessage: String = "") {
    this.error =
        if (isError) null else errorMessage

    if (isError) {
        this.isErrorEnabled = false
    }
}

private const val LOG_TAG = "ActivityExtensions"
fun View.hideBoard() {
    try {
        val methodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        methodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (error: Exception) {
        Log.e(LOG_TAG, "Error hiding keyboard", error)
    }
}

fun Long.convertLongToTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun convertDateToLong(): Long {
    val localDateTime: LocalDateTime = LocalDateTime.now()
    val zdt: ZonedDateTime = ZonedDateTime.of(localDateTime, TimeZone.getTimeZone("UTC").toZoneId())
    return zdt.toInstant().toEpochMilli()
}

fun TextInputLayout.clearInputFocus() {
    this.editText?.run {
        isFocusableInTouchMode = false
        clearFocus()
        isFocusableInTouchMode = true
    }
    this.nextFocusDownId
}

fun Fragment.createPiker(listener: (String) -> Unit, negative: () -> Unit) {
    val builder = MaterialDatePicker.Builder.datePicker()
    builder.setTitleText("Select date")
    builder.setSelection(convertDateToLong())
    builder.setTheme(R.style.MyCusTomCalendar)

    val picker: MaterialDatePicker<*> = builder.build()
    picker.addOnPositiveButtonClickListener {
        listener.invoke((picker.selection as Long).convertLongToTime())
    }
    picker.addOnNegativeButtonClickListener {
        negative.invoke()
    }
    picker.addOnCancelListener {
        negative.invoke()
    }

    picker.show(childFragmentManager, picker.toString())
}

fun Spinner.getPosition(listener: (Int) -> Unit) {
    this.onItemSelectedListener =
        (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                listener.invoke(pos)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        })
}

fun String.isValidPhone(): Boolean = this.length == 10

fun String.isValidCollaboratorNumber(): Boolean = this.length > 8

fun Int.isValidSpinner(): Boolean = this != 0

fun String.isValidText(): Boolean = this.length > 4

