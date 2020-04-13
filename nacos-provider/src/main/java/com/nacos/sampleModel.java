package com.nacos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("sample实体")
public class sampleModel {

    @ApiModelProperty("是否使用本地缓存")
//    @Value("${useLocalCache}")
    public boolean useLocalCache;

//    @Value("${CALogin}")
    private boolean CALogin;

    public sampleModel() {
    }

    public sampleModel(boolean useLocalCache, boolean CALogin) {
        this.useLocalCache = useLocalCache;
        this.CALogin = CALogin;
    }

    public boolean isUseLocalCache() {
        return useLocalCache;
    }

    public void setUseLocalCache(boolean useLocalCache) {
        this.useLocalCache = useLocalCache;
    }

    public boolean isCALogin() {
        return CALogin;
    }

    public void setCALogin(boolean CALogin) {
        this.CALogin = CALogin;
    }
}
