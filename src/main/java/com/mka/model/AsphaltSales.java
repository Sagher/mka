/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "asphalt_sales")
@NamedQueries({
    @NamedQuery(name = "AsphaltSales.findAll", query = "SELECT a FROM AsphaltSales a")})
public class AsphaltSales implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "biltee")
    private int biltee;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "vehicle")
    private String vehicle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "type")
    private String type;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ex_plant_rate")
    private BigDecimal exPlantRate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ex_plant_cost")
    private BigDecimal exPlantCost;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_sale_amount")
    private BigDecimal totalSaleAmount;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Size(max = 100)
    @Column(name = "buyer")
    private String buyer;
    @Size(max = 100)
    @Column(name = "project")
    private String project;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asphlatSaleId")
    private List<AsphaltSaleConsumption> asphaltSaleConsumptionList;

    public AsphaltSales() {
    }

    public AsphaltSales(Integer id) {
        this.id = id;
    }

    public AsphaltSales(Integer id, int quantity, BigDecimal totalSaleAmount, boolean isActive, Date createdDate) {
        this.id = id;
        this.quantity = quantity;
        this.totalSaleAmount = totalSaleAmount;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<AsphaltSaleConsumption> getAsphaltSaleConsumptionList() {
        return asphaltSaleConsumptionList;
    }

    public void setAsphaltSaleConsumptionList(List<AsphaltSaleConsumption> asphaltSaleConsumptionList) {
        this.asphaltSaleConsumptionList = asphaltSaleConsumptionList;
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
        if (!(object instanceof AsphaltSales)) {
            return false;
        }
        AsphaltSales other = (AsphaltSales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AsphaltSales{" + "id=" + id + ", quantity=" + quantity + ", buyer=" + buyer + ", project=" + project + ", totalSaleAmount=" + totalSaleAmount + ", entryDate=" + entryDate + '}';
    }

    public BigDecimal getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(BigDecimal totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    public int getBiltee() {
        return biltee;
    }

    public void setBiltee(int biltee) {
        this.biltee = biltee;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getExPlantRate() {
        return exPlantRate;
    }

    public void setExPlantRate(BigDecimal exPlantRate) {
        this.exPlantRate = exPlantRate;
    }

    public BigDecimal getExPlantCost() {
        return exPlantCost;
    }

    public void setExPlantCost(BigDecimal exPlantCost) {
        this.exPlantCost = exPlantCost;
    }

}
