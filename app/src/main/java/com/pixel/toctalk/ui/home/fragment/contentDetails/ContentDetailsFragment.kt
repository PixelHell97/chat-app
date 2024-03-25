package com.pixel.toctalk.ui.home.fragment.contentDetails

import androidx.lifecycle.ViewModelProvider
import com.pixel.toctalk.R
import com.pixel.toctalk.databinding.FragmentContentDetailsBinding
import com.pixel.toctalk.ui.base.BaseFragment

class ContentDetailsFragment :
    BaseFragment<FragmentContentDetailsBinding, ContentDetailsViewModel>() {
    override fun initViewModel(): ContentDetailsViewModel =
        ViewModelProvider(this)[ContentDetailsViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_content_details
}
