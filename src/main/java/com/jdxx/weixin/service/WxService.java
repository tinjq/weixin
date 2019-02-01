package com.jdxx.weixin.service;

import com.jdxx.weixin.menu.Button;
import com.jdxx.weixin.menu.ClickButton;
import com.jdxx.weixin.menu.ComplexButton;
import com.jdxx.weixin.menu.ViewButton;
import com.jdxx.weixin.utils.WxConsts;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.weixin.at.impl.DaoAccessTokenStore;
import org.nutz.weixin.bean.WxMenu;
import org.nutz.weixin.impl.WxLoginImpl;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxLogin;
import org.nutz.weixin.spi.WxResp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/30.
 */
@IocBean
public class WxService {

    private static final Log log = Logs.get();

    @Inject
    protected PropertiesProxy conf;

    public WxApi2 getWxApi2() {
        WxApi2 wxApi2 = Mvcs.getIoc().get(WxApi2.class);
        DaoAccessTokenStore daoAccessTokenStore = ((DaoAccessTokenStore) wxApi2.getAccessTokenStore());
        daoAccessTokenStore.setParams(new NutMap("id", "wx_access_token"));
        return wxApi2;
    }

    /**
     * 创建菜单
     *
     * @param baseUrl
     * @return
     */
    public WxResp createMenu(String baseUrl) {
        List<WxMenu> menuList = getMenuJson(baseUrl);
        return getWxApi2().menu_create(menuList);
    }

    public List<WxMenu> getMenuJson(String baseUrl) {
        ClickButton btn11 = new ClickButton(WxConsts.BUTTON_CLICK, "签到", "checkin");
        ClickButton btn12 = new ClickButton(WxConsts.BUTTON_CLICK, "签退", "checkout");
        ComplexButton mainBtn1 = new ComplexButton("考勤", new Button[]{btn11, btn12});

        ViewButton btn21 = new ViewButton(WxConsts.BUTTON_VIEW, "爱企秀", baseUrl + "/iqx/home.do");
        ViewButton btn22 = new ViewButton(WxConsts.BUTTON_VIEW, "预约", baseUrl + "/weixin/authorize.do?button=order");
        ViewButton btn23 = new ViewButton(WxConsts.BUTTON_VIEW, "网页授权", baseUrl + "/weixin/OAuth/snsapiUserinfo.do");
        ComplexButton mainBtn2 = new ComplexButton("阅读", new Button[]{btn21, btn22, btn23});

        ViewButton btn31 = new ViewButton(WxConsts.BUTTON_VIEW, "我的", "https://www.baidu.com");

        List<WxMenu> list = new ArrayList<WxMenu>();
        list.add(mainBtn1);
        list.add(mainBtn2);
        list.add(btn31);

        return list;
    }

    public void getMenu() {
        WxResp resp = getWxApi2().menu_get();
        if (resp.ok()) {
            if (log.isDebugEnabled())
                log.debugf("menu = %s", Json.toJson(resp.get("menu")));
        } else {
            log.info("something happen : " + Json.toJson(resp));
        }
    }

    public WxLogin wxLogin() {
        WxLoginImpl w = new WxLoginImpl();
        return w.configure(conf, "weixin.");
    }

    /**
     * 通过微信网页授权获取用户openId
     * @param code
     * @return
     */
    public String getOpenId(String code) {
        WxResp wxResp = wxLogin().access_token(code);
        log.info("-------->WxService#getOpenId:" + Json.toJson(wxResp, JsonFormat.compact()));
        return wxResp.getString("openid");
    }

}
