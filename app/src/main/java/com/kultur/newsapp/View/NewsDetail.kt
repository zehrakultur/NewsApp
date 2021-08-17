package com.kultur.newsapp.View

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kultur.newsapp.R
import com.kultur.newsapp.SaveData
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog
    private lateinit var saveData: SaveData

    override fun onCreate(savedInstanceState: Bundle?) {
        saveData= SaveData(this)
        if(saveData.loadDarkModeState() == true){
            setTheme(R.style.darkTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        alertDialog= SpotsDialog(this)
        alertDialog.show()

        webView.settings.javaScriptEnabled=true
        webView.webChromeClient= WebChromeClient()
        webView.webViewClient=object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                alertDialog.dismiss()
            }
        }

        if(intent !=null){
            if(!intent.getStringExtra("webURL")!!.isEmpty()){
                webView.loadUrl(intent.getStringExtra("webURL")!!)
            }
        }
    }
}