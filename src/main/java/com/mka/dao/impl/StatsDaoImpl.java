/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao.impl;

import com.mka.dao.StatsDao;
import com.mka.model.StockTrace;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    public List<StockTrace> getStats() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(StockTrace.class);
            List<StockTrace> users = criteria.list();
            if (users.size() > 0) {
                return users;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getStats() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean updateStockTrace(StockTrace st) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(st);
            session.saveOrUpdate(st);

            tx.commit();
            session.flush();
            response = true;
        } catch (Exception e) {
            log.error("Exception in updateStockTrace() " + st.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return response;
    }

    @Override
    public int getAveragePricePerUnit(int itemId) {
        Session session = null;
        int avgPricePerUnit = 0;
        try {
            session = sessionFactory.openSession();
//            String sQuery = "select sum(de.quantity) / avg(de.rate) as avgPerUnit from entries_direct de "
//                    + "where de.is_active=1 and de.item=:itemId group by de.item";
            String sQuery = "select avg(de.rate) as avgPerUnit from entries_direct de "
                    + "where de.is_active=1 and de.item=:itemId group by de.item";
            Query hQuery = session.createSQLQuery(sQuery);
            hQuery.setParameter("itemId", itemId);
            List result = hQuery.list();

            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                BigDecimal row = (BigDecimal) iterator.next();
                log.info(row);
                avgPricePerUnit = row.intValue();
            }
        } catch (Exception e) {
            log.error("Exception in getAveragePricePerUnit() : ", e);
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return avgPricePerUnit;
    }

}
