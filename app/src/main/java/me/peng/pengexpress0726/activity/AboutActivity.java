package me.peng.pengexpress0726.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import me.peng.pengexpress0726.BuildConfig;
import me.peng.pengexpress0726.R;

/**
 * Created by Administrator on 2017/7/28.
 */

public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.ll_fragment_container, new AboutFragment())
                .commit();
    }

    public static class AboutFragment extends PreferenceFragment
                implements Preference.OnPreferenceClickListener{

        private Preference mVersion;
        private Preference mStar;
        private Preference mWeibo;
        private Preference mJianshu;
        private Preference mGithub;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);

            mVersion = findPreference("version");
            mStar = findPreference("star");
            mWeibo = findPreference("weibo");
            mJianshu = findPreference("jianshu");
            mGithub = findPreference("github");

            mVersion.setSummary("v " + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener(){
            mStar.setOnPreferenceClickListener(this);
            mWeibo.setOnPreferenceClickListener(this);
            mJianshu.setOnPreferenceClickListener(this);
            mGithub.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mStar){
                openUrl(getString(R.string.about_project_url));
                return true;
            } else if (preference == mWeibo || preference == mJianshu || preference == mGithub){
                return true;
            }
            return false;
        }

        private void openUrl(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
