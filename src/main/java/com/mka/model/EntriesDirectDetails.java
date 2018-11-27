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
@Table(name = "entries_direct_details")
@NamedQueries({
    @NamedQuery(name = "EntriesDirectDetails.findAll", query = "SELECT e FROM EntriesDirectDetails e")})
public class EntriesDirectDetails implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_unloaded")
    private BigDecimal totalUnloaded;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unloading_cost")
    private BigDecimal unloadingCost;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sub_type")
    private String subType;
    @Column(name = "unloading_party")
    private String unloadingParty;
    @JoinColumn(name = "entry_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private EntriesDirect entryId;

    public EntriesDirectDetails() {
    }

    public EntriesDirectDetails(Integer id) {
        this.id = id;
    }

    public EntriesDirectDetails(Integer id, String subType, BigDecimal totalUnloaded, BigDecimal unloadingCost, String unloadingParty) {
        this.id = id;
        this.subType = subType;
        this.totalUnloaded = totalUnloaded;
        this.unloadingCost = unloadingCost;
        this.unloadingParty = unloadingParty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getUnloadingParty() {
        return unloadingParty;
    }

    public void setUnloadingParty(String unloadingParty) {
        this.unloadingParty = unloadingParty;
    }

    public EntriesDirect getEntryId() {
        return entryId;
    }

    public void setEntryId(EntriesDirect entryId) {
        this.entryId = entryId;
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
        if (!(object instanceof EntriesDirectDetails)) {
            return false;
        }
        EntriesDirectDetails other = (EntriesDirectDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mka.model.EntriesDirectDetails[ id=" + id + " ]";
    }

    public BigDecimal getTotalUnloaded() {
        return totalUnloaded;
    }

    public void setTotalUnloaded(BigDecimal totalUnloaded) {
        this.totalUnloaded = totalUnloaded;
    }

    public BigDecimal getUnloadingCost() {
        return unloadingCost;
    }

    public void setUnloadingCost(BigDecimal unloadingCost) {
        this.unloadingCost = unloadingCost;
    }

}
