package com.example.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etname;
    private EditText etphone;
    private EditText etemail;

    private Button write;
    private Button read;
    private Button update;
    private Button remove;

    private TextView show;

    MyHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etname=(EditText)findViewById(R.id.et_name);
        etphone=(EditText)findViewById(R.id.et_mobile);
        etemail=(EditText)findViewById(R.id.et_email);

        write=(Button)findViewById(R.id.BT_write);
        read=(Button)findViewById(R.id.BT_read);
        update=(Button)findViewById(R.id.BT_update);
        remove=(Button)findViewById(R.id.BT_remove);
        write.setOnClickListener(this);
        read.setOnClickListener(this);
        update.setOnClickListener(this);
        remove.setOnClickListener(this);

        show=(TextView)findViewById(R.id.show);

        myHelper=new MyHelper(this);
    }

    @Override
    public void onClick(View v) {
        String name;
        String phone;
        String email;
        SQLiteDatabase db;
        ContentValues values;
        switch (v.getId()){
            case R.id.BT_write:
                name=etname.getText().toString();
                phone=etphone.getText().toString();
                email=etemail.getText().toString();
                db=myHelper.getReadableDatabase();
                values=new ContentValues();
                if(name.isEmpty()||phone.isEmpty()||email.isEmpty()){
                    Toast.makeText(this,"Input is empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    values.put("name",name);
                    values.put("phone",phone);
                    values.put("email",email);
                    db.insert("info",null,values);
                    Toast.makeText(this,"write success",Toast.LENGTH_SHORT).show();
                }
                db.close();
                break;
            case R.id.BT_read:
                db=myHelper.getWritableDatabase();
                Cursor cursor=db.query("info",null,null,null,null,null,null,null);
                if(cursor.getCount()==0){
                    Toast.makeText(this,"no data",Toast.LENGTH_SHORT).show();
                }
                else{
                    cursor.moveToFirst();
                    show.setText("name:"+cursor.getString(1)+"phone:"+cursor.getString(2)+"email:"+cursor.getString(3));
                }
               while (cursor.moveToNext()){
                   show.append("\n"+"name:"+cursor.getString(1)+"phone:"+cursor.getString(2)+"email:"+cursor.getString(3));
               }
               cursor.close();
               db.close();
                break;
            case R.id.BT_update:
                db=myHelper.getWritableDatabase();
                values=new ContentValues();
                values.put("phone",etphone.getText().toString());
                db.update("info",values,"name=?",new String[]{etname.getText().toString()});
                Toast.makeText(this,"update success",Toast.LENGTH_SHORT).show();
                db.close();
                break;
            case R.id.BT_remove:
                db=myHelper.getWritableDatabase();
                db.delete("info",null,null);
                Toast.makeText(this,"remove success",Toast.LENGTH_SHORT).show();
                db.close();
                show.setText("no records");
                break;
        }
    }
}
