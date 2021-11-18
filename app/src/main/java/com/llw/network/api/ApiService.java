package com.llw.network.api;

import com.llw.network.bean.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;


/**
 * ApiService接口 统一管理应用所有的接口
 * @author llw
 */
public interface ApiService {

    /**
     * 获取热门壁纸列表
     */
    @GET("/v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    Observable<WallPaperResponse> getWallPaper();
}
