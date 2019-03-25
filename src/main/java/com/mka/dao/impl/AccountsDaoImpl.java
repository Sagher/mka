/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao.impl;

import com.mka.dao.AccountsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccountHistory;
import com.mka.utils.Constants;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
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
    public List<AccountPayableReceivable> getAccountPayableReceivable(
            EntryItems entryItem, String type, String subType,
            int startIndex, int fetchSize, String orderBy, String sortBy, String startDate,
            String endDate, String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));

            if (type != null) {
                Criterion rest1 = Restrictions.eq("type", type);
                Criterion rest2 = Restrictions.eq("type", "NA");
                criteria.add(Restrictions.or(rest1, rest2));
//                criteria.add(Restrictions.eq("type", type));
            }

            if (subType != null) {
                Criterion rest1 = Restrictions.eq("subType", subType);
                Criterion rest2 = Restrictions.like("subType", "aw", MatchMode.START);
                criteria.add(Restrictions.or(rest1, rest2));
//                criteria.add(Restrictions.eq("subType", subType));
            }

            if (entryItem != null) {
                criteria.add(Restrictions.eq("itemType", entryItem));

            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("accountName", buyerSupplier));
            }
            if (project != null && !project.isEmpty()) {
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
    public int getAccountPayableReceivableCount(EntryItems entryItem, String type, 
            String startDate, String endDate, String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));

            if (type != null) {
                Criterion rest1 = Restrictions.eq("type", type);
                Criterion rest2 = Restrictions.eq("type", "NA");
                criteria.add(Restrictions.or(rest1, rest2));
            }

            if (entryItem != null) {
                criteria.add(Restrictions.eq("itemType", entryItem));
            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (buyerSupplier != null && !buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("accountName", buyerSupplier));
            }
            if (project != null && !project.isEmpty()) {
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

    @Override
    public Object getAllTransactions(String orderBy, String sortby, String startDate, String endDate, String buyerSupplier) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));

            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            criteria.add(Restrictions.eq("accountName", buyerSupplier));

            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(sortby));
            } else if (!sortby.isEmpty()) {
                criteria.addOrder(Order.desc(sortby));
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
            log.error("Exception in getAllTransactions() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public Object getAccountPayableReceivable(String type, String[] payablees, int startIndex, int fetchSize, String orderBy, String sortby, String startDate, String endDate, String buyerSupplier) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));
//            criteria.add(Restrictions.eq("type", type));
            if (type != null) {
//                Criterion rest1 = Restrictions.eq("type", type);
//                Criterion rest2 = Restrictions.eq("itemType", new EntryItems(18));
//                criteria.add(Restrictions.or(rest1, rest2));
            }

            if (payablees != null && payablees.length > 0) {
                Criterion[] crits = new Criterion[payablees.length];
                for (int i = 0; i < payablees.length; i++) {
                    crits[i] = Restrictions.eq("accountName", payablees[i]);
                }
                criteria.add(Restrictions.or(crits));
//                criteria.add(Restrictions.eq("type", type));
            }

            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("accountName", buyerSupplier));
            }

            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);
            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(sortby));
            } else if (!sortby.isEmpty()) {
                criteria.addOrder(Order.desc(sortby));
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
    public int getAccountPayableReceivableCounts(String type, String[] payablees, String startDate, String endDate, String buyerSupplier) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);

            criteria.add(Restrictions.eq("isActive", true));
            criteria.add(Restrictions.eq("type", type));

            if (payablees != null && payablees.length > 0) {
                Criterion[] crits = new Criterion[payablees.length];
                for (int i = 0; i < payablees.length; i++) {
                    crits[i] = Restrictions.eq("accountName", payablees[i]);
                }
                criteria.add(Restrictions.or(crits));
//                criteria.add(Restrictions.eq("type", type));
            }

            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("timestamp", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("timestamp", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                criteria.add(Restrictions.eq("accountName", buyerSupplier));
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

    @Override
    public int getHeadOfficeReceivable() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(MasterAccountHistory.class);

            List<MasterAccountHistory> emps = criteria.list();
            if (emps.size() > 0) {
                int rec = 0;
                for (MasterAccountHistory mah : emps) {
                    if (mah.getType().equalsIgnoreCase(Constants.PERSON_TO_HEADOFFICE)
                            || mah.getType().equalsIgnoreCase(Constants.CASH_IN_HAND_TO_HEADOFFICE)) {
                        rec += mah.getAmount().intValue();
                    } else if (mah.getType().equalsIgnoreCase(Constants.FROM_HEADOFFICE_TO_PERSON)
                            || mah.getType().equalsIgnoreCase(Constants.FROM_HEADOFFICE_TO_CASH_IN_HAND)) {
                        rec = rec - mah.getAmount().intValue();
                    }
                }
                return rec;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("Exception in getAccountPayableReceivable() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }
}
