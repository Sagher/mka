package com.mka.service;

import com.mka.model.EntriesDirect;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface EntriesService {

    public List<EntryItems> getAllEntryItems();

    public EntryItems getEntryItemById(int id);

    public boolean logDirectEntry(EntriesDirect entry);

    public List<EntriesDirect> getDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate);

    public int getDirectEntriesCount(String startDate, String endDate);

    public EntriesDirect getDirectEntry(int id);

    public boolean updateDirectEntry(EntriesDirect entry);

    public boolean logInDirectEntry(EntriesIndirect entry);

    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate);

    public int getInDirectEntriesCount(String startDate, String endDate);

    public EntriesIndirect getInDirectEntry(int id);

    public boolean updateInDirectEntry(EntriesIndirect entry);
}
