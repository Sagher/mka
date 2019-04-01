package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MachineryCarriage;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import com.mka.service.StatsService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import java.math.BigDecimal;
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

    @Autowired
    private AsyncUtil asyncUtil;

    @Autowired
    private StatsService statsService;

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
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project, String subType) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EntriesDirect.class);
            criteria.add(Restrictions.eq("isActive", true));

            if (entryItem != null) {
                criteria.add(Restrictions.eq("item", entryItem));
            }
            if (subType != null) {
                criteria.add(Restrictions.eq("subType", subType));
            }
            if (!subEntryType.isEmpty()) {
                criteria.add(Restrictions.eq("subEntryType", subEntryType));
            }
            if (!startDate.isEmpty()) {
                criteria.add(Restrictions.ge("createdDate", Constants.DATE_FORMAT.parse(startDate)));
            }
            if (!endDate.isEmpty()) {
                criteria.add(Restrictions.le("createdDate", Constants.DATE_FORMAT.parse(endDate)));
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
                criteria.addOrder(Order.desc("createdDate"));
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
    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier) {
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
            if (!buyerSupplier.isEmpty()) {
                Criterion c1 = Restrictions.eq("name", buyerSupplier);
                Criterion c2 = Restrictions.eq("name", buyerSupplier);
                criteria.add(Restrictions.or(c1, c2));
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
    public int getInDirectEntriesCount(String startDate, String endDate, String buyerSupplier) {
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
            if (!buyerSupplier.isEmpty()) {
                Criterion c1 = Restrictions.eq("name", buyerSupplier);
                Criterion c2 = Restrictions.eq("name", buyerSupplier);
                criteria.add(Restrictions.or(c1, c2));
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

    @Override
    public boolean logMachineryCarriage(MachineryCarriage mac) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(mac);

            mac.setIsActive(true);
            mac.setCreatedDate(new Date());
            mac.setUpdateDate(new Date());

            session.save(mac);

            tx.commit();
            session.flush();
            response = true;
            log.info("New Machinery Carriage Logged: " + mac);
        } catch (Exception e) {
            log.error("Exception in logMachineryCarriage() " + mac.toString(), e);
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
    public AsphaltSales getAsphaltSaleById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AsphaltSales.class);
            criteria.add(Restrictions.eq("id", id));
            List<AsphaltSales> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAsphaltSaleById() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public AccountPayableReceivable getPayableReceivableEntry(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);
            criteria.add(Restrictions.eq("id", id));
            List<AccountPayableReceivable> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getPayableReceivableEntry() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean deleteIndirectPayRecEntryAndUpdateAccount(AccountPayableReceivable entry) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entry);
            entry.setIsActive(false);
            session.saveOrUpdate(entry);

            if (entry.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                entry.setType(Constants.PAYABLE);
            } else if (entry.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                entry.setType(Constants.RECEIVABLE);
            }
            asyncUtil.updateAccount(entry);

            tx.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error("Exception in updatePayRecEntry() " + entry.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return false;
    }

    @Override
    public boolean deleteAsphaltSaleAndAllRelated(AsphaltSales as) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);

            session.evict(as);
            as.setIsActive(false);
            session.saveOrUpdate(as);

            List<AccountPayableReceivable> relatedEntries = getPayableReceivableEntriesByEntryId(as.getId());
            if (relatedEntries != null) {
                for (AccountPayableReceivable ac : relatedEntries) {
                    session.evict(ac);
                    ac.setIsActive(false);

                    if (ac.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                        ac.setType(Constants.PAYABLE);
                    } else if (ac.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                        ac.setType(Constants.RECEIVABLE);
                    }
                    asyncUtil.updateAccount(ac);

                    session.saveOrUpdate(ac);
                }
            }

            // add stock of crush & bitumen
            // minus the asphalt sale
            List<AsphaltSaleConsumption> asConsumption = statsService.getAsphaltSaleConsumptions(as);
            if (asConsumption != null && !asConsumption.isEmpty()) {
                for (StockTrace s : statsService.getStats()) {
                    switch (s.getItemId()) {
                        case 1:
                            s.setStockUnits(s.getStockUnits().add(asConsumption.get(0).getItemQuantity()));
                            s.setStockAmount(s.getStockAmount().add(asConsumption.get(0).getItemAmount()));
                            s.setConsumeUnit(s.getConsumeUnit().subtract(asConsumption.get(0).getItemQuantity()));
                            s.setConsumeAmount(s.getConsumeAmount().subtract(asConsumption.get(0).getItemAmount()));
                            asyncUtil.updateStockTrace(s);
                            break;
                        case 6:
                            String key = s.getItemName() + s.getSubType();
                            for (AsphaltSaleConsumption aasCon : asConsumption) {
                                if (aasCon.getItemName().equalsIgnoreCase(key)) {
                                    s.setStockUnits(s.getStockUnits().add(aasCon.getItemQuantity()));
                                    s.setStockAmount(s.getStockAmount().add(aasCon.getItemAmount()));

                                    s.setConsumeUnit(s.getConsumeUnit().subtract(aasCon.getItemQuantity()));
                                    s.setConsumeAmount(s.getConsumeAmount().subtract(aasCon.getItemAmount()));

                                    asyncUtil.updateStockTrace(s);
                                }
                            }
                            break;
                        case 17:
                            s.setSalesUnit(s.getSalesUnit().subtract(as.getQuantity()));
                            s.setSalesAmount(s.getSalesAmount().subtract(as.getTotalSaleAmount()));
                            asyncUtil.updateStockTrace(s);
                            break;
                        default:
                            break;
                    }
                }
            }

            tx.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error("Exception in deleteAsphaltSaleAndAllRelated() " + as.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return false;
    }

    @Override
    public boolean deleteCashTransaction(AccountPayableReceivable entry) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entry);
            entry.setIsActive(false);
            session.saveOrUpdate(entry);

            if (entry.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                entry.setType(Constants.PAYABLE);
            } else if (entry.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                entry.setType(Constants.RECEIVABLE);
            }
            asyncUtil.updateAccount(entry);

            MasterAccount ma = statsService.getMasterAccount();
            if (entry.getSubType().equalsIgnoreCase(Constants.PERSON_TO_HEADOFFICE)) {
                ma.setTotalCash(ma.getTotalCash().subtract(entry.getTotalAmount()));

            } else if (entry.getSubType().equalsIgnoreCase(Constants.CASH_IN_HAND_TO_HEADOFFICE)) {
                ma.setCashInHand(ma.getCashInHand().add(entry.getTotalAmount()));
                ma.setTotalCash(ma.getTotalCash().subtract(entry.getTotalAmount()));

            } else if (entry.getSubType().equalsIgnoreCase(Constants.FROM_HEADOFFICE_TO_PERSON)) {
                ma.setTotalCash(ma.getTotalCash().add(entry.getTotalAmount()));

            } else if (entry.getSubType().equalsIgnoreCase(Constants.FROM_HEADOFFICE_TO_CASH_IN_HAND)) {
                ma.setCashInHand(ma.getCashInHand().subtract(entry.getTotalAmount()));
                ma.setTotalCash(ma.getTotalCash().add(entry.getTotalAmount()));

            } else if (entry.getSubType().equalsIgnoreCase(Constants.FROM_CASH_IN_HAND_TO_PERSON)) {
                ma.setCashInHand(ma.getCashInHand().add(entry.getTotalAmount()));

            } else if (entry.getSubType().equalsIgnoreCase(Constants.FROM_PERSON_TO_CASH_IN_HAND)) {
                ma.setCashInHand(ma.getCashInHand().subtract(entry.getTotalAmount()));

            }

            statsService.updateMasterAccount(ma);

            tx.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error("Exception in deleteCashTransaction() " + entry.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return false;
    }

    @Override
    public boolean deleteDirectEntry(AccountPayableReceivable entry) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entry);
            entry.setIsActive(false);
            session.saveOrUpdate(entry);
            for (StockTrace s : statsService.getStats()) {
                if (s.getItemId() == entry.getItemType().getId()) {
                    if (entry.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                        s.setStockUnits(s.getStockUnits().add(entry.getQuantity()));
                        s.setStockAmount(s.getStockAmount().add(entry.getTotalAmount()));
                    } else if (entry.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                        s.setStockUnits(s.getStockUnits().subtract(entry.getQuantity()));
                        s.setStockAmount(s.getStockAmount().subtract(entry.getTotalAmount()));
                    }
                    asyncUtil.updateStockTrace(s);
                }
            }

            if (entry.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                entry.setType(Constants.PAYABLE);
            } else if (entry.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                entry.setType(Constants.RECEIVABLE);
            }
            asyncUtil.updateAccount(entry);

            tx.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error("Exception in deleteDirectEntry() " + entry.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return false;
    }

    @Override
    public boolean deleteDirectCrushAndRelatedEntries(AccountPayableReceivable entry) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);

            List<AccountPayableReceivable> allEntries = getPayableReceivableEntriesByEntryId(entry.getEntryId());
            if (allEntries != null && !allEntries.isEmpty()) {

                for (AccountPayableReceivable acc : allEntries) {
                    session.evict(false);
                    acc.setIsActive(false);
                    session.saveOrUpdate(acc);

                    for (StockTrace s : statsService.getStats()) {
                        if (s.getItemId() == entry.getItemType().getId()
                                && s.getSubType().equalsIgnoreCase(entry.getSubType())) {
                            if (entry.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                                entry.setType(Constants.PAYABLE);
                                s.setStockUnits(s.getStockUnits().add(entry.getQuantity()));
                                s.setStockAmount(s.getStockAmount().add(entry.getTotalAmount()));
                            } else if (entry.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                                entry.setType(Constants.RECEIVABLE);
                                s.setStockUnits(s.getStockUnits().subtract(entry.getQuantity()));
                                s.setStockAmount(s.getStockAmount().subtract(entry.getTotalAmount()));
                            }
                            asyncUtil.updateStockTrace(s);
                        }
                    }
                    asyncUtil.updateAccount(acc);
                }

            }

            tx.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error("Exception in deleteDirectCrushAndRelatedEntries() " + entry.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return false;
    }

    @Override
    public boolean deleteIndirectEntry(AccountPayableReceivable entry) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(entry);
            entry.setIsActive(false);
            session.saveOrUpdate(entry);

            if (entry.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                entry.setType(Constants.PAYABLE);
            } else if (entry.getType().equalsIgnoreCase(Constants.PAYABLE)) {
                entry.setType(Constants.RECEIVABLE);
            }
            asyncUtil.updateAccount(entry);

            MasterAccount ma = statsService.getMasterAccount();
            ma.setCashInHand(ma.getCashInHand().add(entry.getTotalAmount()));

            statsService.updateMasterAccount(ma);

            tx.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error("Exception in deleteDirectCrushAndRelatedEntries() " + entry.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return false;
    }

    private List<AccountPayableReceivable> getPayableReceivableEntriesByEntryId(int entryId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(AccountPayableReceivable.class);
            criteria.add(Restrictions.eq("isActive", true));
            criteria.add(Restrictions.eq("entryId", entryId));
            List<AccountPayableReceivable> entryItems = criteria.list();
            if (entryItems.size() > 0) {
                return entryItems;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getPayableReceivableEntriesByEntryId() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

}
