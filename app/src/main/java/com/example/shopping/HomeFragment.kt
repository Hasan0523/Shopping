package com.example.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.shopping.adapter.CategoryAdapter
import com.example.shopping.adapter.ProductsAdapter
import com.example.shopping.databinding.FragmentHomeBinding
import com.example.shopping.model.Product
import com.example.shopping.model.ProductData
import com.example.shopping.networking.APIClient
import com.example.shopping.networking.APIService
import com.example.shopping.service.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var lastSearch = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeAllRv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.homeCategoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val api = APIClient.getInstance().create(APIService::class.java)
        val shared = SharedPrefHelper.getInstance(requireContext())
        val user = shared.getUser()
        if (user != null) binding.homeAvatarIv.load(user.image)

        api.getAll().enqueue(object : Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                val products = response.body()?.products!!
                changeProductsAdapter(products)
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
        api.getCategories().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val categories = response.body()!!
                binding.homeCategoryRv.adapter = CategoryAdapter(
                    categories,
                    requireContext(),
                    binding.homeCategoryRv,
                    object : CategoryAdapter.CategoryPressed {
                        override fun onPressed(category: String) {
                            if (category == "") {
                                api.getAll().enqueue(object : Callback<ProductData> {
                                    override fun onResponse(
                                        call: Call<ProductData>,
                                        response: Response<ProductData>
                                    ) {
                                        val products = response.body()!!.products
                                        changeProductsAdapter(products)
                                    }

                                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                        Log.d("TAG", "$t")
                                    }

                                })
                            } else {
                                api.getByCategory(category).enqueue(object : Callback<ProductData> {
                                    override fun onResponse(
                                        call: Call<ProductData>,
                                        response: Response<ProductData>
                                    ) {
                                        val products = response.body()?.products!!
                                        changeProductsAdapter(products)
                                    }

                                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                        Log.d("TAG", "$t")
                                    }
                                })
                            }
                        }

                    })
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {

            }

        })


        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == lastSearch) return false

                api.search(query!!).enqueue(object : Callback<ProductData> {
                    override fun onResponse(
                        call: Call<ProductData>,
                        response: Response<ProductData>
                    ) {
                        val products = response.body()!!.products
                        changeProductsAdapter(products)
                    }

                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                        Log.d("TAG", "$t")
                    }

                })
                lastSearch = query

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        binding.homeAvatarIv.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("user", user)
            findNavController().navigate(R.id.action_homeFragment_to_userFragment, bundle)
        }
        binding.homeFilterFab.setOnClickListener {
            if (binding.homeCategoryRv.isVisible) binding.homeCategoryRv.visibility = View.GONE
            else binding.homeCategoryRv.visibility = View.VISIBLE
        }

        return binding.root
    }

    fun changeProductsAdapter(products: List<Product>) {
        binding.homeAllRv.adapter =
            ProductsAdapter(products, requireContext(), object : ProductsAdapter.ProductPressed {
                override fun onPressed(product: Product) {
                    val bundle = Bundle()
                    bundle.putSerializable("product", product)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_productFragment,
                        bundle
                    )
                }
            })
    }

}


