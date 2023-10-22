package com.example.shopping

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import com.example.shopping.adapter.ImageAdapter
import com.example.shopping.databinding.FragmentProductBinding
import com.example.shopping.model.Product
import kotlin.math.roundToInt


private const val ARG_PARAM1 = "product"

class ProductFragment : Fragment() {
    private lateinit var product: Product
    private lateinit var binding: FragmentProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        handleBackPress()
        setView()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        binding.productIamgeVp.adapter =
            ImageAdapter(product.images, binding.productIamgeVp, binding.productParentConstraint)
        binding.productScreenBackFab.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.productScreenTitle.text = product.title
        binding.productScreenBrand.text = product.brand
        binding.productScreenPrice.text = product.price.toString() + " $"
        binding.productScreenDescription.text = product.description
        binding.productScreenRating.text =
            ((product.rating * 10).roundToInt().toDouble() / 10).toString()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.productIamgeVp.layoutParams.height == ActionBar.LayoutParams.MATCH_PARENT) {
                    binding.productIamgeVp.updateLayoutParams {
                        height = 200.toPx(requireContext())
                    }
                    binding.productParentConstraint.setBackgroundColor(Color.WHITE)
                } else {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    fun Int.toPx(context: Context) =
        this * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT

}
