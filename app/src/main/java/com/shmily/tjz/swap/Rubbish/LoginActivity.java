package com.shmily.tjz.swap.Rubbish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.shmily.tjz.swap.MainActivity;
import com.shmily.tjz.swap.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LoginActivity extends AppCompatActivity {
    private MaterialEditText upass,uname;
    private Button button;
    TextView text;
    String name,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname= (MaterialEditText) findViewById(R.id.editText1);
        upass= (MaterialEditText) findViewById(R.id.editText2);
        button= (Button) findViewById(R.id.loginbutton);
        text= (TextView) findViewById(R.id.registerbutton);

        init();
    }

    private void init() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=uname.getText().toString().trim();
                pass=upass.getText().toString().trim();
                RequestParams params=new RequestParams("http://120.25.96.231/ForAndroidHttp/sign.action");
                params.addBodyParameter("uname",name);
                params.addBodyParameter("upass",pass);

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)){
                    x.http().post(params, new Callback.CacheCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject json = new JSONObject(result);
                                String results = json.getString("result");

                                if (results.equals("1")) {
                                    Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                    SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=prefs.edit();
                                    editor.putString("username",name);
                                    editor.putBoolean("denglu",true);
                                    editor.commit();
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    intent.putExtra("username",name);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(LoginActivity.this, "请您先连接网络！", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                        }

                        @Override
                        public void onFinished() {

                        }

                        @Override
                        public boolean onCache(String result) {
                            return false;
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "请输入正确的账号或密码！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

}
