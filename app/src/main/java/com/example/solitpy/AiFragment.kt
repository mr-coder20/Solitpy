package com.example.solitpy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.request.PurchaseRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException

data class ApiResponse(
    val Ai_api: String,
    val versionApp: String
)

data class Message(val role: String, val content: String)

data class ChatRequest(val model: String, val messages: List<Message>)

data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)

class AiFragment : Fragment() {

    private lateinit var payment: Payment
    private var paymentConnection: Connection? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messagesList = mutableListOf<Message>()
    private lateinit var progressBar: ProgressBar
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var token: String
    private lateinit var sharedPreferences: SharedPreferences
    private val CLICK_COUNT_KEY = "click_count"
    private var clickCount: Int = 5 // مقدار پیش‌فرض 10
    private lateinit var sendButton: Button
    private lateinit var messageInput: EditText

    interface ChatApi {
        @Headers("Content-Type: application/json")
        @POST("chat/completions")
        fun sendMessage(@Body chatRequest: ChatRequest): Call<ChatResponse>
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.avalai.ir/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original: Request = chain.request()
                        val request: Request = original.newBuilder()
                            .header("Authorization", "Bearer $token")
                            .method(original.method, original.body)
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
    }

    private val chatApi: ChatApi by lazy {
        retrofit.create(ChatApi::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ai, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        chatAdapter = ChatAdapter(messagesList, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = chatAdapter
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
        progressBar = view.findViewById(R.id.progressBar)

        messageInput = view.findViewById(R.id.messageInput)
        sendButton = view.findViewById(R.id.sendButton)
        val clearChatButton: Button = view.findViewById(R.id.clearChatButton)

        // تنظیمات پرداخت
        val localSecurityCheck = SecurityCheck.Enable(
            rsaPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDK1ra+j0RKNQ5vuLz9qyfXlrVQBnXS0sB8Dt7eISQ5cC7wkVTXBWAJDG2czUnwP4wSGmIdMLg8xSFPF+fX7HIL29A9GnHTxdNQCOZ9569dObp7i0cqaJjvwRTxP4KsZT/6l0AlqAjYjJdm5vpVAgg85fhTPnEf5915mxruZQRFOHmnYjgfC1iVhmYmlDZhiH7DJIb8N2ML0WRaNEwdEjzfnZH4E3OB0xW2G/6tH08CAwEAAQ=="
        )
        val paymentConfiguration = PaymentConfiguration(
            localSecurityCheck = localSecurityCheck
        )
        payment = Payment(context = requireContext(), config = paymentConfiguration)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Get the current click count
        loadClickCount(5)

        sendButton.setOnClickListener {
            val messageContent = messageInput.text.toString()
            if (messageContent.isNotEmpty()) {
                sendMessage(messageContent)
                messageInput.text.clear()

                // Reduce click count
                clickCount -= 1
                saveClickCount(clickCount)

                // Show a message with the current click count
                if (clickCount > 0) {
                    Toast.makeText(
                        requireContext(),
                        "You have $clickCount chances left.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // Check if the click count has reached 0
                if (clickCount <= 0) {
                    sendButton.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_lock,
                        0
                    ) // Set the vector drawable as icon
                    sendButton.setPadding(0, 0, 10, 0) // Set padding for the button

                    // Disable the send button
                }
            }
        }
        if (clickCount <= 0) {
            sendButton.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_lock,
                0
            ) // Set the vector drawable as icon
            sendButton.setPadding(0, 0, 10, 0) // Set padding for the button
        }
        clearChatButton.setOnClickListener {
            clearChat()
        }

        // بارگذاری توکن
        fetchApiData()

        return view
    }

    private fun sendMessage(content: String) {
        if (!::token.isInitialized) {
            Toast.makeText(requireContext(), "Failure Connect to Server", Toast.LENGTH_SHORT).show()
            return
        } else if (clickCount <= 0) {
            Toast.makeText(
                requireContext(),
                "You haven't chances!",
                Toast.LENGTH_SHORT
            ).show()
            goPay()
            return
        }
        try {
            val userMessage = Message(role = "user", content = content)
            messagesList.add(userMessage)
            chatAdapter.notifyDataSetChanged()
            updateUI()
            recyclerView.scrollToPosition(messagesList.size - 1)

            progressBar.visibility = View.VISIBLE

            val chatRequest = ChatRequest(
                model = "gpt-3.5-turbo",
                messages = messagesList
            )

            chatApi.sendMessage(chatRequest).enqueue(object : retrofit2.Callback<ChatResponse> {
                override fun onResponse(
                    call: Call<ChatResponse>,
                    response: retrofit2.Response<ChatResponse>
                ) {
                    progressBar.visibility = View.GONE // Hide ProgressBar
                    if (response.isSuccessful && response.body() != null) {
                        val choices = response.body()!!.choices
                        if (choices.isNotEmpty()) {
                            choices.forEach { choice ->
                                messagesList.add(
                                    Message(
                                        role = "assistant",
                                        content = choice.message.content
                                    )
                                )
                            }
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        messagesList.add(
                            Message(
                                role = "assistant",
                                content = "Error: $errorMessage"
                            )
                        )
                    }
                    chatAdapter.notifyDataSetChanged()
                    updateUI()
                    recyclerView.scrollToPosition(messagesList.size - 1)
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE // Hide ProgressBar
                    messagesList.add(Message(role = "assistant", content = "Error: ${t.message}"))
                    chatAdapter.notifyDataSetChanged()
                    updateUI()
                    recyclerView.scrollToPosition(messagesList.size - 1)
                }
            })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearChat() {
        messagesList.clear() // Clear the messages list
        chatAdapter.notifyDataSetChanged()
        updateUI() // Update the adapter
    }

    private fun updateUI() {
        if (messagesList.isEmpty()) {
            lottieAnimationView.visibility = View.VISIBLE  // Show Lottie animation
        } else {
            lottieAnimationView.visibility = View.GONE  // Hide Lottie animation
        }
    }

    private fun fetchApiData() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://rash32.ir/roshd/amir3.php?api_key=hva-D2J]:rZA?LpmVmA^4dic?F%3EuU%3C%3ECk,cS;~n@")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace() // Handle failure
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Failed to connect to server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        // چاپ محتوای پاسخ برای بررسی
                        Log.d("ApiResponse", responseBody)

                        // تلاش برای تجزیه پاسخ به آرایه
                        try {
                            val apiResponse =
                                Gson().fromJson(responseBody, Array<ApiResponse>::class.java)

                            if (apiResponse.isNotEmpty()) {
                                token = apiResponse[0].Ai_api // Save token in variable
                                requireActivity().runOnUiThread {
                                    // Toast.makeText(
                                    //     requireContext(),
                                    //     "Token loaded successfully",
                                    //     Toast.LENGTH_SHORT
                                    // ).show()
                                }
                            } else {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        "No data received from server",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: JsonSyntaxException) {
                            Log.e("ApiResponse", "JSON parsing error: ${e.message}")
                            requireActivity().runOnUiThread {
                                Toast.makeText(
                                    requireContext(),
                                    "Error parsing response: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Failure Connect to Server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun loadClickCount(count: Int) {
        clickCount = sharedPreferences.getInt(CLICK_COUNT_KEY, count) // مقدار پیش‌فرض 10
    }

    private fun saveClickCount(count: Int) {
        sharedPreferences.edit().putInt(CLICK_COUNT_KEY, count).apply()
    }

    private fun goPay() {
        paymentConnection = payment.connect {
            connectionSucceed {
                val purchaseRequest = PurchaseRequest(
                    productId = "AiChatBot",
                    payload = "PAYLOAD"
                )
                payment.purchaseProduct(
                    registry = requireActivity().activityResultRegistry,
                    request = purchaseRequest
                ) {
                    purchaseFlowBegan {
                        // Handle the beginning of the purchase flow
                    }
                    failedToBeginFlow { throwable ->
                        Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
                    }
                    purchaseSucceed { purchaseEntity ->
                        // Set the click count to 100 upon successful purchase
                        saveClickCount(20) // استفاده از saveClickCount برای ذخیره تعداد کلیک
                        clickCount = 20 // Update the local variable as well
                        sendButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0) // Remove the lock icon

                    }
                    purchaseCanceled {
                        Toast.makeText(requireContext(), "Purchase canceled", Toast.LENGTH_SHORT).show()
                    }
                    purchaseFailed { throwable ->
                        Toast.makeText(requireContext(), "Purchase failed: ${throwable.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            connectionFailed { throwable ->
                Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
            }
            disconnected {
                // Handle disconnection
            }
        }
    }

    override fun onDestroy() {
        paymentConnection?.disconnect()
        super.onDestroy()
    }
}