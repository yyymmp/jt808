package com.jt.server.mq;

import com.jt.server.message.MsgHeader;
import com.jt.server.message.req.LocationInfoUploadMsg;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 作为测试工具 往mq发送数据
 */
public class LocationProductTestUtil {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new SendTask(),0,5, TimeUnit.SECONDS);
    }


}
class SendTask implements Runnable{

    @Override
    public void run() {
        LocationInfoUploadMsg locationInfoUploadMsg = new LocationInfoUploadMsg();
        Random r = new Random();
        Map<String, Double> stringDoubleMap = randomLonLat(119.69, 120.75, 29.89, 30.72);
        locationInfoUploadMsg.setLatitude(stringDoubleMap.get("lat").floatValue());
        locationInfoUploadMsg.setLongitude(stringDoubleMap.get("lon").floatValue());
        locationInfoUploadMsg.setElevation(r.nextInt(10));
        locationInfoUploadMsg.setSpeed(r.nextInt(10));
        locationInfoUploadMsg.setDirection(r.nextInt(10));
        Date date = new Date();
        locationInfoUploadMsg.setTime(date);
        locationInfoUploadMsg.setWarningFlagField(0);
        locationInfoUploadMsg.setStatusField(0);
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setTerminalPhone("15755640439");
        locationInfoUploadMsg.setMsgHeader(msgHeader);
        LocationProduct.send(locationInfoUploadMsg);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(date)+" 发送完成");
    }

    /**
     * @Description: 在矩形内随机生成经纬度
     * @param MinLon：最小经度
     * 		  MaxLon： 最大经度
     *  	  MinLat：最小纬度
     * 		  MaxLat：最大纬度
     * @return @throws
     */
    public  Map<String, Double> randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        double lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        double lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Double> map = new HashMap<>();
        map.put("lon",lon);
        map.put("lat",lat);
        return map;
    }
}