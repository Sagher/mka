package com.mka.service;

import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sagher Mehmood
 */
public interface EntriesService {

    public List<EntryItems> getAllEntryItems();

    public EntryItems getEntryItemById(int id);

    public Object logDirectEntry(HttpServletRequest request);

    public List<EntriesDirect> getDirectEntries(EntryItems entryItem, String subEntryType, int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project);

    public int getDirectEntriesCount(EntryItems entryItem, String subEntryType, String startDate, String endDate,
            String buyerSupplier, String project);

    public EntriesDirect getDirectEntry(int id);

    public boolean updateDirectEntry(EntriesDirect entry);

    public Object logInDirectEntry(HttpServletRequest request);

    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier);

    public int getInDirectEntriesCount(String startDate, String endDate, String buyerSupplier);

    public EntriesIndirect getInDirectEntry(int id);

    public boolean updateInDirectEntry(EntriesIndirect entry);

    public EntryItems createNewEntryItem(String parameter);

    public void addEntryDetail(EntriesDirectDetails entryDetail);

    public Object logAsphaltSale(HttpServletRequest request);

    public boolean logInDirectEntry(EntriesIndirect entry);
}
