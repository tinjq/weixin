package com.jdxx.weixin;

import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.NutLoading;

/**
 * Created by Administrator on 2019/02/01.
 */

@Modules(scanPackage = true)
@Ok("json")
@Fail("json")
@LoadingBy(NutLoading.class)
@IocBy(args = {
        "*js", "ioc/",
        "*anno", "com.jdxx.weixin",
        "*weixin",  // org.nutz.plugins.weixin.WeixinIocLoader
        "*tx"
})
@Localization("msg")
@Encoding(input = "UTF-8", output = "UTF-8")
@SetupBy(StartSetup.class)
public class MainModule {
}
