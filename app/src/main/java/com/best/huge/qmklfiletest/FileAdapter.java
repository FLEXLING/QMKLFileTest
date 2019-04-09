package com.best.huge.qmklfiletest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{

    private OnClickListener onClickListener;
    private String path;
    private Context context;


    interface OnClickListener{
        void onClick(View itemView,int i,String cate);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    private List<FileItem> mFileItemList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        TextView fileSize;
        public ViewHolder(View view){
            super(view);
            fileName=(TextView)view.findViewById(R.id.name);
            fileSize=(TextView)view.findViewById(R.id.size);
        }

    }

    public FileAdapter(List<FileItem> fileItemList,Context context){
        mFileItemList=fileItemList;
        this.context = context;
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i){
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,int i){
        final FileItem file=mFileItemList.get(i);
        viewHolder.fileName.setText(file.getName());
        viewHolder.fileSize.setText(file.getSize());
        final View itemView=viewHolder.itemView;
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                path=SharedPreferencesUtils.getStoredMessage(context,"send");
                path=path.concat(file.getName()+"/");
                final String finalPath=path;
                onClickListener.onClick(itemView,viewHolder.getLayoutPosition(),finalPath);
            }
        });
    }

    @Override
    public int getItemCount(){
        return mFileItemList.size();
    }

    public int getSectionForPosition(int position){
        return mFileItemList.get(position).getLetters().charAt(0);
    }

    public int getPositionForSection(int section){
        for(int i=0;i<getItemCount();i++){
            String sortStr=mFileItemList.get(i).getLetters();
            char firstChar=sortStr.toUpperCase().charAt(0);
            if(firstChar==section){
                return i;
            }
        }
        return -1;
    }
}
