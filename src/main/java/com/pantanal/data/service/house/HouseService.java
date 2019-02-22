package com.pantanal.data.service.house;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pantanal.data.dao.HouseDao;
import com.pantanal.data.dao.ImportLogDao;
import com.pantanal.data.entity.House;
import com.pantanal.data.entity.ImportLog;
import com.pantanal.data.util.DateUtil;
import com.pantanal.data.util.StringUtil;

@Service
public class HouseService {


    private static Logger logger = LoggerFactory.getLogger(RentService.class);
    @Autowired
    private HouseDao houseDao;

    @Autowired
    private JdbcTemplate xuwuTemplate;

    @Autowired
    private ImportLogDao importLogDao;

    private static ThreadPoolExecutor HOUSE_EXECUTOR = new ThreadPoolExecutor(4, 16, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());


    public int importFromFile(File file) {
        if (importLogDao.getByFileName(file.getName()) != null) {
            logger.info("=====importFromFile ignored, because file: " + file.getName() + " has been imported!");
            return -1;
        }

        Set<String> communitySet = new HashSet<String>();
        ObjectMapper mapper = new ObjectMapper();
        int index = 0;
        try {
            ArrayNode arrayNode = (ArrayNode) mapper.readTree(file);
            House house;

            String tmp;
            String[] tmps;
            tmp = StringUtils.remove(file.getName(), ".json");
            tmp = StringUtils.remove(tmp, ".JSON");
            tmps = StringUtils.split(tmp, "-");
            String source = tmps[0];
            String city = tmps[1];
            String day = tmps[2];
            Date createTime = DateUtil.toDate(day, "yyyyMMdd");
            ArrayNode tmpNode;
            List<House> houseList = new ArrayList<House>();
            int total = arrayNode.size();
            for (JsonNode jsonNode : arrayNode) {
                index++;
                house = new House();

                tmpNode = (ArrayNode) jsonNode.get("price");
                if (tmpNode != null) {
                    house.setPrice(tmpNode.get(0).asInt());
                }

                tmpNode = (ArrayNode) jsonNode.get("rentType");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    if ("整租".equals(tmp)) {
                        house.setRentType(House.RENTTYPE_ALL);
                    } else {
                        house.setRentType(House.RENTTYPE_UNKNOWN);
                    }
                }

                tmpNode = (ArrayNode) jsonNode.get("layout");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    Double[] ds = StringUtil.getDoubleArray(tmp);
                    if (ds.length > 0) {
                        house.setRoom(ds[0].intValue());
                    } else if (ds.length > 1) {
                        house.setHall(ds[1].intValue());
                    } else if (ds.length > 2) {
                        house.setToilet(ds[2].intValue());
                    }
                }

                tmpNode = (ArrayNode) jsonNode.get("square");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setSize(StringUtil.getInt(tmp));
                }

                tmpNode = (ArrayNode) jsonNode.get("orientation");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setOrien(tmp.replace("朝", "-").replaceAll("\\s+", "-"));
                }

                tmpNode = (ArrayNode) jsonNode.get("subway");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    tmps = tmp.split("\\s+\\-\\s+");
                    house.setSubway(tmps[0]);
                    house.setSubwayStation(tmps[1]);
                }

                tmpNode = (ArrayNode) jsonNode.get("community");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setName(tmp);
                }

                tmpNode = (ArrayNode) jsonNode.get("latitude");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setLatitude(StringUtil.getDouble(tmp));
                }

                tmpNode = (ArrayNode) jsonNode.get("longtitude");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setLongtitude(StringUtil.getDouble(tmp));
                }

                tmpNode = (ArrayNode) jsonNode.get("exposureDay");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setExposureDate(DateUtil.toDate(tmp, "yyyy-MM-dd"));
                }

                tmpNode = (ArrayNode) jsonNode.get("floor");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    tmps = tmp.split("/");
                    if ("高楼层".equals(tmps[0])) {
                        house.setFloorLevel(House.FLOORLEVEL_HIGH);
                    } else if ("中楼层".equals(tmps[0])) {
                        house.setFloorLevel(House.FLOORLEVEL_MIDDLE);
                    } else if ("低楼层".equals(tmps[0])) {
                        house.setFloorLevel(House.FLOORLEVEL_LOW);
                    } else {
                        if (StringUtil.getInt(tmps[0]) == null) {
                            house.setFloorLevel(House.FLOORLEVEL_UNKNOWN);
                        } else {
                            house.setFloorLevel(StringUtil.getInt(tmps[0]));
                        }
                    }
                    if (tmps.length > 1) {
                        house.setMaxFloor(StringUtil.getInt(tmps[1]));
                    }
                }

                tmpNode = (ArrayNode) jsonNode.get("elevator");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    if ("有".equals(tmp)) {
                        house.setElevator(true);
                    } else if ("无".equals(tmp)) {
                        house.setElevator(false);
                    }
                }

                tmpNode = (ArrayNode) jsonNode.get("distirct");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setDistirct(tmp);
                }

                tmpNode = (ArrayNode) jsonNode.get("landmark");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setLandmark(tmp);
                }

                tmpNode = (ArrayNode) jsonNode.get("visitCnt");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    house.setVisitCnt(StringUtil.getInt(tmp));
                }

                tmpNode = (ArrayNode) jsonNode.get("city");
                if (tmpNode != null) {
                    tmp = tmpNode.get(0).asText();
                    city = tmp;
                }

                house.setCreateDate(createTime);
                house.setCity(city);
                house.setSource(source);

                houseList.add(house);
                if (index >= 5000 && (index % 5000 == 0)) {
                    houseDao.save(houseList);
                    houseList.clear();
                    logger.info("=====file:" + file.getName() + " has imported " + index + " lines. total:" + total);
                }

            }

            if (houseList.size() > 0) {
                houseDao.save(houseList);
                houseList.clear();
                logger.info("=====file:" + file.getName() + " has imported " + index + " lines. total:" + total);
            }


            // 保存日志
            ImportLog importLog = new ImportLog();
            importLog.setFileName(file.getName());
            importLog.setCity(city);
            importLog.setSource(source);
            importLog.setDay(NumberUtils.toInt(day));
            importLog.setCreateDate(new Date());
            importLog.setFileTime(new Date(file.lastModified()));
            importLog.setTotal(index);
            importLogDao.save(importLog);
            // 移动文件
            FileUtils.moveFileToDirectory(file, new File("/opt/xuwu/housedone"), true);
            logger.info("=====importFromFile done! file: " + file.getName() + " imported " + index + " houses");
            return index;
        } catch (Exception e) {
            String log = "=====importFromFile error! file: " + file.getName() + " about " + index + " line";
            logger.error(log, e);
            throw new RuntimeException(log, e);
        }
    }


    private void importRentInfoData(File file){

        ObjectMapper om = new ObjectMapper();
        try {
            List data = om.readValue(file , ArrayList.class);
            System.out.println(1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String[] getFields(String db, String table) {
        return xuwuTemplate.queryForList("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE table_name = ? AND table_schema = ?", new Object[]{table, db}, String.class).toArray(new String[]{});
    }


    public static void main(String args[]){
        File f = new File("/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json ");
    }
}
