package com.rohitneel.photopixelpro.curvedbottomnavigation

import android.content.Context
import android.util.DisplayMetrics

fun Int.toPx(context: Context) = (this * context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT