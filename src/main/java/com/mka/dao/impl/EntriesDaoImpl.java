package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesIndirect;
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

@Repository
public class EntriesDaoImpl implements EntriesDao {

    private static final Logger log = Logger.getLogger(EntriesDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EntryItems> getAllEntryItems() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntryItems.class);
            List<EntryItems> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAllEntryItems() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean logDirectEntry(EntriesDirect entry) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entry);
            entry.setCreatedDate(new Date());
            entry.setUpdateDate(null);
            session.save(entry);

            tx.commit();
            session.flush();
            response = true;
            log.info("Direct Entry Logged: " + entry);
        } catch (Exception e) {
            log.error("Exception in logDirectEntry() " + entry.toString(), e);
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
    public List<EntriesDirect> getDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesDirect.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);
            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(sortBy));
            } else if (!sortBy.isEmpty()) {
                criteria.addOrder(Order.desc(sortBy));
            } else {
                criteria.addOrder(Order.desc("entryDate"));
            }
            List<EntriesDirect> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getDirectEntries() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getDirectEntriesCount(String startDate, String endDate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesDirect.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getDirectEntriesCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public EntriesDirect getDirectEntry(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesDirect.class);
            criteria.add(Restrictions.eq("id", id));
            List<EntriesDirect> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getDirectEntry() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean updateDirectEntry(EntriesDirect entry) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            entry.setUpdateDate(new Date());
            session.evict(entry);
            session.saveOrUpdate(entry);

            tx.commit();
            session.flush();
            response = true;
            log.info("Direct Entry Updated: " + entry);
        } catch (Exception e) {
            log.error("Exception in updateDirectEntry() " + entry.toString(), e);
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
    public boolean logInDirectEntry(EntriesIndirect entry) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entry);
            entry.setCreatedDate(new Date());
            entry.setUpdateDate(null);
            session.save(entry);

            tx.commit();
            session.flush();
            response = true;
            log.info("In-Direct Entry Logged: " + entry);
        } catch (Exception e) {
            log.error("Exception in logInDirectEntry() " + entry.toString(), e);
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
    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesIndirect.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);
            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(sortBy));
            } else if (!sortBy.isEmpty()) {
                criteria.addOrder(Order.desc(sortBy));
            } else {
                criteria.addOrder(Order.desc("entryDate"));
            }
            List<EntriesIndirect> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getInDirectEntries() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getInDirectEntriesCount(String startDate, String endDate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesIndirect.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getInDirectEntriesCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public EntriesIndirect getInDirectEntry(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesIndirect.class);
            criteria.add(Restrictions.eq("id", id));
            List<EntriesIndirect> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getInDirectEntry() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean updateInDirectEntry(EntriesIndirect entry) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            entry.setUpdateDate(new Date());
            session.evict(entry);
            session.saveOrUpdate(entry);

            tx.commit();
            session.flush();
            response = true;
            log.info("In-Direct Entry Updated: " + entry);
        } catch (Exception e) {
            log.error("Exception in updateInDirectEntry() " + entry.toString(), e);
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

}
