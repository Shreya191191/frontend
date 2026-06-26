package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ui.navigation.AppNavigation
import com.example.frontend.ui.screens.auth.AuthViewModel
import com.example.frontend.ui.screens.booking.CheckoutViewModel
import com.example.frontend.ui.screens.search.SearchFlowViewModel
import com.example.frontend.ui.theme.FrontEndTheme
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    private val checkoutViewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val searchViewModel: SearchFlowViewModel = hiltViewModel()

                AppNavigation(
                    authViewModel = authViewModel,
                    searchViewModel = searchViewModel
                )
            }
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        val paymentId = razorpayPaymentId ?: ""
        val orderId = paymentData?.orderId ?: ""
        val signature = paymentData?.signature ?: ""
        checkoutViewModel.onPaymentSuccess(paymentId, orderId, signature)
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        val message = response ?: "Payment cancelled or failed"
        checkoutViewModel.onPaymentError(message)
    }
}