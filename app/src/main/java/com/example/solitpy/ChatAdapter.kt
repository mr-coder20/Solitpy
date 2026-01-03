package com.example.solitpy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



class ChatAdapter(private val messages: List<Message>, private val context: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
    }

    private val animatedPositions = mutableSetOf<Int>() // برای ذخیره وضعیت انیمیشن

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.content

        // تنظیم پس‌زمینه بر اساس نقش پیام
        if (message.role == "user") {
            holder.itemView.setBackgroundResource(R.drawable.user_background) // پس‌زمینه برای پیام کاربر
        } else {
            holder.itemView.setBackgroundResource(R.drawable.assistant_background) // پس‌زمینه برای پیام دستیار
        }

        // اعمال انیمیشن فقط برای پیام‌های جدید
        if (!animatedPositions.contains(position)) {
            val animation = if (message.role == "user") {
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.user_message_in)
            } else {
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.assistant_message_in)
            }
            holder.itemView.startAnimation(animation)
            animatedPositions.add(position) // وضعیت انیمیشن را ذخیره کنید
        }

        // اضافه کردن Listener برای کپی متن
        holder.messageText.setOnClickListener {
            // کپی متن به کلیپ بورد
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Chat Message", message.content)
            clipboard.setPrimaryClip(clip)

            // نمایش پیام کپی شدن
            Toast.makeText(context, "The Text Was Copied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    // تابعی برای پاک کردن وضعیت انیمیشن (در صورت نیاز)

}