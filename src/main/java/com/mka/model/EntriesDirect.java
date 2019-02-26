/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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

    @Size(max = 10)
    @Column(name = "asphalt_type")
    private String asphaltType;
    @Size(max = 10)
    @Column(name = "asphalt_ton")
    private String asphaltTon;
    @Size(max = 10)
    @Column(name = "vehicle_no")
    private String vehicleNo;

    @Column(name = "plant_bilty")
    private Integer plantBilty;
    @Column(name = "recipient_bilty")
    private Integer recipientBilty;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rate")
    private BigDecimal rate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "advance")
    private BigDecimal advance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "sub_entry_type")
    private String subEntryType;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "entryId")
    private EntriesDirectDetails entriesDirectDetails;

    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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

    public EntriesDirect(Integer id, String buyer, String supplier, String project, BigDecimal quantity, BigDecimal rate, BigDecimal totalPrice, boolean isActive, Date createdDate) {
        this.id = id;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
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
        return "id=" + id + ", subEntryType=" + subEntryType + ", buyer=" + buyer + ", supplier=" + supplier + ", project=" + project + ", quantity=" + quantity + ", rate=" + rate + ", advance=" + advance + ", totalPrice=" + totalPrice + ", isActive=" + isActive + ", createdDate=" + createdDate + ", updateDate=" + updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubEntryType() {
        return subEntryType;
    }

    public void setSubEntryType(String subEntryType) {
        this.subEntryType = subEntryType;
    }

    public EntriesDirectDetails getEntriesDirectDetails() {
        return entriesDirectDetails;
    }

    public void setEntriesDirectDetails(EntriesDirectDetails entriesDirectDetails) {
        this.entriesDirectDetails = entriesDirectDetails;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPlantBilty() {
        return plantBilty;
    }

    public void setPlantBilty(Integer plantBilty) {
        this.plantBilty = plantBilty;
    }

    public Integer getRecipientBilty() {
        return recipientBilty;
    }

    public void setRecipientBilty(Integer recipientBilty) {
        this.recipientBilty = recipientBilty;
    }

    public String getAsphaltType() {
        return asphaltType;
    }

    public void setAsphaltType(String asphaltType) {
        this.asphaltType = asphaltType;
    }

    public String getAsphaltTon() {
        return asphaltTon;
    }

    public void setAsphaltTon(String asphaltTon) {
        this.asphaltTon = asphaltTon;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

}
