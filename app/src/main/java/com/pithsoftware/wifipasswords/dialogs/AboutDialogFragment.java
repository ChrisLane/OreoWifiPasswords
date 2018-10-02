package com.pithsoftware.wifipasswords.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.pithsoftware.wifipasswords.R;
import com.pithsoftware.wifipasswords.activities.IntroActivity;
import com.pithsoftware.wifipasswords.extras.MyApplication;

import java.util.List;


public class AboutDialogFragment extends DialogFragment {

    @BindViews({R.id.dialog_about_info,
            R.id.dialog_about_github,
            R.id.dialog_about_library_butterknife,
            R.id.dialog_about_library_retrolambda,
            R.id.dialog_about_library_materialprogressbar,
            R.id.dialog_about_library_appintro,
            R.id.dialog_about_library_rippleeffect,
            R.id.dialog_about_help_readme})
    List<TextView> textViewsLinks;

    @BindView(R.id.dialog_about_version) TextView mVersion;

    @OnClick(R.id.dialog_about_help_intro)
    void intro(View v) { startActivity(new Intent(v.getContext(), IntroActivity.class)); }



    public static AboutDialogFragment getInstance() {
        return new AboutDialogFragment();
    }

    public AboutDialogFragment() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity parent = getActivity();
        AlertDialog.Builder builder;

        //Choose theme
        if (MyApplication.sIsDark) {
            builder = new AlertDialog.Builder(parent, R.style.AlertDialogTheme_Dark);
        } else {
            builder = new AlertDialog.Builder(parent, R.style.AlertDialogTheme);
        }

        View helpDialogLayout = LayoutInflater.from(parent).inflate(R.layout.dialog_about, null);

        ButterKnife.bind(this, helpDialogLayout);
        ButterKnife.apply(textViewsLinks, LINK);

        //set Version Number
        String versionName;

        try {
            versionName = mVersion.getText().toString() + " " + parent.getPackageManager().getPackageInfo(parent.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = getString(R.string.dialog_about_version_error);
        }

        mVersion.setText(versionName);


        builder.setTitle(getString(R.string.dialog_about_title));
        builder.setView(helpDialogLayout)
                .setPositiveButton(R.string.dialog_about_button, (dialog, which) -> {
                    //do nothing
                });

        return builder.create();
    }


    final static ButterKnife.Action<TextView> LINK = (view, index) -> view.setMovementMethod(LinkMovementMethod.getInstance());
}
