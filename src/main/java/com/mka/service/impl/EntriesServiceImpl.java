package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.service.EntriesService;
import com.mka.utils.AsyncUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("entriesService")
public class EntriesServiceImpl implements EntriesService {

    private static final Logger log = Logger.getLogger(EntriesServiceImpl.class);

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    EntriesDao entriesDao;

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
            item = entryItems.parallelStream().filter(e -> e.getId() == id).collect(Collectors.toList()).get(0);
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
    public List<EntriesDirect> getDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        return entriesDao.getDirectEntries(startIndex, fetchSize, orderBy, sortBy, startDate, endDate);
    }

    @Override
    public int getDirectEntriesCount(String startDate, String endDate) {
        return entriesDao.getDirectEntriesCount(startDate, endDate);
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
        return entriesDao.logInDirectEntry(entry);
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

}
