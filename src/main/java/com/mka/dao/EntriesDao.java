package com.mka.dao;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MachineryCarriage;
import java.util.List;

public interface EntriesDao {

    public List<EntryItems> getAllEntryItems();

    public boolean logDirectEntry(EntriesDirect entry);

    public List<EntriesDirect> getDirectEntries(EntryItems entryItem, String subEntryType, int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project, String subType);

    public int getDirectEntriesCount(EntryItems entryItem, String subEntryType, String startDate, String endDate,
            String buyerSupplier, String project);

    public EntriesDirect getDirectEntry(int id);

    public boolean updateDirectEntry(EntriesDirect entry);

    public boolean logInDirectEntry(EntriesIndirect entry);

    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier);

    public int getInDirectEntriesCount(String startDate, String endDate, String buyerSupplier);

    public EntriesIndirect getInDirectEntry(int id);

    public boolean updateInDirectEntry(EntriesIndirect entry);

    public EntryItems createNewEntryItem(EntryItems item);

    public void addEntryDetail(EntriesDirectDetails entryDetail);

    public boolean logAsphaltSale(AsphaltSales ass);

    public boolean logAsphaltSaleConsumptions(List<AsphaltSaleConsumption> ass);

    public boolean logMachineryCarriage(MachineryCarriage mac);
}
