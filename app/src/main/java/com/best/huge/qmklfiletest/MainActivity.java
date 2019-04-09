package com.best.huge.qmklfiletest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.github.promeg.pinyinhelper.Pinyin;
import com.gjiazhe.wavesidebar.WaveSideBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity";

    private RecyclerView fileRecyclerView;
    private List<FileItem> fileList;
    private FileAdapter fileAdapter;

    String cate="/";
    private PinyinComparator mComparator=new PinyinComparator();
    private LinearLayoutManager layoutManager;
    private TitleItemDecoration mDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //网络请求
        request("/");
    }

    private void request(final String catelogueGo){
        try{
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("http://120.77.32.233/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d(TAG, "request: 创建retrofit");

            PostRequest_interface request=retrofit.create(PostRequest_interface.class);
            Log.d(TAG, "request: 创建request");

            FileRequest fileRequest=new FileRequest(catelogueGo,"福州大学","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzM3ODE1OTYsImlhdCI6MTUzMzE3Njc5NiwidXNlcm5hbWUiOiIxODk1MDMzOTM2NiJ9.aE7uNVbjz17GRewtM0Sq9Sd53jspSq303FEth-_s3Jg");
            Call<Reception> call=request.getCall(fileRequest);
            Log.d(TAG, "request: getcall");

            call.enqueue(new Callback<Reception>(){
                //请求成功时回调
                @Override
                public void onResponse(Call<Reception> call, Response<Reception> response) {
                    fileList.clear();
                    Log.d(TAG, "onResponse: "+response.body().getCode());

                    for(String name:response.body().getData().keySet()){
                        String size=response.body().getData().get(name);
                        FileItem fileItem=new FileItem(name,size);
                        String namePinYin=Pinyin.toPinyin(name.charAt(0));
                        String firstLetter=namePinYin.substring(0,1).toUpperCase();
                        if (firstLetter.matches("[A-Z]")) {
                            fileItem.setLetters(firstLetter.toUpperCase());
                        } else {
                            fileItem.setLetters("#");
                        }
                        fileList.add(fileItem);
                    }
                    Collections.sort(fileList,mComparator);

                    fileAdapter.notifyDataSetChanged();
                    Log.d(TAG, "request: 请求成功"+catelogueGo);
                    cate=catelogueGo;
                }

                //请求失败时回调
                @Override
                public void onFailure(Call<Reception> call, Throwable throwable) {
                    Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
        }
    }

    public void initView(){
        fileRecyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(this);
        fileRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fileList=new ArrayList<>();
        SharedPreferencesUtils.setStoredMessage(getApplicationContext(),"send",cate);
        fileAdapter=new FileAdapter(fileList,getApplicationContext());
        fileRecyclerView.setAdapter(fileAdapter);

        WaveSideBar sideBar = (WaveSideBar) findViewById(R.id.side_bar);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                int position =fileAdapter.getPositionForSection(index.charAt(0));
                layoutManager.scrollToPositionWithOffset(position,0);
            }
        });

        Collections.sort(fileList,mComparator);

        mDecoration=new TitleItemDecoration(this,fileList);
        fileRecyclerView.addItemDecoration(mDecoration);
        fileRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));

        fileAdapter.setOnClickListener(new FileAdapter.OnClickListener(){
            public void onClick(View itemView,int position,String cate){
                request(cate);
            }
        });
    }

    private void fillData(Response<Reception> response) {
        for(String name:response.body().getData().keySet()){
            String size=response.body().getData().get(name);
            FileItem fileItem=new FileItem(name,size);
            String namePinYin=Pinyin.toPinyin(name.charAt(0));
            String firstLetter=namePinYin.substring(0,1).toUpperCase();
            if (firstLetter.matches("[A-Z]")) {
                fileItem.setLetters(firstLetter.toUpperCase());
            } else {
                fileItem.setLetters("#");
            }
            fileList.add(fileItem);
        }
    }

    @Override
    public void onBackPressed(){
        if(cate!="/"){
            cate=cate.substring(0,cate.lastIndexOf("/",cate.length()-2)+1);
            if(cate.length()==0){
                cate=cate.concat("/");
            }
            SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("SEND", Context.MODE_PRIVATE).edit();
            editor.putString("SEND",cate);
            editor.apply();
            request(cate);
        }else{
            super.onBackPressed();
        }
    }
}