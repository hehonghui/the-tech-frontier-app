package org.tech.frontier.ui.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.fragment.ProgressDialogFragment;

import org.tech.frontier.R;
import org.tech.frontier.utils.HackStatusBarTool;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

public class ContainActivity extends AppCompatActivity
        implements SwipeBackActivityBase {
    private static final String TAG = "org.tech.frontier.ui.activity.ContainActivity";

    private DialogFragment mDialogFragment = null;
    private SwipeBackLayout mSwipeBackLayout;
    private SwipeBackActivityHelper mHelper;

    private int currPosition = -1;

    Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mHelper = new SwipeBackActivityHelper(this);
        this.mHelper.onActivityCreate();

        setContentView(R.layout.activity_contain);

        HackStatusBarTool.hackit(this);

        //点击 toolbar nav 返回上一级
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });

        mSwipeBackLayout = getSwipeBackLayout();

        //设置Activity进入/退出动画
        overridePendingTransition(R.anim.slide_from_right, 0);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("position")) {
                currPosition = bundle.getInt("position");
            }
        }

    }

    public View findViewById(int id) {
        View v = super.findViewById(id);
        return v == null && this.mHelper != null ? this.mHelper.findViewById(id) : v;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currPosition >= 0) {
            swapToFragment(currPosition);
        } else {

        }
    }

    private void swapToFragment(int position) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mHelper.onPostCreate();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return this.mHelper.getSwipeBackLayout();
    }

    public void setSwipeBackEnable(boolean enable) {
        this.getSwipeBackLayout().setEnableGesture(enable);
    }

    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        this.getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

/*
    @Subcriber(tag = EventTag.DISMISS_DIALOG)
    private void dismissDialog(String str) {
        if ((mDialogFragment != null) && mDialogFragment.isVisible()) {
            mDialogFragment.dismiss();
        }
    }

    @Subcriber(tag = EventTag.SHOW_DIALOG)
    private void showDialog(String str) {
        if ((mDialogFragment != null) && mDialogFragment.isVisible()) {
            mDialogFragment.dismiss();
        }

        BaseDialogBuilder baseDialogBuilder = ProgressDialogFragment
                .createBuilder(this, getSupportFragmentManager())
                .setCancelable(false)
                .setMessage(str)
                .setCancelableOnTouchOutside(false);
        mDialogFragment = baseDialogBuilder.show();
    }


    @Subcriber(tag = EventTag.SHOW_TOOLBAR)
    public void showHeader(String str) {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Subcriber(tag = EventTag.HIDE_TOOLBAR)
    public void hideToolbar(String str) {
        mToolbar.setVisibility(View.INVISIBLE);
    }
*/
}
