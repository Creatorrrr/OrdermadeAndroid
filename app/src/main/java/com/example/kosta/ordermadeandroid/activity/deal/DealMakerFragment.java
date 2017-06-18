package com.example.kosta.ordermadeandroid.activity.deal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.example.kosta.ordermadeandroid.dto.loader.PurchaseHistoryLoader;
import com.example.kosta.ordermadeandroid.dto.loader.RequestLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by kosta on 2017-06-08.
 */

public class DealMakerFragment extends Fragment {
	private List<PurchaseHistory> purchaseHistoryList;
	private PurchaseHistoryMakerAdapter adapter;

	private static DealMakerFragment instance;

	synchronized public static DealMakerFragment getInstance() {
		if(instance == null) instance =  new DealMakerFragment();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deal_maker, container, false);

		purchaseHistoryList = new ArrayList<>();
		adapter = new PurchaseHistoryMakerAdapter(getActivity(), purchaseHistoryList);

		ListView listView = (ListView)view.findViewById(R.id.dealMakerList);
		listView.setAdapter(adapter);

		OkHttpUtils.initClient(CustomApplication.getClient())
				.get()
				.url(Constants.mBaseUrl + "/deal/xml/searchPurchaseMakerList.do")
				.build()
				.execute(new PurchaseHistoryLoader(purchaseHistoryList, adapter));

		return view;
	}
}
