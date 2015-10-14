package com.gmail.ndraiman.wifipasswords.dialogs;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.ndraiman.wifipasswords.R;


public class PasscodeActivity extends AppCompatActivity {

    private static final String TAG = "PasscodeActivity";
    private static final String PASSCODE_KEY = "passcode_key";

    private EditText mEditTextPasscode;
    private String mPasscode;

    private LinearLayout mRadioButtonLayout;
    private TextView mDescription;
    private ImageView mLogo;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3, mRadioButton4;
    private RadioButton mRadioError1, mRadioError2, mRadioError3, mRadioError4;
    private ImageButton mButtonOne, mButtonTwo, mButtonThree, mButtonFour, mButtonFive, mButtonSix,
            mButtonSeven, mButtonEight, mButtonNine, mButtonZero, mButtonBackspace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.passcode);

        bindViews();

//        mPasscode = PreferenceManager.getDefaultSharedPreferences(this).getString(PASSCODE_KEY, "0000");
        mPasscode = "1234";

        setupPasscode();

    }


    @Override
    public void onBackPressed() {
        //Block BackPressed
    }

    @Override
    protected void onResume() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onResume();
    }




    /********************************************************/
    /****************** Additional Methods ******************/
    /********************************************************/

    private void bindViews() {

        mDescription = (TextView) findViewById(R.id.passcode_description);
        mRadioButtonLayout = (LinearLayout) findViewById(R.id.layout_radio_buttons);
        mLogo = (ImageView) findViewById(R.id.passcode_logo);

        mRadioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        mRadioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        mRadioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        mRadioButton4 = (RadioButton) findViewById(R.id.radioButton4);

        mRadioError1 = (RadioButton) findViewById(R.id.radioButtonError1);
        mRadioError2 = (RadioButton) findViewById(R.id.radioButtonError2);
        mRadioError3 = (RadioButton) findViewById(R.id.radioButtonError3);
        mRadioError4 = (RadioButton) findViewById(R.id.radioButtonError4);

        mButtonOne = (ImageButton) findViewById(R.id.button_1);
        mButtonTwo = (ImageButton) findViewById(R.id.button_2);
        mButtonThree = (ImageButton) findViewById(R.id.button_3);
        mButtonFour = (ImageButton) findViewById(R.id.button_4);
        mButtonFive = (ImageButton) findViewById(R.id.button_5);
        mButtonSix = (ImageButton) findViewById(R.id.button_6);
        mButtonSeven = (ImageButton) findViewById(R.id.button_7);
        mButtonEight = (ImageButton) findViewById(R.id.button_8);
        mButtonNine = (ImageButton) findViewById(R.id.button_9);
        mButtonZero = (ImageButton) findViewById(R.id.button_0);
        mButtonBackspace = (ImageButton) findViewById(R.id.button_backspace);

        mEditTextPasscode = (EditText) findViewById(R.id.edit_text_passcode);

    }

    private void setupPasscode() {

        mEditTextPasscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: " + "s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");

                if (count > before) { //digit added

                    switch (count) {

                        case 1:
                            mRadioButton1.setChecked(true);
                            break;

                        case 2:
                            mRadioButton2.setChecked(true);
                            break;

                        case 3:
                            mRadioButton3.setChecked(true);
                            break;

                        case 4:
                            mRadioButton4.setChecked(true);
                            break;
                    }

                } else { //digit subtracted

                    switch (before) {

                        case 1:
                            mRadioButton1.setChecked(false);
                            break;

                        case 2:
                            mRadioButton2.setChecked(false);
                            break;

                        case 3:
                            mRadioButton3.setChecked(false);
                            break;
                    }
                }

                if (s.length() == 4) {

                    if (submit(s.toString())) {
                        finish();

                    } else {
                        wrongPasscode();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

    private boolean submit(String passcodeInserted) {
        Log.d(TAG, "submit() called with: " + "passcodeInserted = [" + passcodeInserted + "]");
        return mPasscode.equals(passcodeInserted);
    }


    private void wrongPasscode() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                digitsClickable(false);

                clearRadioButtons();

                showErrorRadioButtons(true);

                mLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.passcode_logo_error, getTheme()));
                mDescription.setTextColor(ContextCompat.getColor(PasscodeActivity.this, R.color.colorRed500));
                mDescription.setText(R.string.passcode_description_error);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                digitsClickable(true);

                showErrorRadioButtons(false);

                mLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.passcode_logo, getTheme()));
                mDescription.setTextColor(ContextCompat.getColor(PasscodeActivity.this, R.color.colorPrimary));
                mDescription.setText(R.string.passcode_description);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mEditTextPasscode.setText("");
        mRadioButtonLayout.startAnimation(shake);
    }


    public void buttonClicked(View view) {
        Log.d(TAG, "buttonClicked() called with: " + "view = [" + view + "]");

        String current = mEditTextPasscode.getText().toString();
        int id = view.getId();

        switch (id) {

            case R.id.button_1:
                current += "1";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_2:
                current += "2";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_3:
                current += "3";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_4:
                current += "4";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_5:
                current += "5";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_6:
                current += "6";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_7:
                current += "7";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_8:
                current += "8";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_9:
                current += "9";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_0:
                current += "0";
                mEditTextPasscode.setText(current);
                break;

            case R.id.button_backspace:
                if (current.length() > 0) {
                    current = current.substring(0, current.length() - 1);
                    mEditTextPasscode.setText(current);
                }
                break;

        }
    }


    private void digitsClickable(boolean enable) {

        mButtonOne.setClickable(enable);
        mButtonTwo.setClickable(enable);
        mButtonThree.setClickable(enable);
        mButtonFour.setClickable(enable);
        mButtonFive.setClickable(enable);
        mButtonSix.setClickable(enable);
        mButtonSeven.setClickable(enable);
        mButtonEight.setClickable(enable);
        mButtonNine.setClickable(enable);
        mButtonZero.setClickable(enable);
        mButtonBackspace.setClickable(enable);

    }


    private void clearRadioButtons() {

        mRadioButton1.setChecked(false);
        mRadioButton2.setChecked(false);
        mRadioButton3.setChecked(false);
        mRadioButton4.setChecked(false);

    }

    private void showErrorRadioButtons(boolean show) {

        if (show) {

            mRadioButton1.setVisibility(View.GONE);
            mRadioButton2.setVisibility(View.GONE);
            mRadioButton3.setVisibility(View.GONE);
            mRadioButton4.setVisibility(View.GONE);

            mRadioError1.setVisibility(View.VISIBLE);
            mRadioError2.setVisibility(View.VISIBLE);
            mRadioError3.setVisibility(View.VISIBLE);
            mRadioError4.setVisibility(View.VISIBLE);

        } else {

            mRadioButton1.setVisibility(View.VISIBLE);
            mRadioButton2.setVisibility(View.VISIBLE);
            mRadioButton3.setVisibility(View.VISIBLE);
            mRadioButton4.setVisibility(View.VISIBLE);

            mRadioError1.setVisibility(View.GONE);
            mRadioError2.setVisibility(View.GONE);
            mRadioError3.setVisibility(View.GONE);
            mRadioError4.setVisibility(View.GONE);
        }

    }
}