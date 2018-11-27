/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao.impl;

import com.mka.dao.AccountsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import com.mka.utils.Constants;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
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
public class AccountsDaoImpl implements AccountsDao {

    private static final Logger log = Logger.getLogger(AccountsDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean logAccountPayableReceivable(AccountPayableReceivable accountPayableReceivable) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(accountPayableReceivable);
            accountPayableReceivable.setCreatedDate(new Date());
            session.save(accountPayableReceivable);

            tx.commit();
            session.flush();
            response = true;
            log.info("AccountPayableReceivable Entry Logged: " + accountPayableReceivable);
        } catch (Exception e) {
            log.error("Exception in logAccountPayableReceivable() " + accountPayableReceivable.toString(), e);
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
    public List<AccountPayableReceivable> getAccountPayableReceivable(EntryItems entryItem, String type,
            int startIndex, int fetchSize, String orderBy, String sortBy, String startDate,
            String endDate, String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));

            if (type != null) {
                criteria.add(Restrictions.eq("type", type));
            }

            if (entryItem != null) {
            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("createdDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("createdDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("accountName", buyerSupplier));
            }
            if (!project.isEmpty()) {
                criteria.add(Restrictions.eq("project", project));
            }

            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);
            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(sortBy));
            } else if (!sortBy.isEmpty()) {
                criteria.addOrder(Order.desc(sortBy));
            } else {
                criteria.addOrder(Order.desc("id"));
            }
            List<AccountPayableReceivable> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAccountPayableReceivable() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getAccountPayableReceivableCount(EntryItems entryItem, String type, String startDate, String endDate, String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));
            criteria.add(Restrictions.eq("type", type));

            if (entryItem != null) {
                criteria.add(Restrictions.eq("itemType", entryItem));
            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("createdDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("createdDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("accountName", buyerSupplier));
            }
            if (!project.isEmpty()) {
                criteria.add(Restrictions.eq("project", project));
            }

            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getAccountPayableReceivableCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

}
