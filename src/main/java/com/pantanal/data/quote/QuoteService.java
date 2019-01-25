package com.pantanal.data.quote;

import com.pantanal.data.dao.QuoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuoteService {

    @Autowired
    private QuoteDao quoteDao;

    public List<Map> getQuoteData(){
        List data =  quoteDao.selectWeekSeriesQuote();
        return data;
    }

}
