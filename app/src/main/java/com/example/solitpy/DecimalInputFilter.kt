package com.example.solitpy

import android.text.InputFilter
import android.text.Spanned

class DecimalInputFilter : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        // Regular expression to allow Persian and English numbers, decimal points, and negative sign
        val regex = "^-?[\\d۰-۹]*([.][\\d۰-۹]*)?$"
        val input = dest.subSequence(0, dstart).toString() + source.toString() + dest.subSequence(dend, dest.length)

        return if (input.matches(Regex(regex))) {
            null // Allow the input
        } else {
            "" // Reject the input
        }
    }
}