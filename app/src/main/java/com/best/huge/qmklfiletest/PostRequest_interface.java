package com.best.huge.qmklfiletest;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PostRequest_interface {
    @POST("qmkl1.0.0/file/list")
    Call<Reception> getCall(@Body FileRequest fileRequest);

}
