package app.egghunt

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToLong


class WelcomeActivity : AppCompatActivity() {
    @Suppress("UNUSED_PARAMETER")
    fun onClickButton(view: View) {
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickMenu(view: View) {
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        // Initialize the button.

        findViewById<View>(R.id.button).setOnClickListener(this::onClickButton)

        // Initialize the eggs.

        val eggs = arrayOf<ImageView>(
            findViewById(R.id.egg0),
            findViewById(R.id.egg1),
            findViewById(R.id.egg2),
            findViewById(R.id.egg3))

        eggs.forEach { egg -> run {
            startRotation(egg)
            startTranslation(egg)
        }}

        // Initialize the menu.

        findViewById<View>(R.id.menu).setOnClickListener(this::onClickMenu)
    }

    private fun startRotation(egg: View) {
        val rotate = ObjectAnimator.ofFloat(egg, "rotation", -20f, 20f)

        rotate.duration = (5000 + Math.random() * 1000).roundToLong()
        rotate.repeatCount = ValueAnimator.INFINITE
        rotate.repeatMode = ValueAnimator.REVERSE
        rotate.start()
    }

    private fun startTranslation(egg: View) {
        val translate = ObjectAnimator.ofFloat(egg, "translationY", -300f, 0f)

        translate.interpolator = BounceInterpolator()
        translate.duration = (2000 + Math.random() * 1000).roundToLong()
        translate.repeatCount = ValueAnimator.INFINITE
        translate.repeatMode = ValueAnimator.REVERSE
        translate.start()
    }
}