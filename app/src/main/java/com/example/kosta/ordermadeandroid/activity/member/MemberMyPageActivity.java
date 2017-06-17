package com.example.kosta.ordermadeandroid.activity.member;

import android.os.Bundle;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductDetailFragment;

/**
 * Created by kosta on 2017-06-17.
 */

public class MemberMyPageActivity  extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_product_detail, relativeLayout);

		setTitle("나의 프로필");
		getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new MemberMyPageFragment()).commit();
	}


}
