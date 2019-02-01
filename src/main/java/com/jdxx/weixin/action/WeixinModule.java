package com.jdxx.weixin.action;

import com.jdxx.weixin.service.WxService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Administrator on 2019/1/29.
 */
@IocBean
@At("/weixin")
public class WeixinModule {

    private static final Log log = Logs.get();

    @Inject
    protected WxHandler wxHandler;
    @Inject
    private WxService wxService;

    @At
    public View msgin(HttpServletRequest req) throws IOException {
        return Wxs.handle(wxHandler, req, "default");
    }

    /**
     * 创建菜单
     * @param request
     * @return
     */
    @At("/menu/create")
    @Ok("raw")
    public String createMenu(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        WxResp resp = wxService.createMenu(baseUrl);
        if (resp.ok()) {
            if (log.isDebugEnabled())
                log.debugf("menu = %s", Json.toJson(resp.get("menu")));

            return "菜单创建成功!";
        } else {
            log.info("something happen : " + Json.toJson(resp));
            return "菜单创建失败!";
        }
    }

    /**
     * 微信授权
     * @param button
     * @param request
     * @return
     */
    @At("/authorize")
    @Ok(">>:${obj}")
    public String authorize(@Param("button") String button, HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        String action = "/weixin";
        if (!Strings.isBlank(button)) {
            if ("order".equals(button)) {
                action += "/order/create.do";
            }
        }
        String redirect_uri = baseUrl + action;
        // snsapi_base 静默登录, snsapi_userinfo 需要确认, 后者才能获取用户详细信息
        return wxService.wxLogin().authorize(redirect_uri, "snsapi_base", null);
    }

    @At
    public void getMenu() {
        wxService.getMenu();
    }

}
