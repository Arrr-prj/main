package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String Username ;
    private FirebaseUser currentUser;

    private ImageButton btnBidding, btnOpen, btnBest, btnShare, btnEvent, btnAlarm, btnMypage, btnMembership;

    private ImageButton btnCateCar, btnCateAcc, btnCateClo, btnCatelimited , btnCatePremium, btnCateShoes, btnCateGoods, btnCateFurn,btnCateSport, btnCateGame, btnCateAnotOther, btnCateBag; // 카테고리 버튼들은 헷갈리지 않게 따로 만들었습니다.
    private ImageButton btnCateNike, btnCateAdidas, btnCateApple, btnCateSamsung; // 브랜드 카테고리 버튼들은 헷갈리지 않게 따로 만들었습니다.

    private ImageView buyItem, saleItem, shareItem, waitingBuy, eventItem, btnAnnounce; // 슬라이드한 곳에 있는 버튼
    private TextView textName;

    private Boolean membershipValue;
    private ViewPagerAdapter viewPagerAdapter;
    private HomeActivityViewModel viewModel;
    private ViewPager2 viewPager2;
    private ImageView ivHamburger;
    private TextView tvPageNumber;
    private LinearLayout ll_left_area;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnShare = findViewById(R.id.btn_share); // 무료나눔 버튼
        btnBidding = findViewById(R.id.btn_bidding); // 비공개 경매 버튼
        btnOpen = findViewById(R.id.btn_open); // 공개 경매 버튼
        btnBest = findViewById(R.id.btn_best); // 인기 상품 버튼
        btnMypage = findViewById(R.id.btn_mypage); // 마이페이지 버튼
        btnEvent = findViewById(R.id.btn_eventAuction); // 이벤트 경매 버튼
        btnAlarm = findViewById(R.id.btn_alarm); // 알림 기능 버튼
        btnMembership = findViewById(R.id.btn_membership); // 멤버십 기능 버튼
        mAuth = FirebaseAuth.getInstance();
        btnAnnounce = findViewById(R.id.btn_announce);
        currentUser = mAuth.getCurrentUser();

        // 카테고리 버튼들은 헷갈리지 않게 따로 선언해줬습니다
        // 필요하실때 가져다가 쓰시면 됩니다.
        btnCateCar = findViewById(R.id.btn_cateCar); // 차량 카테고리 버튼
        btnCateAcc = findViewById(R.id.btn_cateAcc); // 액세서리 카테고리 버튼
        btnCateClo = findViewById(R.id.btn_cateClo); // 의류 카테고리 버튼
        btnCateFurn = findViewById(R.id.btn_cateFurn); // 가구 / 인테리어 카테고리 버튼
        btnCatelimited = findViewById(R.id.btn_cateLim); // 한정판 카테고리 버튼
        btnCateAnotOther = findViewById(R.id.btn_cateAnot); // 기타 카테고리 버튼
        btnCateShoes = findViewById(R.id.btn_cateSho); // 신발 카테고리 버튼
        btnCateGoods = findViewById(R.id.btn_cateGoo); // 굿즈 카테고리 버튼
        btnCatePremium = findViewById(R.id.btn_catePre); // 프리미엄 카테고리 버튼
        btnCateSport = findViewById(R.id.btn_cateSpo); // 스포츠 / 레저 카테고리 버튼
        btnCateGame = findViewById(R.id.btn_cateHob); // 게임 / 취미 카테고리 버튼
        btnCateBag = findViewById(R.id.btn_cateBag);



        btnCateNike = findViewById(R.id.btn_cateNike); // 나이키 브랜드 카테고리 버튼
        btnCateAdidas = findViewById(R.id.btn_cateAdi); // 아디다스 브랜드 카테고리 버튼
        btnCateApple = findViewById(R.id.btn_cateApp); // 애플 브랜드 카테고리 버튼
        btnCateSamsung = findViewById(R.id.btn_cateSam); // 삼성 브랜드 카테고리 버튼



        buyItem = findViewById(R.id.IV_buyItem); // 구매한 아이템 이미지뷰
        saleItem = findViewById(R.id.IV_saleItem); // 판매한 아이템 이미지뷰
        shareItem = findViewById(R.id.IV_shareItem); // 나눔 아이템 이미지뷰
        waitingBuy = findViewById(R.id.IV_waitingBuy); // 구매 대기 아이템 이미지뷰
        eventItem = findViewById(R.id.IV_eventItem); // 이벤트 아이템 이미지뷰

        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        List<BannerItem> bannerItems = new ArrayList<>();
        viewModel.setBannerItems(bannerItems);

        ivHamburger = findViewById(R.id.iv_hamburger);
        ivHamburger.setOnClickListener((View.OnClickListener) this);
        ll_left_area = findViewById(R.id.ll_left_area);
        ll_left_area.setOnClickListener((View.OnClickListener) this);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout); // Correct the ID

        findViewById(R.id.iv_hamburger).setOnClickListener((View.OnClickListener) this);
        viewPager2 = findViewById(R.id.viewPager2);
        initViewPager2();
        textName = findViewById(R.id.text_name);

        showUsername();

