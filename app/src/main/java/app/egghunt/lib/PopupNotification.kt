package app.egghunt.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import app.egghunt.R

/**
 * A popup notification.
 *
 * _Key Responsibilities_
 *
 * * Display push notifications while the app runs in the foreground.
 */

class PopupNotification(private val activity: Activity) {
    private lateinit var body: String
    private lateinit var title: String

    @SuppressLint("InflateParams")
    private fun buildView(): View {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.popup_notification, null)

        view.findViewById<TextView>(R.id.body).text = body
        view.findViewById<TextView>(R.id.title).text = title

        return view
    }

    private fun buildWindow(): PopupWindow {
        val window = PopupWindow(
            buildView(),
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        window.animationStyle = android.R.style.Animation_Toast
        window.contentView.setOnClickListener { window.dismiss() }

        return window
    }

    fun setBody(body: Int): PopupNotification = setBody(activity.getString(body))

    fun setBody(body: String): PopupNotification {
        this.body = body
        return this
    }

    fun setTitle(title: Int): PopupNotification = setTitle(activity.getString(title))

    fun setTitle(title: String): PopupNotification {
        this.title = title
        return this
    }

    fun show() {
        if (activity.window != null) {
            val window = buildWindow()

            window.showAtLocation(activity.window.decorView, Gravity.TOP, 0, 0)

            Handler(Looper.getMainLooper()).postDelayed(
                { if (!activity.isFinishing) window.dismiss() },
                5000
            )
        }
    }
}