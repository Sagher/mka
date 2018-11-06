package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.service.EntriesService;
import com.mka.service.StatsService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("entriesService")
public class EntriesServiceImpl implements EntriesService {

    private static final Logger log = Logger.getLogger(EntriesServiceImpl.class);

//    @Autowired
//    AsyncUtil asyncUtil;
    @Autowired
    EntriesDao entriesDao;

    @Autowired
    StatsService ss;

    private List<EntryItems> entryItems = null;

    @Override
    public List<EntryItems> getAllEntryItems() {
        if (entryItems == null) {
            entryItems = entriesDao.getAllEntryItems();
        }
        return entryItems;
    }

    public EntryItems getEntryItemById(int id) {
        EntryItems item = null;
        try {
            item = getAllEntryItems().parallelStream().filter(e -> e.getId() == id).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            log.error("Exception in getEntryItemById(" + id + "): ", e);
        }
        return item;
    }

    @Override
    public boolean logDirectEntry(EntriesDirect entry) {
        return entriesDao.logDirectEntry(entry);
    }

    @Override
    public List<EntriesDirect> getDirectEntries(EntryItems entryItem, String subEntryType, int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project) {
        return entriesDao.getDirectEntries(entryItem, subEntryType, startIndex, fetchSize, orderBy, sortBy, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public int getDirectEntriesCount(EntryItems entryItem, String subEntryType, String startDate, String endDate,
            String buyerSupplier, String project) {
        return entriesDao.getDirectEntriesCount(entryItem, subEntryType, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public EntriesDirect getDirectEntry(int id) {
        return entriesDao.getDirectEntry(id);
    }

    @Override
    public boolean updateDirectEntry(EntriesDirect entry) {
        return entriesDao.updateDirectEntry(entry);
    }

    @Override
    public boolean logInDirectEntry(EntriesIndirect entry) {
        boolean logged = entriesDao.logInDirectEntry(entry);
        if (logged) {
            MasterAccount ma = ss.getMasterAccount();
            ma.setCashInHand(ma.getCashInHand() - entry.getAmount());
            ss.updateMasterAccount(ma);
        }
        return logged;
    }

    @Override
    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        return entriesDao.getInDirectEntries(startIndex, fetchSize, orderBy, sortBy, startDate, endDate);
    }

    @Override
    public int getInDirectEntriesCount(String startDate, String endDate) {
        return entriesDao.getInDirectEntriesCount(startDate, endDate);
    }

    @Override
    public EntriesIndirect getInDirectEntry(int id) {
        return entriesDao.getInDirectEntry(id);
    }

    @Override
    public boolean updateInDirectEntry(EntriesIndirect entry) {
        return entriesDao.updateInDirectEntry(entry);
    }

    @Override
    public EntryItems createNewEntryItem(String parameter) {
        EntryItems item = new EntryItems();
        item.setItemName(parameter);
        item.setItemType(null);
        item.setItemUnit(null);
        item.setEntryType(Constants.INDIRECT);
        item.setSubEntryType(Constants.EXPENSE);

        List<EntryItems> items = getAllEntryItems().parallelStream().filter((EntryItems e) -> {
            if (e.getItemName().equalsIgnoreCase(parameter)) {
                return e.getEntryType().equalsIgnoreCase(Constants.INDIRECT);
            }
            return false;
        }).collect(Collectors.toList());

        if (items != null && items.size() > 0) {
            return items.get(0);
        } else {
            item = entriesDao.createNewEntryItem(item);
            if (item != null) {
                entryItems.add(item);
            }
            return item;
        }
    }

    public void addEntryDetail(EntriesDirectDetails entryDetail) {
        entriesDao.addEntryDetail(entryDetail);
    }
}
