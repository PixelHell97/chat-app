package com.pixel.toctalk.auth.presentation.fragment.resetPassword.updatePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pixel.toctalk.R
import com.pixel.toctalk.auth.presentation.fragment.resetPassword.ResetPasswordViewModel

class UpdatePasswordFragment : Fragment() {
    companion object {
        fun newInstance() = UpdatePasswordFragment()
    }

    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_update_password, container, false)
}
