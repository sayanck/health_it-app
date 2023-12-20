package com.healthit.application.utils

import android.annotation.SuppressLint
import android.webkit.WebView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("htmlText")
fun TextView.setHtmlText(htmlString: String?) {
    if (htmlString != null) {
        text = HtmlCompat.fromHtml(htmlString, 0)
    }
}

//@BindingAdapter("last_update")
//fun TextView.setTimeStamp(dateString: String?) {
//    if (dateString != null) {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
//        val date = sdf.parse(dateString)
//        val formatter = SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH)
//        val resultDate =
//            this.context.getString(com.grillhouse.mx.R.string.last_update) + " " + formatter.format(
//                date!!
//            )
//        text = resultDate
//    }
//}

@SuppressLint("SetJavaScriptEnabled")
@BindingAdapter("loadHtml")
fun WebView.loadHtml(loadHtml: String?) {
    if (!loadHtml.isNullOrEmpty()) {
        val html = loadHtml.replace(
            "<p>", "<p style=\"color:#1e1e1e\n" +
                    "\n;font-size:12px;\">"
        )

        val stringToAdd = "width=\"100%\" "
        // Create a StringBuilder to insert string in the middle of content.
        val sb = StringBuilder(html)
        var i = 0
        var cont = 0
        while (i != -1) {
            i = html.indexOf("src", i + 1)
            if (i != -1) sb.insert(i + cont * stringToAdd.length, stringToAdd)
            ++cont
        }

        logD("WebView_loadHtml    $sb")
        this.settings.javaScriptEnabled = true
        this.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null)
    }
}
