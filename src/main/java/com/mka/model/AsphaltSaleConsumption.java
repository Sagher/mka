/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
@Table(name = "asphalt_sale_consumption")
@NamedQueries({
    @NamedQuery(name = "AsphaltSaleConsumption.findAll", query = "SELECT a FROM AsphaltSaleConsumption a")})
public class AsphaltSaleConsumption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "item_name")
    private String itemName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_quantity")
    private int itemQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_rate")
    private int itemRate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_amount")
    private int itemAmount;
    @JoinColumn(name = "asphlat_sale_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private AsphaltSales asphlatSaleId;

    public AsphaltSaleConsumption() {
    }

    public AsphaltSaleConsumption(Integer id) {
        this.id = id;
    }

    public AsphaltSaleConsumption(Integer id, String itemName, int itemQuantity, int itemRate, int itemAmount) {
        this.id = id;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemRate = itemRate;
        this.itemAmount = itemAmount;
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

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getItemRate() {
        return itemRate;
    }

    public void setItemRate(int itemRate) {
        this.itemRate = itemRate;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public AsphaltSales getAsphlatSaleId() {
        return asphlatSaleId;
    }

    public void setAsphlatSaleId(AsphaltSales asphlatSaleId) {
        this.asphlatSaleId = asphlatSaleId;
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
        if (!(object instanceof AsphaltSaleConsumption)) {
            return false;
        }
        AsphaltSaleConsumption other = (AsphaltSaleConsumption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mka.model.AsphaltSaleConsumption[ id=" + id + " ]";
    }
    
}
