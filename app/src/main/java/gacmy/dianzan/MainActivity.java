package gacmy.dianzan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    LikeView likeView;
    EditText et_number;
    Button bt_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        likeView = (LikeView)findViewById(R.id.likeview);
        bt_test =(Button)findViewById(R.id.bt_test);
        et_number = (EditText)findViewById(R.id.et_number);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long number = 0;
                if(!TextUtils.isEmpty(et_number.getText().toString())){
                    number = Long.parseLong(et_number.getText().toString());
                }
                likeView.setNumber(number,false);
            }
        });
    }
}
