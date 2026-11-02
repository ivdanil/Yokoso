package com.example.yokoso

import android.os.Bundle
import coil.Coil
import coil.ImageLoader
import okhttp3.OkHttpClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.yokoso.ui.common.LanguageState
import com.example.yokoso.ui.navigation.YokosoNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageState.init(this)
        // Wikimedia требует осмысленный User-Agent, иначе отклоняет запросы
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("User-Agent", "Yokoso/1.0 (student coursework app)")
                        .build()
                )
            }
            .build()
        Coil.setImageLoader(ImageLoader.Builder(this).okHttpClient(client).build())
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    YokosoNavHost()
                }
            }
        }
    }
}