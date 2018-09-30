package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.Entries;
import com.mka.model.EntryItems;
import com.mka.service.EntriesService;
import com.mka.utils.AsyncUtil;
import java.util.List;
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

    @Override
    public boolean logEntry(Entries entry) {
        return entriesDao.logEntry(entry);
    }

    @Override
    public List<Entries> getEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        return entriesDao.getEntries(startIndex, fetchSize, orderBy, sortBy, startDate, endDate);
    }

    @Override
    public int getEntriesCount(String startDate, String endDate) {
        return entriesDao.getEntriesCount(startDate, endDate);
    }

    @Override
    public Entries getEntry(int id) {
        return entriesDao.getEntry(id);
    }

    @Override
    public boolean updateEntry(Entries entry) {
        return entriesDao.updateEntry(entry);
    }

}
