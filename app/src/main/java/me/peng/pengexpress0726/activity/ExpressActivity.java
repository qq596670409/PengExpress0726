package me.peng.pengexpress0726.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import me.peng.pengexpress0726.R;
import me.peng.pengexpress0726.adapter.HistoryAdapter;
import me.peng.pengexpress0726.database.History;
import me.peng.pengexpress0726.model.SearchInfo;
import me.peng.pengexpress0726.utils.DataManager;
import me.peng.pengexpress0726.utils.SnackbarUtils;
import me.peng.pengexpress0726.utils.binding.Bind;
import me.peng.pengexpress0726.utils.binding.ViewBinder;
import me.peng.pengexpress0726.utils.permission.PermissionReq;
import me.peng.pengexpress0726.utils.permission.PermissionResult;

import static android.support.design.widget.NavigationView.GONE;
import static android.support.design.widget.NavigationView.OnClickListener;
import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import static android.support.design.widget.NavigationView.VISIBLE;
import static android.widget.AdapterView.OnItemClickListener;

public class ExpressActivity extends AppCompatActivity
        implements OnClickListener,OnItemClickListener,
        OnNavigationItemSelectedListener{

    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.tv_search)
    private TextView tvSearch;
    @Bind(R.id.tv_post)
    private TextView tvPost;
    @Bind(R.id.tv_sweep)
    private TextView tvSweep;
    @Bind(R.id.lv_un_check)
    private ListView lvUnCheck;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;

    private List<History> mUnCheckList = new ArrayList<>();
    private HistoryAdapter mAdapter = new HistoryAdapter(mUnCheckList);
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ViewBinder.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        lvUnCheck.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setNavigationItemSelectedListener(this);
        tvSearch.setOnClickListener(this);
        tvPost.setOnClickListener(this);
        tvSweep.setOnClickListener(this);
        lvUnCheck.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<History> unCheckList = DataManager.getInstance().getUnCheckList();
        mUnCheckList.clear();
        mUnCheckList.addAll(unCheckList);
        mAdapter.notifyDataSetChanged();
        tvEmpty.setVisibility(mUnCheckList.isEmpty() ? VISIBLE : GONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        drawerLayout.closeDrawers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                item.setChecked(false);
            }
        }, 500);

        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.action_history:
                intent.setClass(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_qrcode:
                intent.setClass(this, QRCodeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.tv_post:
                SnackbarUtils.show(this, "敬请期待");
                break;
            case R.id.tv_sweep:
                startCaptureActivity();
                break;
            default:
                break;
        }
    }

    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Manifest.permission.CAMERA)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        CaptureActivity.start(ExpressActivity.this, false, 0);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(ExpressActivity.this, getString(R.string.no_permission, "相机", "打开扫一扫"));
                    }
                })
                .request();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History history = mUnCheckList.get(position);
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setPost_id(history.getPost_id());
        searchInfo.setCode(history.getCompany_param());
        searchInfo.setName(history.getCompany_name());
        searchInfo.setLogo(history.getCompany_icon());
        ResultActivity.start(this, searchInfo);
    }

    private void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
            return;
        }

        if (System.currentTimeMillis() - mExitTime > 2000){
            mExitTime = System.currentTimeMillis();
            SnackbarUtils.show(this, R.string.click2exit);
        } else {
            finish();
        }
    }
}
