package com.kristianskokars.tasky.lib

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun showToast(
    context: Context,
    @StringRes text: Int,
    length: Int = Toast.LENGTH_SHORT,
) = Toast.makeText(
    context,
    context.getString(text),
    length
).show()

fun showToast(
    context: Context,
    text: String,
    length: Int = Toast.LENGTH_SHORT,
) = Toast.makeText(
    context,
    text,
    length
).show()
