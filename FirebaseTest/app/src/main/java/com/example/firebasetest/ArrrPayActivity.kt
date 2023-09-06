package com.example.firebasetest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.firebasetest.databinding.ActivityArrrPayBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.PaymentCallback
import com.tosspayments.paymentsdk.model.PaymentWidgetStatusListener
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.PaymentMethod
import com.tosspayments.paymentsdk.view.PaymentMethod.Rendering.Amount
import com.tosspayments.paymentsdk.view.PaymentMethod.Rendering.Currency


class ArrrPayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArrrPayBinding
    private lateinit var viewModel: ViewModel  // ViewModel 추가

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrr_pay)
        viewModel = ViewModel()  // ViewModel 초기화
        binding.viewModel = viewModel  // ViewModel을 데이터 바인딩으로 연결


        val intent = intent
        val documentId = getIntent().getStringExtra("documentId")
        val title = intent.getStringExtra("documentId")
        val buyerUid = intent.getStringExtra("buyer")
        val seller = intent.getStringExtra("seller")
        val confirm = intent.getStringExtra("confirm")
        val id = intent.getStringExtra("id")
        val bidType = intent.getStringExtra("bidType")

        val doubleEndPrice = getIntent().getDoubleExtra("endPrice", 0.0) // 0.0은 기본값, 적절한 기본값으로 변경 가능


        val amount = Amount(
            value = doubleEndPrice,
            currency = Currency.KRW,
            country = "KR"
        )
        // 이하의 코드는 이전과 동일하게 유지
        val paymentWidget = PaymentWidget(
            activity = this@ArrrPayActivity,
            clientKey = "test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq",
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
                amount = amount,
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

                        val userRef = db.collection("User").document(UserManager.getInstance().userUid)
                        userRef.get().addOnCompleteListener { userTask ->
                            if (userTask.isSuccessful) {
                                val userDocument = userTask.result
                                if (userDocument != null && userDocument.exists()) {
                                    val userMembership = userDocument.getBoolean("membership") ?: false

                                    if (bidType == "bidding") {
                                        val db = FirebaseFirestore.getInstance()

                                        val openItemRef = title?.let {
                                            // 업데이트할 문서의 참조 생성 (title을 문서 ID로 사용)
                                            db.collection("BiddingItem").document(title)
                                        }

                                        if (openItemRef != null) {
                                            // 업데이트할 필드와 값 설정
                                            val updates = hashMapOf<String, Any>(
                                                "ArrrPay" to true,
                                                "BuyerMembership" to userMembership
                                            )

                                            // 문서 업데이트
                                            openItemRef.update(updates)
                                                .addOnSuccessListener {
                                                    Log.d("Firestore", "ArrrPay 필드 업데이트 완료")
                                                }
                                                .addOnFailureListener {
                                                    Log.e("Firestore", "ArrrPay 필드 업데이트 실패: ${it.message}")
                                                }
                                        }
                                    }

                                    if (bidType == "open") {
                                        val db = FirebaseFirestore.getInstance()

                                        val openItemRef = title?.let {
                                            // 업데이트할 문서의 참조 생성 (title을 문서 ID로 사용)
                                            db.collection("OpenItem").document(title)
                                        }

                                        if (openItemRef != null) {
                                            // 업데이트할 필드와 값 설정
                                            val updates = hashMapOf<String, Any>(
                                                "ArrrPay" to true,
                                                "BuyerMembership" to userMembership
                                            )

                                            // 문서 업데이트
                                            openItemRef.update(updates)
                                                .addOnSuccessListener {
                                                    Log.d("Firestore", "ArrrPay 필드 업데이트 완료")
                                                }
                                                .addOnFailureListener {
                                                    Log.e("Firestore", "ArrrPay 필드 업데이트 실패: ${it.message}")
                                                }
                                        }
                                    }
                                }
                            }
                        }


                        finish()
                    }

                    override fun onPaymentFailed(fail: TossPaymentResult.Fail) {
                        Log.e("fail",fail.errorMessage)
                    }
                }
            )
        }

    }
}


