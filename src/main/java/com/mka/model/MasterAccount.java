/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sagher
 */
@Entity
@Table(name = "master_account")
@NamedQueries({
    @NamedQuery(name = "MasterAccount.findAll", query = "SELECT m FROM MasterAccount m")})
public class MasterAccount implements Serializable {

    @Column(name = "all_receivable")
    private BigDecimal allReceivable;
    @Column(name = "all_payable")
    private BigDecimal allPayable;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cash_in_hand")
    private BigDecimal cashInHand;
    @Column(name = "total_cash")
    private BigDecimal totalCash;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    public MasterAccount() {
    }

    public MasterAccount(Integer id) {
        this.id = id;
    }

    public MasterAccount(Integer id, Date updateAt) {
        this.id = id;
        this.updateAt = updateAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
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
        if (!(object instanceof MasterAccount)) {
            return false;
        }
        MasterAccount other = (MasterAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mka.model.MasterAccount[ id=" + id + " ]";
    }

    public BigDecimal getCashInHand() {
        return cashInHand;
    }

    public void setCashInHand(BigDecimal cashInHand) {
        this.cashInHand = cashInHand;
    }

    public BigDecimal getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }

    public BigDecimal getAllReceivable() {
        return allReceivable;
    }

    public void setAllReceivable(BigDecimal allReceivable) {
        this.allReceivable = allReceivable;
    }

    public BigDecimal getAllPayable() {
        return allPayable;
    }

    public void setAllPayable(BigDecimal allPayable) {
        this.allPayable = allPayable;
    }

}
