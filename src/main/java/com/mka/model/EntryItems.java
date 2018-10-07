/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sagher Mehmood
 */
@Entity
@Table(name = "entry_items")
@NamedQueries({
    @NamedQuery(name = "EntryItems.findAll", query = "SELECT e FROM EntryItems e")})
public class EntryItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "item_name")
    private String itemName;
    @Size(max = 100)
    @Column(name = "item_type")
    private String itemType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "entry_type")
    private String entryType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "sub_entry_type")
    private String subEntryType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    @JsonIgnore
    private List<EntriesDirect> entriesDirectList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    @JsonIgnore
    private List<EntriesIndirect> entriesIndirectList;

    public EntryItems() {
    }

    public EntryItems(Integer id) {
        this.id = id;
    }

    public EntryItems(Integer id, String itemName, String entryType, String subEntryType) {
        this.id = id;
        this.itemName = itemName;
        this.entryType = entryType;
        this.subEntryType = subEntryType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getSubEntryType() {
        return subEntryType;
    }

    public void setSubEntryType(String subEntryType) {
        this.subEntryType = subEntryType;
    }

    public List<EntriesDirect> getEntriesDirectList() {
        return entriesDirectList;
    }

    public void setEntriesDirectList(List<EntriesDirect> entriesDirectList) {
        this.entriesDirectList = entriesDirectList;
    }

    public List<EntriesIndirect> getEntriesIndirectList() {
        return entriesIndirectList;
    }

    public void setEntriesIndirectList(List<EntriesIndirect> entriesIndirectList) {
        this.entriesIndirectList = entriesIndirectList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntryItems)) {
            return false;
        }
        EntryItems other = (EntryItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mka.configuration.EntryItems[ id=" + id + " ]";
    }

}
