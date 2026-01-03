package com.example.solitpy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment

class CallFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call, container, false)




        val callInfoButton: AppCompatButton = view.findViewById(R.id.callinfo)

        // تنظیم کلیک روی دکمه
        callInfoButton.setOnClickListener {
            // نمایش DialogFragment
            val dialog = CallInfoDialogFragment()
            dialog.show(parentFragmentManager, "CallInfoDialog")
        }
        return view
    }
}