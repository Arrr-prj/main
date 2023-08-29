package com.example.firebasetest

import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {
    var paymentAmount: Int = 10000  // 예시 데이터: 결제 금액
    var paymentOrderName: String = "Order Name"  // 예시 데이터: 주문명
    // 다른 데이터와 메서드 추가 가능
}
