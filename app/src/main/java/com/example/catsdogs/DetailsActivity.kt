package com.example.catsdogs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

private const val EXTRA_POSITION = "EXTRA_POSITION"
private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
private const val EXTRA_TEXT = "EXTRA_TEXT"

fun startDetailsActivity(context: Context, position: Int, imageUrl: String, text: String) {
    val intent = Intent(context, DetailsActivity::class.java).apply {
        putExtra(EXTRA_POSITION, position)
        putExtra(EXTRA_IMAGE_URL, imageUrl)
        putExtra(EXTRA_TEXT, text)
    }

    context.startActivity(intent)
}

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent?.let { intent ->
            item_number.text = intent.getIntExtra(EXTRA_POSITION, 1).toString()
            item_text.text = intent.getStringExtra(EXTRA_TEXT)
            Picasso.with(this).load(intent.getStringExtra(EXTRA_IMAGE_URL)).into(item_image)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}