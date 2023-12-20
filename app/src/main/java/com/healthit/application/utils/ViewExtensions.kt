package com.healthit.application.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.net.Uri
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import com.healthit.application.R
import com.healthit.application.data.AppPreferences
import com.healthit.application.utils.constant.HelperConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Activity.addDots(currentPage: Int, item: Int, linerLayout: LinearLayout) {
    val dots = arrayOfNulls<ImageView>(item)
    linerLayout.removeAllViews()
    for (i in 0 until item) {
        dots[i] = ImageView(this)
        dots[i]?.setImageResource(R.drawable.dot_empty)
        dots[i]?.setPadding(0, 0, 12, 0)
        linerLayout.addView(dots[i])
    }
    if (dots.isNotEmpty()) {
        dots[currentPage]?.setImageResource(R.drawable.dot_fill_black)
    }
}

fun addDots(context: Context, currentPage: Int, item: Int, linerLayout: LinearLayout) {
    val dots = arrayOfNulls<ImageView>(item)
    linerLayout.removeAllViews()
    for (i in 0 until item) {
        dots[i] = ImageView(context)
        dots[i]?.setImageResource(R.drawable.dot_empty)
        dots[i]?.setPadding(0, 0, 12, 0)
        linerLayout.addView(dots[i])
    }
    if (dots.isNotEmpty()) {
        dots[currentPage]?.setImageResource(R.drawable.dot_fill_black)
    }
}

fun TextView.textColor(context: Context, color: Int) {
    this.setTextColor(ContextCompat.getColor(context, color))
}

fun TextView.compoundDrawablesWithIntrinsicBounds(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun String.formatString(): String {
    return replace(" ", "").lowercase(Locale(AppPreferences.selectedLanguage)).trim()
}

// "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" - > "dd/MM/YYYY"
fun findDate(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale(AppPreferences.selectedLanguage))
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale(AppPreferences.selectedLanguage))
        outputFormat.format(inputFormat.parse(date)!!)
    } catch (e: ParseException) {
        ""
    }
}

// "yyyy-MM-dd'T'HH:mm:ss'Z'" - > "dd/MM/YYYY \n hh:mm aa"
fun findDateAndTime(date: String): String {
    return try {
        val inputFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale(AppPreferences.selectedLanguage))
        val outputFormat = SimpleDateFormat("MMMM dd", Locale(AppPreferences.selectedLanguage))
        val newDate: Date = inputFormat.parse(date)!!
        val formattedDate = outputFormat.format(newDate)
        val formattedTime =
            SimpleDateFormat("hh:mm aa", Locale(AppPreferences.selectedLanguage)).format(newDate)
        "$formattedDate\n$formattedTime"
    } catch (e: ParseException) {
        ""
    }
}

// "yyyy-MM-dd'T'HH:mm:ss'Z'" - > "MMM dd, YYYY"
fun findDateTime(time: String): String {
    val inputFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale(AppPreferences.selectedLanguage))
    val outputFormat =
        SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale(AppPreferences.selectedLanguage))
    val date: Date = inputFormat.parse(time)!!
    return outputFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertGMTToDateAndTime(date: String): String {
    return try {
        val inputFormat: DateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zz yyy")
        val d = inputFormat.parse(date)
        val outputFormat: DateFormat = SimpleDateFormat("MMM dd, yyy h:mm a")
        outputFormat.format(d!!)
    } catch (e: ParseException) {
        ""
    }
}

@SuppressLint("SimpleDateFormat")
// Convert in 24 hours format
fun getTimeFromGMT(date: String): String {
    return try {
        val inputFormat: DateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zz yyy")
        val d = inputFormat.parse(date)
        val outputFormat: DateFormat = SimpleDateFormat("HH:mm a")
        outputFormat.format(d!!)
    } catch (e: ParseException) {
        ""
    }
}

@SuppressLint("SimpleDateFormat")
fun getDateFromGMT(date: String): String {
    return try {
        val inputFormat: DateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zz yyy")
        val d = inputFormat.parse(date)
        val outputFormat: DateFormat = SimpleDateFormat("EEEE, MMMM dd yyy")
        outputFormat.format(d!!)
    } catch (e: ParseException) {
        ""
    }
}

@SuppressLint("SimpleDateFormat")
fun convertDateAndTimeToDate(date: String): Date? {
    return try {
        val inputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm a")
        val d = inputFormat.parse(date)
        val outputFormat: DateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zz yyy")
//        outputFormat.format(d!!)
        outputFormat.parse(d!!.toString())
    } catch (e: ParseException) {
        return null
    }

}

fun convertDateToAnotherFormat(dateString: String): String {
    return try {
        val inputFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale(AppPreferences.selectedLanguage))
        val outputFormat =
            SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale(AppPreferences.selectedLanguage))
        val date: Date = inputFormat.parse(dateString)!!
        outputFormat.format(date)
    } catch (e: ParseException) {
        ""
    }
}

fun getDate(time: Long): String {
    val date = Date(time * 1000L)
    val sdf = SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale(AppPreferences.selectedLanguage))
    sdf.timeZone = TimeZone.getTimeZone("GMT-4")
    return sdf.format(date)
}

