package com.app.pipelinesurvey.view.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.bean.AccountInfo;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.iview.IAccountInfoView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountInfoActivity extends BaseActivity implements View.OnClickListener, IAccountInfoView, RadioGroup.OnCheckedChangeListener {
    private TextView tvReturn;
    private LinearLayout linearReturn;
    private TextView tvTitle;
    private TextView tvSubmit;
    private EditText edtAccountName;
    private EditText edtAccountPhone;
    private EditText edtCompany;
    private EditText edtDepartment;
    private EditText edtWorkGroup;
    private RadioGroup radioGroup;
    private RadioButton rdbtnMale;
    private RadioButton rdbtnFeMale;
    private RadioButton rdbtnUnknow;
    private String sex = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        initView();
    }

    private void initView() {
        tvReturn = (TextView) findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(this);
        linearReturn = (LinearLayout) findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("个人信息");
        tvSubmit.setText("修改");
        edtAccountName = (EditText) findViewById(R.id.edtAccountName);
        edtAccountName.setFilters(new InputFilter[]{m_inputFilter, new InputFilter.LengthFilter(4)});
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        edtAccountPhone = (EditText) findViewById(R.id.edtAccountPhone);
        edtAccountPhone.setFilters(new InputFilter[]{m_inputFilter2,new InputFilter.LengthFilter(11)});
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtDepartment = (EditText) findViewById(R.id.edtDepartment);
        edtWorkGroup = (EditText) findViewById(R.id.edtWorkGroup);
        rdbtnMale = (RadioButton) findViewById(R.id.radioSexMale);
        rdbtnFeMale = (RadioButton) findViewById(R.id.radioSexFeMale);
        rdbtnUnknow = (RadioButton) findViewById(R.id.radioSexUnknow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearReturn:
                finish();
                break;
            case R.id.tvSubmit:
                if (tvSubmit.getText().toString().equals("修改")) {
                    tvSubmit.setText("完成");
                    setEdittextEnable();
                } else {
                    boolean isNameOk = checkName();
                    boolean isPhoneOk = checkPhone();
                    boolean isNameExist = isNameExist();
                    if (isNameOk && isPhoneOk && isNameExist) {
                        ContentValues _values = new ContentValues();
                        _values.put("name", getName());
                        _values.put("sex", getSex());
                        _values.put("dept", getDepartment());
                        _values.put("workgroup", getWorkGroup());
                        _values.put("mobile", getPhone());
                        _values.put("company", getCompany());
                        DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_USER_INFO, _values);
                        ToastUtil.showShort(this, "个人信息已保存");
                        AccountInfo _accountInfo = new AccountInfo();
                        _accountInfo.setName(getName());
                        _accountInfo.setSex(getSex());
                        _accountInfo.setPhone(getPhone());
                        _accountInfo.setCompany(getCompany());
                        _accountInfo.setDepartment(getDepartment());
                        _accountInfo.setWorkGroup(getWorkGroup());
                        AccountInfo.setAccountInfo(_accountInfo);
                        tvSubmit.setText("修改");
                        setEdittextEnable();
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean isNameExist() {
        Cursor _cursor = DatabaseHelpler.getInstance().rawQuery("select * from " +
                SQLConfig.TABLE_NAME_USER_INFO + " where name = ?", new String[]{getName()});
        if (_cursor.moveToNext()) {
            ToastUtil.showShortByQueue(this, "该名字已被占用，请重新输入");
            return false;
        } else {
            return true;
        }
    }

    private void setEdittextEnable() {
        if (edtAccountName.isEnabled())
            edtAccountName.setEnabled(false);
        else
            edtAccountName.setEnabled(true);

        if (rdbtnMale.isEnabled())
            rdbtnMale.setEnabled(false);
        else
            rdbtnMale.setEnabled(true);

        if (rdbtnFeMale.isEnabled())
            rdbtnFeMale.setEnabled(false);
        else
            rdbtnFeMale.setEnabled(true);

        if (rdbtnUnknow.isEnabled())
            rdbtnUnknow.setEnabled(false);
        else
            rdbtnUnknow.setEnabled(true);

        if (edtAccountPhone.isEnabled())
            edtAccountPhone.setEnabled(false);
        else
            edtAccountPhone.setEnabled(true);

        if (edtCompany.isEnabled())
            edtCompany.setEnabled(false);
        else
            edtCompany.setEnabled(true);

        if (edtDepartment.isEnabled())
            edtDepartment.setEnabled(false);
        else
            edtDepartment.setEnabled(true);

        if (edtWorkGroup.isEnabled())
            edtWorkGroup.setEnabled(false);
        else
            edtWorkGroup.setEnabled(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton _radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        sex = _radioButton.getText().toString();
    }

    InputFilter m_inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!isChinese(source.charAt(i))) {
                    ToastUtil.showShort(AccountInfoActivity.this, "仅支持最长4位中文字符");
                    return "";
                }
            }
            return null;
        }
    };
    InputFilter m_inputFilter2 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return null;
        }
    };

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    //验证电话号码
    public boolean checkPhone() {
        Pattern _pattern = Pattern.compile("[0-9]*");
        Matcher _matcher = _pattern.matcher(getPhone());
        if (_matcher.matches() && getPhone().length() == 11) {
            return true;
        } else {
            ToastUtil.showShortByQueue(this, "请输入11位数字的电话号码");
            return false;
        }
    }

    //验证姓名
    public boolean checkName() {
        if (getName().length() == 0) {
            ToastUtil.showShortByQueue(this, "请正确填写姓名");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public String getName() {
        return edtAccountName.getText().toString();
    }

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public String getPhone() {
        return edtAccountPhone.getText().toString();
    }

    @Override
    public String getCompany() {
        return edtCompany.getText().toString();
    }

    @Override
    public String getDepartment() {
        return edtDepartment.getText().toString();
    }

    @Override
    public String getWorkGroup() {
        return edtWorkGroup.getText().toString();
    }
}
