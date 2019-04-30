package com.example.swiperefreshlistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 使用谷歌提供的SwipeRefreshLayout下拉控件进行下拉刷新和上拉加载
 */
public class MainActivity extends AppCompatActivity {

	private List<String> mList;
	private int mCount;
	private ArrayAdapter<String> mAdapter;
	private SwiperefreshListView swipeRefreshView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		reFresh();
	}

	private void initUI() {
		swipeRefreshView = findViewById(R.id.srl);
		ListView listView = findViewById(R.id.lv);
		// 设置适配器数据
		mList = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			mList.add("我是laohu" + i + "号");
			mCount++;
		}
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
		listView.setAdapter(mAdapter);
	}

	private void reFresh() {
		// 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
		// 设置下拉进度的背景颜色，默认就是白色的
		swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
		// 设置下拉进度的主题颜色
		swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
		// 设置刷新出现的位置
		swipeRefreshView.setProgressViewEndTarget(false,200);
		// 设置向下拉多少出现刷新
		swipeRefreshView.setDistanceToTriggerSync(30);
		// 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
		swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				//默认会自动刷新无需调用这个属性setRefreshing(boolean d)
				// 这里是主线程一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
				final Random random = new Random();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
                        //刷新数据在这里
						mList.add(0, "我是天才" + random.nextInt(100) + "号");
						mAdapter.notifyDataSetChanged();
						Toast.makeText(MainActivity.this, "刷新了一条数据", Toast.LENGTH_SHORT).show();
						// 加载完数据设置为不刷新状态，将下拉进度收起来
						swipeRefreshView.setRefreshing(false);
					}
				}, 1200);
				// 这个不能写在外边，不然会直接收起来
				// swipeRefreshLayout.setRefreshing(false);
			}
		});

		// 设置下拉加载更多
		swipeRefreshView.setOnLoadListener(new SwiperefreshListView.OnLoadListener() {
			
			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// 添加数据
						for (int i = 30; i < 35; i++) {
							mList.add("我是小胡" + i + "号");
							// 这里要放在里面刷新，放在外面会导致刷新的进度条卡住
							mAdapter.notifyDataSetChanged();
						}
						Toast.makeText(MainActivity.this, "数据加载完成", Toast.LENGTH_SHORT).show();
						// 加载完数据隐藏掉上拉加载布局
						swipeRefreshView.setLoading(false);
					}
				}, 1200);
			}
		});
		
	}
}
