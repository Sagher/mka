/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao.impl;

import com.mka.dao.StatsDao;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import com.mka.utils.Constants;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
            criteria.add(Restrictions.eq("month", Constants.MONTH_FORMAT.format(new Date())));
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

    public boolean insertStockTraceForNewMonth(StockTrace stat) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.save(stat);

            tx.commit();
            session.flush();
            response = true;
        } catch (Exception e) {
            log.error("Exception in logCashTransaction() " + stat.toString(), e);
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
    public BigDecimal getAveragePricePerUnit(int itemId) {
        Session session = null;
        BigDecimal avgPricePerUnit = BigDecimal.ZERO;
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
                avgPricePerUnit = row;
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

    @Override
    public MasterAccount getMasterAccount() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(MasterAccount.class);
            List<MasterAccount> users = criteria.list();
            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getMasterAccount() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean logCashTransaction(MasterAccountHistory mah) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            mah.setTimestamp(new Date());
            session.save(mah);

            tx.commit();
            session.flush();
            response = true;
        } catch (Exception e) {
            log.error("Exception in logCashTransaction() " + mah.toString(), e);
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
    public boolean updateMasterAccount(MasterAccount ma) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            ma.setUpdateAt(new Date());
            session.evict(ma);
            session.saveOrUpdate(ma);

            tx.commit();
            session.flush();
            response = true;
        } catch (Exception e) {
            log.error("Exception in updateMasterAccount() " + ma.toString(), e);
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
    public Object getCashTransactions(int startIndex, int fetchSize, String orderBy, String sortby, String startDate, String endDate, String buyerSupplier) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(MasterAccountHistory.class);

            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("payee", buyerSupplier));
            }

            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);

            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(sortby));
            } else if (!sortby.isEmpty()) {
                criteria.addOrder(Order.desc(sortby));
            } else {
                criteria.addOrder(Order.desc("timestamp"));
            }
            List<MasterAccountHistory> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getCashTransactions() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getCashTransactionsCount(String startDate, String endDate, String buyerSupplier) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(MasterAccountHistory.class);

            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("payee", buyerSupplier));
            }

            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getCashTransactionsCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    public AsphaltSales getAsphaltSale(String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AsphaltSales.class);

            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("buyer", buyerSupplier));
            }
            if (!project.isEmpty()) {
                criteria.add(Restrictions.eq("project", project));
            }

            criteria.setFirstResult(0);
            criteria.setMaxResults(1);

            criteria.addOrder(Order.desc("id"));

            List<AsphaltSales> emps = criteria.list();
            if (emps.size() > 0) {
                return emps.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAsphaltSale() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    public List<AsphaltSaleConsumption> getAsphaltSaleConsumptions(AsphaltSales ass) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AsphaltSaleConsumption.class);

            criteria.add(Restrictions.eq("asphlatSaleId", ass));

            List<AsphaltSaleConsumption> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAsphaltSaleConsumptions() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    public List<AsphaltSales> getAsphaltSales(String from, String to) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AsphaltSales.class);
            criteria.add(Restrictions.ge("isActive", true));

            if (!from.isEmpty()) {
                criteria.add(Restrictions.ge("createdDate", Constants.DATE_FORMAT.parse(from)));
            }
            if (!to.isEmpty()) {
                criteria.add(Restrictions.le("createdDate", Constants.DATE_FORMAT.parse(to)));
            }

            criteria.addOrder(Order.desc("createdDate"));

            List<AsphaltSales> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAsphaltSale() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public List<StockTrace> getLastMonthStats() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(StockTrace.class);

            LocalDate todaydate = LocalDate.now();
            String from = todaydate.withMonth(todaydate.getMonthValue() - 1).toString();

            criteria.add(Restrictions.eq("month", Constants.MONTH_FORMAT.format(Constants.MONTH_FORMAT.parse(from))));
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
}
