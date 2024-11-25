package com.rohitneel.photopixelpro.curvedbottomnavigation

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity

class BottomNavTextViewItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "BottomNavTextViewView"
    }


    fun setTextView(title: String) {

        text = title
        setTextColor(Color.WHITE)
        setSingleLine(true)
        ellipsize = TextUtils.TruncateAt.END
        gravity = Gravity.CENTER
        textSize = 11f
    }


}