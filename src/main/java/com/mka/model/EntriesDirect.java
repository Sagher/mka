/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sagher Mehmood
 */
@Entity
@Table(name = "entries_direct")
@NamedQueries({
    @NamedQuery(name = "EntriesDirect.findAll", query = "SELECT e FROM EntriesDirect e")})
public class EntriesDirect implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "advance")
    private int advance;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 20)
    @Column(name = "item_type")
    private String itemType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "sub_entry_type")
    private String subEntryType;
    @Basic(optional = false)
    @Column(name = "buyer")
    private String buyer;
    @Basic(optional = false)
    @Column(name = "supplier")
    private String supplier;
    @Column(name = "project")
    private String project;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rate")
    private int rate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_price")
    private int totalPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "entry_date")
    @Temporal(TemporalType.DATE)
    private Date entryDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JoinColumn(name = "item", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private EntryItems item;

    public String getItemName() {
        return item.getItemName();
    }

    public EntriesDirect() {
    }

    public EntriesDirect(Integer id) {
        this.id = id;
    }

    public EntriesDirect(Integer id, String subEntryType, String buyer, String supplier, String project, int quantity, int rate, int totalPrice, boolean isActive, Date createdDate) {
        this.id = id;
        this.subEntryType = subEntryType;
        this.buyer = buyer;
        this.supplier = supplier;
        this.project = project;
        this.quantity = quantity;
        this.rate = rate;
        this.totalPrice = totalPrice;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSubEntryType() {
        return subEntryType;
    }

    public void setSubEntryType(String subEntryType) {
        this.subEntryType = subEntryType;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public EntryItems getItem() {
        return item;
    }

    public void setItem(EntryItems item) {
        this.item = item;
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
        if (!(object instanceof EntriesDirect)) {
            return false;
        }
        EntriesDirect other = (EntriesDirect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + ", itemType=" + itemType + ", subEntryType=" + subEntryType + ", buyer=" + buyer + ", supplier=" + supplier + ", project=" + project + ", quantity=" + quantity + ", rate=" + rate + ", advance=" + advance + ", totalPrice=" + totalPrice + ", isActive=" + isActive + ", entryDate=" + entryDate + ", createdDate=" + createdDate + ", updateDate=" + updateDate;
    }

    public int getAdvance() {
        return advance;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
    }

}
