package com.best.huge.qmklfiletest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.Collator;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity";

    private List<FileItem> fileList=new ArrayList<>();
    FileAdapter fileAdapter=new FileAdapter(fileList);
    String cate="/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView fileRecyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        fileRecyclerView.setLayoutManager(layoutManager);
        FileItem initfile=new FileItem("test","size");
        fileList.add(initfile);
        fileRecyclerView.setAdapter(fileAdapter);
        request("/");
        fileAdapter.setOnClickListener(new FileAdapter.OnClickListener() {
            public void onClick(View itemView,int position,String cate){
                request(cate);
            }
        });
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
                        fileList.add(fileItem);
                        listSortByName();
                    }
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

    @Override
    public void onBackPressed(){
        if(cate!="/"){
            cate=cate.substring(0,cate.lastIndexOf("/",cate.length()-2)+1);
            if(cate.length()==0){
                cate=cate.concat("/");
            }
            request(cate);
        }else{
            super.onBackPressed();
        }
    }

    public void  listSortByName(){
        Collections.sort(fileList, new Comparator<FileItem>() {
            @Override
            public int compare(FileItem o1,FileItem o2) {
                return Collator.getInstance(Locale.CHINESE).compare(o1.getName(),o2.getName());
            }
        });
    }
}