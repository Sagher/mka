package com.mka.dao;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.model.Entries;
import com.mka.model.EntryItems;
import java.util.List;

public interface EntriesDao {

    public List<EntryItems> getAllEntryItems();

    public boolean logEntry(Entries entry);

    public List<Entries> getEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate);

    public int getEntriesCount(String startDate, String endDate);

    public Entries getEntry(int id);

    public boolean updateEntry(Entries entry);

}
