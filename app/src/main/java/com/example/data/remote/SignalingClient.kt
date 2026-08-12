package com.example.data.remote

import com.example.BuildConfig
import com.example.domain.repository.WebRtcSignalMessage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import java.util.concurrent.TimeUnit

class SignalingClient {

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(WebRtcSignalMessage::class.java)

    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private var webSocket: WebSocket? = null

    private val _messages = MutableSharedFlow<WebRtcSignalMessage>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages: Flow<WebRtcSignalMessage> = _messages.asSharedFlow()

    fun connect(roomId: String, userId: String) {
        val signalingUrl = try {
            val url = BuildConfig::class.java.getField("WEBRTC_SIGNALING_URL").get(null) as? String
            if (!url.isNullOrEmpty()) url else "wss://ishc-webrtc-signaling.run.app/ws/call"
        } catch (e: Exception) {
            "wss://ishc-webrtc-signaling.run.app/ws/call"
        }

        val request = Request.Builder()
            .url("$signalingUrl?room=$roomId&user=$userId")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                val joinMsg = WebRtcSignalMessage(type = "PEER_JOINED", sender = userId, payload = roomId)
                send(joinMsg)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val msg = adapter.fromJson(text)
                    if (msg != null) {
                        _messages.tryEmit(msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _messages.tryEmit(WebRtcSignalMessage(type = "ERROR", sender = "System", payload = t.localizedMessage ?: "WebSocket Error"))
            }
        })
    }

    fun send(message: WebRtcSignalMessage) {
        val json = adapter.toJson(message)
        webSocket?.send(json)
    }

    fun disconnect() {
        webSocket?.close(1000, "User Left Session")
        webSocket = null
    }
}