//        Intent itent = getIntent();
//        String firstVisit = itent.getStringExtra("firstVisit");
//
//        if(firstVisit.equals("yes")){
//            // 팝업 창 띄워서 어플
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("< Arrr 사용 안내 >").setMessage("\n" +
//
//                    "\n" +
//                    "공개 경매:\n" +
//                    "\n" +
//                    "공개 경매는 사용자들이 실시간으로 접속하여 입찰가를 입력하는 방식의 경매입니다.\n" +
//                    "경매에 올라온 아이템 중 가장 높은 입찰가를 제출한 사용자가 낙찰되어 아이템을 가져가게 됩니다.\n" +
//                    "비공개 경매:\n" +
//                    "\n" +
//                    "비공개 경매는 경매에 올라온 아이템이 24시간 후에 마감되는 형식의 경매입니다.\n" +
//                    "사용자들은 경매가 마감되기 전에 최고가를 모르며 입찰을 진행합니다.\n" +
//                    "경매 마감 후 최고 입찰가 사용자가 아이템을 낙찰받습니다.\n" +
//                    "무료 나눔:\n" +
//                    "\n" +
//                    "무료 나눔은 사용자들이 랜덤으로 당첨될 수 있는 이벤트입니다.\n" +
//                    "무료 나눔에 올라온 아이템에 신청하면 추첨을 통해 당첨될 수 있습니다.\n" +
//                    "이벤트 경매:\n" +
//                    "\n" +
//                    "이벤트 경매는 멤버쉽 회원들에게 제공되는 비밀 경매입니다.\n" +
//                    "Arr어플 내에서 특별한 이벤트에 참여하면 아이템을 얻을 수 있는 기회를 제공합니다.\n" +
//                    "위 안내사항을 참고하여 Arrr 어플을 즐겨보세요! 추가적인 문의나 도움이 필요하면 언제든 문의해주세요."); // 다이얼로그 제목
//            builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
//            builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    mAuth.signOut();
//
//                    // 로그인 인텐트 생성 후 이동
//
//                    FirebaseUser user = mAuth.getCurrentUser();
//
//                    UserManager.getInstance().clearUserUid();
//                    startActivity(new Intent(HomeActivity.this, LobyActivity.class));
//                    finish();
//                }
//            });
//
//            builder.setNegativeButton("아니요", new AlertDialog.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//
//            builder.show(); // 다이얼로그 보이기
//        }
//        else{
//
//        }

        btnCateBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "가방");
                startActivity(intent);
            }
        });
        btnCateCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "차량");
                startActivity(intent);
            }
        });
        btnCateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "액세서리");
                startActivity(intent);
            }
        });
        btnCatelimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "한정판");
                startActivity(intent);
            }
        });
        btnCateAnotOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "기타");
                startActivity(intent);
            }
        });
        btnCateClo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "의류");
                startActivity(intent);
            }
        });
        btnCateShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "신발");
                startActivity(intent);
            }
        });
        btnCateGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "굿즈");
                startActivity(intent);
            }
        });
        btnCateFurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "가구");
                startActivity(intent);
            }
        });
        btnCatePremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "프리미엄");
                startActivity(intent);
            }
        });
        btnCateSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "스포츠 레저");
                startActivity(intent);
            }
        });
        btnCateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "게임 취미");
                startActivity(intent);
            }
        });

        btnCateNike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "Nike");
                startActivity(intent);
            }
        });
        btnCateAdidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "Adidas");
                startActivity(intent);
            }
        });
        btnCateApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "Apple");
                startActivity(intent);
            }
        });
        btnCateSamsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category", "Samsung");
                startActivity(intent);
            }
        });










