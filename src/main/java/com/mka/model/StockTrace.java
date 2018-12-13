/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sagher Mehmood
 */
@Entity
@Table(name = "stock_trace_direct_entries")
@NamedQueries({
    @NamedQuery(name = "StockTrace.findAll", query = "SELECT s FROM StockTrace s")})
public class StockTrace implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "consume_unit")
    private int consumeUnit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "consume_amount")
    private BigDecimal consumeAmount;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "sales_amount")
    private BigDecimal salesAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "purchase_amount")
    private BigDecimal purchaseAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock_amount")
    private BigDecimal stockAmount;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "average_unit_price")
    private BigDecimal averageUnitPrice;

    @Size(max = 50)
    @Column(name = "sub_type")
    private String subType;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "month")
    private String month;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sales_unit")
    private int salesUnit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "purchase_unit")
    private int purchaseUnit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock_units")
    private int stockUnits;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "div_bg_theme_class")
    private String divBgThemeClass;
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private EntryItems type;

    public int getItemId() {
        return type.getId();
    }

    public String getItemName() {
        return type.getItemName();
    }

    public String getItemUnit() {
        return type.getItemUnit();
    }

    public StockTrace() {
    }

    public StockTrace(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(int salesUnit) {
        this.salesUnit = salesUnit;
    }

    public int getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(int purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public int getStockUnits() {
        return stockUnits;
    }

    public void setStockUnits(int stockUnits) {
        this.stockUnits = stockUnits;
    }

    public String getDivBgThemeClass() {
        return divBgThemeClass;
    }

    public void setDivBgThemeClass(String divBgThemeClass) {
        this.divBgThemeClass = divBgThemeClass;
    }

    public EntryItems getType() {
        return type;
    }

    public void setType(EntryItems type) {
        this.type = type;
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
        if (!(object instanceof StockTrace)) {
            return false;
        }
        StockTrace other = (StockTrace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + ", month=" + month + ", salesUnit=" + salesUnit + ", salesAmount=" + salesAmount + ", purchaseUnit=" + purchaseUnit + ", purchaseAmount=" + purchaseAmount + ", stockUnits=" + stockUnits + ", stockAmount=" + stockAmount + ", averageUnitPrice=" + averageUnitPrice + ", divBgThemeClass=" + divBgThemeClass + ", type=" + type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public BigDecimal getAverageUnitPrice() {
        return averageUnitPrice;
    }

    public void setAverageUnitPrice(BigDecimal averageUnitPrice) {
        this.averageUnitPrice = averageUnitPrice;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = stockAmount;
    }

    public int getConsumeUnit() {
        return consumeUnit;
    }

    public void setConsumeUnit(int consumeUnit) {
        this.consumeUnit = consumeUnit;
    }

    public BigDecimal getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(BigDecimal consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

}
