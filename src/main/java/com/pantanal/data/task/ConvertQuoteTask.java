package com.pantanal.data.task;

import com.pantanal.data.config.SpringUtils;
import com.pantanal.data.dao.SmartDAO;
import com.pantanal.data.entity.RentHouse;
import com.pantanal.data.entity.RentQuoteEntity;
import com.pantanal.data.service.house.RentService;
import com.pantanal.data.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 *
 * 合并任务开始前 需要对任务起始天为条件 对行情数据预清理 以防止脏数据
 *
 * 从数据库查询数据时 按照 维度条件 + 时序条件（天）迭代 进行批次读取
 *
 * 并以此数据集进行合并逻辑
 *
 *
 */
public class ConvertQuoteTask extends BaseTask {


    private SmartDAO smartDAO;

    private String source;

    private String beginDate;

    private int batchSize = 1000;

    private String SQL_DATA_TEMPLATE = "select * from house_raw where source=? and createdate=? and city=?";

    private String SQL_COUNT_TEMPLATE = "select count(1) from house_raw where source=? and createdate>?";

    private String SQL_DIM_TEMPLATE  = "select distinct(city) from house_raw where source=? ";

    private static String SQL_DEFAULT_DIM = "city";

    private static Logger logger = LoggerFactory.getLogger(ConvertQuoteTask.class);



    public ConvertQuoteTask(Map param, Properties conf ) {
        super(param, conf);
        this.smartDAO = (SmartDAO) SpringUtils.getBean("smartDAO");
        this.source = param.get("source").toString();
        this.beginDate = param.get("beginDate").toString();
    }

    @Override
    public void run() {
        try {
            convertQuote();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convertQuote() throws Exception {
        purgeDataBaseDirtyQuote();
        List<String> dimVals = getDimensionVal();
        String currentDate = DateUtil.toString(DateUtil.getCurrentDate() , DateUtil.DATE_FMT_6);

        logger.info(" {}  维度数量 {}  , {} ==" , this.source , dimVals.size() , dimVals );
        for(String dimValue : dimVals){
            String cursorDate = DateUtil.addDays(this.beginDate , 1);
            while(currentDate.compareTo(cursorDate) > 0){
                List<RentHouse> rhs = loadSliceJobData(dimValue , cursorDate);
//                logger.info("查询数据 数据源 {} =  维度: {} = 日期 = {} = 数量 {} = " ,this.source , dimValue , cursorDate , rhs.size() );
                if(rhs != null && !rhs.isEmpty()){
                    List<RentQuoteEntity> q = convert(rhs);
                    logger.info("数据写入 ： 数据源 {} =  维度: {} = 日期 = {} = 数量 {} = " ,this.source , dimValue , cursorDate , q.size() );
                    save(q);
                }
                cursorDate = DateUtil.addDays(cursorDate , 1);
            }
        }


    }

    private List<RentHouse> loadSliceJobData(String dimValue, String cursorDate) throws Exception {
        List<RentHouse> ret = new ArrayList();
        ret = smartDAO.getList(RentHouse.class , SQL_DATA_TEMPLATE , new Object[]{ this.source , cursorDate , dimValue} );
        return ret;
    }

    /**
     *  脏数据清理任务
     */
    private void purgeDataBaseDirtyQuote() {
    }


    /**
     * 持久化
     * @param quotes
     */
    private void save(List<RentQuoteEntity> quotes) throws Exception {

        String insertSQL = "insert into quote_public values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        logger.info("Aggr Quote Sample {}" , JSONObject.valueToString(quotes));
        if(!quotes.isEmpty()){
            List<Object[]> batchArgs = new ArrayList();
            for(RentQuoteEntity q : quotes){
                batchArgs.add(new Object[]{
                        q.getCode(),
                        q.getEntityName(),
                        q.getCity(),
                        q.getDistrict(),
                        q.getHigh(),
                        q.getLow(),
                        q.getHighSquareMeter(),
                        q.getAvgSquareMeter(),
                        q.getLowSquareMeter(),
                        q.getVolume(),
                        q.getDate(),
                        1,
                        this.source,
                        0
                });
            }
            smartDAO.batchUpdate("xuwu" , insertSQL , batchArgs);

        }


//

    }


    /**
     * 房源数据 聚合 小区行情 核心算法
     * @param rhs
     * @return
     */
    private List<RentQuoteEntity> convert(List<RentHouse> rhs) {
        RentService rentService = (RentService) SpringUtils.getBean("rentService");
        List<RentQuoteEntity> ret =  rentService.combineRentHouseInfoQuote2List(rhs);
        logger.info(" {} 本次聚合的数据 {} 条 == , 聚合行情数据 {} ==" , this.source , rhs.size() , ret.size());
        return ret;

    }

    /**
     * 废弃 不以数量迭代
     * @return
     */
    @Deprecated
    private int getTaskCount(){
        int ret = Integer.MIN_VALUE ;
        try {
           ret =  smartDAO.getCount(SQL_COUNT_TEMPLATE , new Object[]{this.source , this.beginDate});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     *  获得维度数据 默认迭代维度 city
     * @return
     */
    private List<String> getDimensionVal() throws Exception {
        List<String> ret = new ArrayList<String>();
        JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtils.getBean("xuwuJdbcTemplate");
//        ret = smartDAO.getList(String.class , SQL_DIM_TEMPLATE , new Object[]{ this.source , this.beginDate });
        ret = jdbcTemplate.query(SQL_DIM_TEMPLATE , new Object[]{ this.source  } , new RowMapper<String>(){

            @Nullable
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        return ret;
    }


    /**
     *  批量数据加载
     * @param offset
     * @return
     */
    private List<RentHouse> getRentHouse(int offset){
        List<RentHouse> rhs = new ArrayList<RentHouse>();
        try {
            rhs = smartDAO.getList(RentHouse.class , SQL_DATA_TEMPLATE , new Object[]{this.source , this.beginDate , this.batchSize , offset});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rhs;
    }


    public static void main(String args[]){
        String currentDate = DateUtil.toString(DateUtil.getCurrentDate() , DateUtil.DATE_FMT_3);
        String cursorDate = DateUtil.addDays("20010101" , 1);
        System.out.println(currentDate + " ========== " + cursorDate);
    }

}
