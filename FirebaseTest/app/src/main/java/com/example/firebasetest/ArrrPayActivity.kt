package com.example.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.PaymentCallback
import com.tosspayments.paymentsdk.model.PaymentWidgetStatusListener
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.PaymentMethod
import com.example.firebasetest.databinding.ActivityArrrPayBinding

class ArrrPayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArrrPayBinding
    private lateinit var viewModel: ViewModel  // ViewModel 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrr_pay)
        viewModel = ViewModel()  // ViewModel 초기화
        binding.viewModel = viewModel  // ViewModel을 데이터 바인딩으로 연결

        // 이하의 코드는 이전과 동일하게 유지
        val paymentWidget = PaymentWidget(
            activity = this@ArrrPayActivity,
            clientKey = "test_ck_yL0qZ4G1VOmYooRbYDP8oWb2MQYg",
            customerKey = UserManager.getInstance().userUid,
        )

        val paymentMethodWidgetStatusListener = object : PaymentWidgetStatusListener{
            override fun onLoad() {
                val message = "결제위젯 렌더링 완료"
                Log.d("PaymentWidgetStatusListener", message)
            }
        }

        paymentWidget.run {
            renderPaymentMethods(
                method = binding.paymentMethodWidget,
                amount = PaymentMethod.Rendering.Amount(10000/*가격 - price*/),
                paymentWidgetStatusListener = paymentMethodWidgetStatusListener
            )
            renderAgreement(binding.agreementWidget)
        }

        binding.requestPaymentCta.setOnClickListener {
            paymentWidget.requestPayment(
                paymentInfo = PaymentMethod.PaymentInfo(orderId = "wBWO9RJXO0UYqJMV4er8J", orderName = "Arrr 페이"/*주문 할 물건*/),
                paymentCallback = object : PaymentCallback {
                    override fun onPaymentSuccess(success: TossPaymentResult.Success) {
                        Log.i("success", success.paymentKey)
                        Log.i("success", success.orderId)
                        Log.i("success", success.amount.toString())
                        // 결제가 됐을 때 실행부분 추가하면 됨


                    }

                    override fun onPaymentFailed(fail: TossPaymentResult.Fail) {
                        Log.e("fail",fail.errorMessage)
                    }
                }
            )
        }

    }
}