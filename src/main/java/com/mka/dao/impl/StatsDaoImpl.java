/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao.impl;

import com.mka.dao.StatsDao;
import com.mka.model.response.StatItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sagher Mehmood
 */
@Repository
public class StatsDaoImpl implements StatsDao {

    private static final Logger log = Logger.getLogger(StatsDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<StatItem> getStats() {
        Session session = null;
        List<StatItem> statItems = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            String sQuery = "select date_format(e.entry_date,'%Y-%m') as currentMonth,e.item,e.sub_entry_type as subEntryType,"
                    + "avg(e.rate) averageRate,sum(e.total_price) as totalValue from entries_direct e "
                    + "where e.is_active=1 and date_format(e.entry_date,'%Y-%m')=date_format(curdate(), '%Y-%m') group by e.item,e.sub_entry_type";
            Query hQuery = session.createSQLQuery(sQuery);
            List result = hQuery.list();

            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                Object[] row = (Object[]) iterator.next();
                log.info(row[0] + ": " + row[1] + ", " + row[2] + " , " + row[3] + ", " + row[4] + ", " + row[5]);
                StatItem item = new StatItem(row[1], row[2], row[3], row[4], row[5]);

                statItems.add(item);
            }
        } catch (Exception e) {
            log.error("Exception in getStats() : ", e);
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return statItems;
    }

}
