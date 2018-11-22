package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
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
import org.hibernate.criterion.Criterion;
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
            criteria.add(Restrictions.eq("isActive", true));
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
    public List<EntriesDirect> getDirectEntries(EntryItems entryItem, String subEntryType, int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesDirect.class);
            criteria.add(Restrictions.eq("isActive", true));

            if (entryItem != null) {
                criteria.add(Restrictions.eq("item", entryItem));
            }
            if (!subEntryType.isEmpty()) {
                criteria.add(Restrictions.eq("subEntryType", subEntryType));
            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                Criterion c1 = Restrictions.eq("supplier", buyerSupplier);
                Criterion c2 = Restrictions.eq("buyer", buyerSupplier);
                criteria.add(Restrictions.or(c1, c2));
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
    public int getDirectEntriesCount(EntryItems entryItem, String subEntryType, String startDate,
            String endDate, String buyerSupplier, String project) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesDirect.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (entryItem != null) {
                criteria.add(Restrictions.eq("item", entryItem));
            }
            if (!subEntryType.isEmpty()) {
                criteria.add(Restrictions.eq("subEntryType", subEntryType));
            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("entryDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("entryDate", Constants.DATE_FORMAT.parse(endDate)));
            }
            if (!buyerSupplier.isEmpty()) {
                Criterion c1 = Restrictions.eq("supplier", buyerSupplier);
                Criterion c2 = Restrictions.eq("buyer", buyerSupplier);
                criteria.add(Restrictions.or(c1, c2));
            }
            if (!project.isEmpty()) {
                criteria.add(Restrictions.eq("project", project));
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

    @Override
    public EntryItems createNewEntryItem(EntryItems item) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(item);
            item.setCreatedDate(new Date());
            item.setIsActive(true);
            session.save(item);

            tx.commit();
            session.flush();
            response = true;
            log.info("New Entry Item Created: " + item);
        } catch (Exception e) {
            log.error("Exception in logDirectEntry() " + item.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return item;
    }

    public void addEntryDetail(EntriesDirectDetails entryDetail) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entryDetail);
            session.save(entryDetail);

            tx.commit();
            session.flush();
            response = true;
            log.info("New Entry Item Detail Created: " + entryDetail);
        } catch (Exception e) {
            log.error("Exception in addEntryDetail() " + entryDetail.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    public boolean logAsphaltSale(AsphaltSales ass) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            ass.setIsActive(true);
            ass.setCreatedDate(new Date());
            ass.setEntryDate(new Date());

            session.evict(ass);
            session.save(ass);

            tx.commit();
            session.flush();
            response = true;
            log.info("New Asphalt sale Logged: " + ass);
        } catch (Exception e) {
            log.error("Exception in logAsphaltSale() " + ass.toString(), e);
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
    public boolean logAsphaltSaleConsumptions(List<AsphaltSaleConsumption> asses) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);

            for (AsphaltSaleConsumption ass : asses) {
                session.evict(ass);
                session.save(ass);
            }

            tx.commit();
            session.flush();
            response = true;
            log.info("New AsphaltSaleConsumptions Logged: " + asses);
        } catch (Exception e) {
            log.error("Exception in logAsphaltSaleConsumptions() " + asses.toString(), e);
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
