package com.jdxx.weixin.service;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxEventType;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMsgType;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

/**
 * Created by Administrator on 2019/1/30.
 */
@IocBean(create = "init", name = "wxHandler")
public class DefaultWxHandler extends BasicWxHandler {

    private static final Log log = Logs.get();

    @Inject
    protected PropertiesProxy conf; // 注入配置信息加载类

    public void init() {
        //将读取 weixin.token / weixin.aes / weixin.appid, 他们通常会写在weixin.properties或从数据库读取.
        configure(conf, "weixin.");

        // 如果你不知道conf是啥, 完全搞不清楚状况,
        // 请将protected PropertiesProxy conf注释掉,configure也注释掉
        // 把下面这行取消注释.
        // token = "weixin_tin";
    }

    /**
     * 根据不同的消息类型,调用WxHandler不同的方法
     */
    public static WxOutMsg handle(WxInMsg msg, WxHandler handler) {
        WxOutMsg out = null;
        switch (WxMsgType.valueOf(msg.getMsgType())) {
            case text:
                out = handler.text(msg);
                break;
            case image:
                out = handler.image(msg);
                break;
            case voice:
                out = handler.voice(msg);
                break;
            case video:
                out = handler.video(msg);
                break;
            case location:
                out = handler.location(msg);
                break;
            case link:
                out = handler.link(msg);
                break;
            case event:
                out = handleEvent(msg, handler);
                break;
            case shortvideo:
                out = handler.shortvideo(msg);
                break;
            default:
                log.infof("New MsyType=%s ? fallback to defaultMsg", msg.getMsgType());
                out = handler.defaultMsg(msg);
                break;
        }
        return out;
    }

    /**
     * TODO
     */
    public WxOutMsg text(WxInMsg msg) {
        if ("zx".equalsIgnoreCase(msg.getContent().trim())) {
            return Wxs.respText(null, "注销成功!");
        }
        // return defaultMsg(msg);
        return Wxs.respText(null, "注销成功!");
    }

    /**
     * TODO
     * 根据msg中Event的类型,调用不同的WxHandler方法
     */
    public static WxOutMsg handleEvent(WxInMsg msg, WxHandler handler) {
        WxOutMsg out = null;
        switch (WxEventType.valueOf(msg.getEvent())) {
            case subscribe:
                out = handler.eventSubscribe(msg);
                break;
            case unsubscribe:
                out = handler.eventUnsubscribe(msg);
                break;
            case LOCATION:
                out = handler.eventLocation(msg);
                break;
            case SCAN:
                out = handler.eventScan(msg);
                break;
            case CLICK:
                out = handler.eventClick(msg);
                break;
            case VIEW:
                out = handler.eventView(msg);
                break;
            case TEMPLATESENDJOBFINISH:
                out = handler.eventTemplateJobFinish(msg);
                break;
            default:
                log.infof("New EventType=%s ? fallback to defaultMsg", msg.getMsgType());
                out = handler.defaultMsg(msg);
                break;
        }
        return out;
    }

}
