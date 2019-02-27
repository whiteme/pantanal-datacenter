package com.pantanal.data.service.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.pantanal.data.dao.HouseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.dao.CommunityDao;
import com.pantanal.data.dao.QuoteDao;
import com.pantanal.data.entity.Community;
import com.pantanal.data.entity.RentHouse;
import com.pantanal.data.entity.RentQuoteEntity;
import com.pantanal.data.util.GeoUtil;
import com.pantanal.data.util.JsonLoader;

@Service
public class RentService {

    public  static final String DEFAULT_AGGR_HOUSE_DATE = "20000101" ;


    private static Map<String, Community> COMMUNITY_DATA = new HashMap<String, Community>();

    @Autowired
    private QuoteDao quoteDao;

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private HouseDao houseDao;

    private static Logger logger = LoggerFactory.getLogger(RentService.class);

    @PostConstruct
    public void LoadCommunityData() {
        List<Community> data = communityDao.select();
        for (Community c : data) {
            COMMUNITY_DATA.put(c.getCode(), c);
        }
    }

    public static final int QUOTE_TYPE_INTENRN = 1;

    public static final int QUOTE_TYPE_PUBLIC = 0;


    public Object[] combineRentHouseInfoQuote(List<RentHouse> rentHouseInfos) {
        Map<String, RentQuoteEntity> quoteData = new HashMap<String, RentQuoteEntity>();
        for (RentHouse house : rentHouseInfos) {
            if (house.getRentType().equals(RentHouse.RENT_TYPE_ENTIRE)) {
                Community community = locateCommunity(house);
                if (community != null) {
                    RentQuoteEntity quote = quoteData.get(community.getCode());
                    if (quote == null) {
                        quote = new RentQuoteEntity();
                        quote.setCode(community.getCode());
                        quote.setEntityName(community.getCommunityName());
                        quote.setDate(house.getExposureDay());
                        quoteData.put(quote.getCode(), quote);
                    }
                    aggrQuote(quote, house);
                }

            }
        }

        return quoteData.values().toArray();
    }


    public List<RentQuoteEntity> combineRentHouseInfoQuote2List(List<RentHouse> rentHouseInfos){
        return null;
    }





    public Community locateCommunity(RentHouse house) {
        String code = org.apache.commons.codec.digest.DigestUtils.md5Hex(house.getCityName() + "-" + house.getCommunityName());
        return COMMUNITY_DATA.get(code);
    }

    private void aggrQuote(RentQuoteEntity quote, RentHouse houseInfo) {

        quote.setVolume(quote.getVolume() + 1);
        if (quote.getHigh() < houseInfo.getPrice())
            quote.setHigh(houseInfo.getPrice());
        if (quote.getLow() > houseInfo.getPrice())
            quote.setLow(houseInfo.getPrice());

        double houseAvg = houseInfo.getPrice() / houseInfo.getSquare();
        if (quote.getHighSquareMeter() < houseAvg)
            quote.setHighSquareMeter(houseAvg);
        if (quote.getLowSquareMeter() > houseAvg)
            quote.setLowSquareMeter(houseAvg);

        double totalPrice = quote.getAvgSquareMeter() * quote.getTotalSquareMeter();
        quote.setAvgSquareMeter((totalPrice + houseInfo.getPrice()) / (quote.getTotalSquareMeter() + houseInfo.getSquare()));
        quote.setTotalSquareMeter(quote.getTotalSquareMeter() + houseInfo.getSquare());

    }

    public void handleRentHouseDaily(String date, String city, String source, String prefix) {
        String filename = "/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json";
        Object[] originalDatas = JsonLoader.loadJsonFile(filename);
        List<RentHouse> rhs = extractRentHouse(originalDatas, city);
        Object[] quotes = combineRentHouseInfoQuote(rhs);
        for (Object obj : quotes) {
            System.out.println(JSONObject.toJSONString(obj));
        }
    }

    public List<RentHouse> extractRentHouse(Object[] datas, String cityName) {
        List<RentHouse> ret = new ArrayList<RentHouse>();
        for (Object data : datas) {
            Map<String, List> m = (Map<String, List>) data;

            RentHouse h = new RentHouse();
            List price = m.get("price");
            if (!price.isEmpty())
                h.setPrice(new Double(price.get(0).toString()));

            List square = m.get("square");
            if (!square.isEmpty()) {
                String v = square.get(0).toString();
                if (v.indexOf("㎡") != -1) {
                    v = v.substring(0, v.length() - 1);
                }
                h.setSquare(new Integer(v));
            }

            List cl = m.get("community");
            if (!cl.isEmpty())
                h.setCommunityName(cl.get(0).toString());

            List ll = m.get("landmark");
            if (!ll.isEmpty())
                h.setLandmark(ll.get(0).toString());

            List dl = m.get("distirct");
            if (!dl.isEmpty())
                h.setDistrict(dl.get(0).toString());

            List latL = m.get("latitude");
            if (!latL.isEmpty()) {
                double latV = 0.0d;
                String latS = GeoUtil.getLongtitude(latL.get(0).toString());
                if (latS != null) {
                    latV = Double.parseDouble(latS);
                    h.setLatitude(latV);
                }
            }

            List longL = m.get("longtitude");
            if (!longL.isEmpty()) {
                double logV = 0.0d;
                String longS = GeoUtil.getLongtitude(longL.get(0).toString());
                if (longS != null) {
                    logV = Double.parseDouble(longS);
                    h.setLongtitude(logV);
                }
            }

            List rtList = m.get("rentType");
            if (!rtList.isEmpty()) {
                String rt = rtList.get(0).toString();
                if (rt.trim().equals("整租")) {
                    h.setRentType(RentHouse.RENT_TYPE_ENTIRE);
                } else {
                    h.setRentType(RentHouse.RENT_TYPE_PART);
                }

            }
            h.setCityName(cityName);

            ret.add(h);

        }
        return ret;
    }


    public void aggrPublicSourceRentInfo2Quote() {
        List<String> sources = getPublicRentInfoSources();
        Map<String , String> quoteLatestInfo = getLatestQuoteInfo(0);
        for (String source : sources) {
            String aggrBeginDate = DEFAULT_AGGR_HOUSE_DATE;
            if(quoteLatestInfo.containsKey(source)){
                aggrBeginDate = quoteLatestInfo.get(source);
            }

            List<RentHouse> rhs = getRentHouse(source , aggrBeginDate);
            Object[] quotes = combineRentHouseInfoQuote(rhs);
            savePublicQuote(quotes);

        }
    }

    private void savePublicQuote(Object[] quotes) {
    }


    private List<RentHouse> getRentHouse(String source , String date){
        String sql = "select * from house_raw where source=? and createdate > ?";
        return houseDao.selectHouseRaw(sql , new Object[]{ source  , date});
    }


    public Map<String, String> getLatestQuoteInfo(int type) {
        Map<String , String> data = new HashMap<String , String>();
        List<Map<String , String>> dbret = quoteDao.selectLatestQuoteInfo(QUOTE_TYPE_PUBLIC);

        for(Map m : dbret){
            data.putAll(m);
        }

        return data;

    }

    public List<String> getPublicRentInfoSources() {
        return quoteDao.selectHouseDataSources();
    }

    public static void main(String args[]) {
        RentService service = new RentService();
        service.handleRentHouseDaily("", ":", "", "");
    }

}
