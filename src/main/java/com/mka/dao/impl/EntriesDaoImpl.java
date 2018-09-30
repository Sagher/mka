package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.Entries;
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
    public boolean logEntry(Entries entry) {
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
        } catch (Exception e) {
            log.error("Exception in logEntry() " + entry.toString(), e);
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
    public List<Entries> getEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Entries.class);
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
            List<Entries> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getEntries() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getEntriesCount(String startDate, String endDate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Entries.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getEntriesCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public Entries getEntry(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Entries.class);
            criteria.add(Restrictions.eq("id", id));
            List<Entries> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getEntry() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean updateEntry(Entries entry) {
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
        } catch (Exception e) {
            log.error("Exception in updateEntry() " + entry.toString(), e);
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
