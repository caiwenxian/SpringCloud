package com.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

public class sampleModel {

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
