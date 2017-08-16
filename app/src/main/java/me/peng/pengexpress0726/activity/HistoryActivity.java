package me.peng.pengexpress0726.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.peng.pengexpress0726.R;
import me.peng.pengexpress0726.adapter.HistoryAdapter;
import me.peng.pengexpress0726.database.History;
import me.peng.pengexpress0726.model.SearchInfo;
import me.peng.pengexpress0726.utils.DataManager;
import me.peng.pengexpress0726.utils.binding.Bind;

import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.AdapterView.OnItemLongClickListener;

/**
 * Created by Administrator on 2017/7/28.
 */

public class HistoryActivity extends BaseActivity
        implements OnItemClickListener,OnItemLongClickListener{

    @Bind(R.id.lv_history_list)
    private ListView lvHistoryList;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;

    private List<History> mHistoryList = new ArrayList<>();
    private HistoryAdapter mAdapter = new HistoryAdapter(mHistoryList);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvHistoryList.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        lvHistoryList.setOnItemClickListener(this);
        lvHistoryList.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<History> historyList = DataManager.getInstance().getHistoryList();
        mHistoryList.clear();
        mHistoryList.addAll(historyList);
        mAdapter.notifyDataSetChanged();
        tvEmpty.setVisibility(mHistoryList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> view, View atg1, int position, long arg3) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setPost_id(mHistoryList.get(position).getPost_id());
        searchInfo.setCode(mHistoryList.get(position).getCompany_param());
        searchInfo.setName(mHistoryList.get(position).getCompany_name());
        searchInfo.setLogo(mHistoryList.get(position).getCompany_icon());
        ResultActivity.start(this, searchInfo);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> view, View arg1, final int position, long arg3) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tips))
                .setMessage(getResources().getString(R.string.sure_delete_history))
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataManager.getInstance().deleteById(mHistoryList.get(position).getPost_id());
                        refreshList();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
        return true;
    }
}