fun getFullDate(time: String): String {
    val inputFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale(AppPreferences.selectedLanguage))
    val outPutFormat = SimpleDateFormat("MMMM dd, yyyy", Locale(AppPreferences.selectedLanguage))
    val date: Date = inputFormat.parse(time)!!
    return outPutFormat.format(date)
}

fun Activity.changeLocale(locale: String) {
    val dm: DisplayMetrics = resources.displayMetrics
    val conf: Configuration = resources.configuration
    conf.setLocale(Locale(locale))
    resources.updateConfiguration(conf, dm)
}

fun findTimeStamp(dateString: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale(AppPreferences.selectedLanguage))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date = sdf.parse(dateString)!!
    return date.time
}

fun findCurrentTimeStamp(): Long {
    return System.currentTimeMillis()
}

fun findTime(time: String): String {
    var newTime = ""
    try {
        val sdf = SimpleDateFormat("H:mm", Locale(AppPreferences.selectedLanguage))
        val dateObj: Date = sdf.parse(time)!!
        newTime =
            SimpleDateFormat("hh:mm aa", Locale(AppPreferences.selectedLanguage)).format(dateObj)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return newTime
}

fun findTimeAgo(context: Context, time: Double): String {
    val currentTime = System.currentTimeMillis()
    val timeDiff = currentTime - time
    val secondMiles = 1000.0
    val minuteMiles: Double = 60 * secondMiles
    val hourMiles: Double = 60 * minuteMiles
    val dayMiles: Double = 24 * hourMiles
    val monthMiles: Double = 30 * dayMiles
    val yearMiles: Double = 12 * monthMiles

    when {
        timeDiff < minuteMiles -> {
            return context.getString(R.string.just_now)
        }

        timeDiff < 2 * minuteMiles -> {
            return context.getString(R.string.a_minute_ago)
        }

        timeDiff < 60 * minuteMiles -> {
            return (timeDiff / minuteMiles).toInt()
                .toString() + " " + context.getString(R.string.minutes_ago)
        }

        timeDiff < 2 * hourMiles -> {
            return context.getString(R.string.an_hour_ago)
        }

        timeDiff < 24 * hourMiles -> {
            return (timeDiff / hourMiles).toInt()
                .toString() + " " + context.getString(R.string.hours_ago)
        }

        timeDiff < 48 * hourMiles -> {
            return context.getString(R.string.yesterday)
        }

        timeDiff < 30 * dayMiles -> {
            return (timeDiff / dayMiles).toInt()
                .toString() + " " + context.getString(R.string.days_ago)
        }

        timeDiff < 12 * monthMiles -> {
            return (timeDiff / monthMiles).toInt()
                .toString() + " " + context.getString(R.string.month_ago)
        }

        else -> {
            return (timeDiff / yearMiles).toInt()
                .toString() + " " + context.getString(R.string.year_ago)
        }
    }
}

fun TextView.makeLinks(
    spannableString: SpannableString,
    vararg links: Pair<String, View.OnClickListener>,
) {
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = spannableString.indexOf(link.first, startIndexOfLink + 1)
        spannableString.bold(startIndexOfLink, startIndexOfLink + link.first.length)
        spannableString.setSpan(clickableSpan,
            startIndexOfLink,
            startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun setUnderline(spanString: String): SpannableString {
    return SpannableString(spanString).underline(0, spanString.length)
}

fun Double.numberToCurrency(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.CANADA)
    return format.format(this)
}

fun String.makeZeroAfterDecimal(): String {
    return this.replaceAfter(".", "00")
}

fun Int.numberToCurrencyWithoutDollar(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.CANADA)
    val removeDollar = format.format(this).replace("$", "")
    return removeDollar.replace(".00", "")
}

fun Context.openLink(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}

fun TextView.applyWithDisabledTextWatcher(
    textWatcher: TextWatcher,
    codeBlock: TextView.() -> Unit,
) {
    this.removeTextChangedListener(textWatcher)
    codeBlock()
    this.addTextChangedListener(textWatcher)
}

fun TextInputLayout.changeHintTextColor(context: Context?, color: Int) {
    context?.let {
        val colorInt = ContextCompat.getColor(it, color)
        val csl: ColorStateList = ColorStateList.valueOf(colorInt)
        this.hintTextColor = csl
    }
}

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

fun getTodayDate(): Date {
    return Calendar.getInstance(Locale.ENGLISH).time
}

fun Context.statusColor(status: String?): Int {
    return when (status) {
        HelperConstant.AppointmentStatus.CONFIRMED.name -> {
            ContextCompat.getColor(this, R.color.green)
        }
        HelperConstant.AppointmentStatus.DECLINED.name -> {
            ContextCompat.getColor(this, R.color.red)
        }
        HelperConstant.AppointmentStatus.PENDING.name -> {
            ContextCompat.getColor(this, R.color.orange)
        }
        HelperConstant.AppointmentStatus.INPROGRESS.name -> {
            ContextCompat.getColor(this, R.color.yellow)
        }
        else -> {
            ContextCompat.getColor(this, R.color.dark_blue)
        }
    }
}