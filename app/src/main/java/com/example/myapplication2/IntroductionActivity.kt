package com.example.myapplication2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication2.databinding.ActivityIntroductionBinding

class IntroductionActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Identify Plants",
                "You can identify the plants you don't know \n" +
                        "through your camera",
                R.drawable.pic1
            ),
            IntroSlide(
                "Learn Many Plants Species",
                "Let's learn about the many plant species that \n" +
                        "exist in this world",
                R.drawable.pic2
            ),
            IntroSlide(
                "Read Many Articles About Plant",
                "Let's learn more about beautiful plants and read \n" +
                        "many articles about plants and gardening",
                R.drawable.pic3
            )
        )
    )
    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Check if the introduction has been seen before
        sharedPreferences = getSharedPreferences("my_app_pref", MODE_PRIVATE)
        val hasSeenIntroduction = sharedPreferences.getBoolean("has_seen_introduction", false)

        if (hasSeenIntroduction) {
            navigateToSignInActivity()
            return
        }

        binding.introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        binding.introSliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        binding.buttonNext.setOnClickListener {
            val currentItem = binding.introSliderViewPager.currentItem
            if (currentItem + 1 < introSliderAdapter.itemCount) {
                // Đang ở trang không phải cuối cùng, chuyển đến trang tiếp theo
                binding.introSliderViewPager.currentItem = currentItem + 1
                if (currentItem + 1 == introSliderAdapter.itemCount - 1) {
                    // Đang ở trang trước trang cuối, đổi nút "Next" thành "Sign In"
                    binding.buttonNext.text = "Sign In"
                }
            } else {
                // Đang ở trang cuối cùng, chuyển đến SignInActivity
                navigateToSignInActivity()
            }
        }

        binding.textSkipIntro.setOnClickListener {
            navigateToSignInActivity()
        }
    }

    private fun navigateToSignInActivity() {
        // Set the flag indicating that the introduction has been seen
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("has_seen_introduction", true)
        editor.apply()

        val intent = Intent(applicationContext, SignInActivity::class.java)
        startActivity(intent)
        finish() // Close the IntroductionActivity
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }

            binding.indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}