//        String uid = mAuth.getUid();//UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
//        db = FirebaseFirestore.getInstance();
//        DocumentReference userDocRef = db.collection("User").document(uid);
//
//        userDocRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Username = documentSnapshot.getString("name");
//                        String message = "항상 감사합니다 " + Username+"님";
//                        textName.setText(message);
//                    }
//                });


        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, AlarmActivity.class);
                    startActivity(intent);
            }
        });

        // 이벤트 버튼을 눌렀을 때
        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이벤트 경매 버튼을 클릭할 때
                Intent intent = new Intent(HomeActivity.this, EventAuctionActivity.class);
                startActivity(intent);

            }
        });

        btnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MembershipActivity.class);
                startActivity(intent);
            }
        });



        // 구매한 아이템 눌렀을 때
        buyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyBuyItemsActivity.class);
                startActivity(intent);
            }
        });
        // 판매한 아이템 눌렀을 때
        saleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyItemsActivity.class);
                startActivity(intent);
            }
        });

        // 나눔 아이템 눌렀을 때
        shareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyShareActivity.class);
                startActivity(intent);
            }
        });

        // 구매 대기 아이템 눌렀을 때
        waitingBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyConfirmItemActivity.class);
                startActivity(intent);
            }
        });

        // 이벤트 아이템 눌렀을 때
        eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyEventActivity.class);
                startActivity(intent);
            }
        });
        // 공지사항 버튼을 눌렀을 때
        btnAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AnnounceActivity.class);
                startActivity(intent);
            }
        });
        
        
        btnBidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BiddingActivity.class);
                startActivity(intent);
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 액티비티에서 값을 저장하여 다른 액티비티로 전달
                Intent intent = new Intent(HomeActivity.this, OpenAuctionActivity.class);
                startActivity(intent);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });

        btnBest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BestItemActivity.class);
                startActivity(intent);
            }
        });
        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
    // Home에서 뒤로가기 했을 때 경고 창 띄워주기
    @Override
    public void onBackPressed(){
        // 팝업 창 띄워서 확인 눌렀을 때 로그아웃 되도록
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout").setMessage("로그아웃 하시겠습니까?"); // 다이얼로그 제목
        builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
        builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();

                // 로그인 인텐트 생성 후 이동

                FirebaseUser user = mAuth.getCurrentUser();

                UserManager.getInstance().clearUserUid();
                startActivity(new Intent(HomeActivity.this, LobyActivity.class));
                finish();
            }
        });

        builder.setNegativeButton("아니요", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show(); // 다이얼로그 보이기
    }


    @Override
    public void onClick(@Nullable View pO){
        if(pO != null){
            switch(pO.getId()){
                case R.id.iv_hamburger:
                    if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }else{
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                    break;
                case R.id.ll_left_area:
                    if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    break;
            }
        }
    }

    private void initViewPager2() {
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewModel.setCurrentPosition(position);
            }
        });


    }

    private void subscribeObservers() {
        viewModel.getBannerItemList().observe(this, new Observer<List<BannerItem>>() {
            @Override
            public void onChanged(List<BannerItem> bannerItemList) {
                viewPagerAdapter.submitList(bannerItemList);
            }
        });

        viewModel.getCurrentPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer currentPosition) {
                ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
                viewPager2.setCurrentItem(currentPosition);
            }
        });
    }

    private void showUsername() {
        if (currentUser != null) {
            String uid = currentUser.getUid(); // 현재 사용자의 uid
            db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("User").document(uid);

            userDocRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Username = documentSnapshot.getString("name");
                                String message = "잘생긴 " + Username + "님";
                                textName.setText(message);
                            } else {
                                Toast.makeText(HomeActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}