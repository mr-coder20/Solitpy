package com.example.solitpy

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment

class CallInfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_call_info, null)

        builder.setView(view)

        // پیدا کردن دکمه‌ها
        val callButton: AppCompatButton = view.findViewById(R.id.call_button)
        val messageButton: AppCompatButton = view.findViewById(R.id.message_button)
        val githubButton: AppCompatButton = view.findViewById(R.id.github_button)
        val telegramButton: AppCompatButton = view.findViewById(R.id.telegram_button)

        val callNumber = "+989308076717" // شماره تماس
        val messageNumber = "+989308076717" // شماره پیامک
        val githubUrl = "https://github.com/mr-coder20" // آدرس GitHub خود را وارد کنید
        val telegramUrl = "https://t.me/a_god_3_6_9" // آدرس Telegram خود را وارد کنید

        // تنظیم کلیک برای دکمه تماس
        callButton.setOnClickListener {
            initiateCall(callNumber)
            dismiss()
        }

        // تنظیم کلیک برای دکمه پیام
        messageButton.setOnClickListener {
            sendMessage(messageNumber)
            dismiss()
        }

        // تنظیم کلیک برای دکمه GitHub
        githubButton.setOnClickListener {
            openUrl(githubUrl)
            dismiss()
        }

        // تنظیم کلیک برای دکمه Telegram
        telegramButton.setOnClickListener {
            openUrl(telegramUrl)
            dismiss()
        }

        return builder.create()
    }

    private fun initiateCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun sendMessage(number: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$number")
        startActivity(intent)
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation) // Define your animation style in styles.xml
    }
}