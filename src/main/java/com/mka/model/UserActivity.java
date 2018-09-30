/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.persistence.Lob;
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
@Table(name = "user_activity")
@NamedQueries({
    @NamedQuery(name = "UserActivity.findAll", query = "SELECT u FROM UserActivity u")})
public class UserActivity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "action_type")
    private String actionType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 65535)
    @Column(name = "action_description")
    private String actionDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "remote_addr")
    private String remoteAddr;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "user_agent")
    private String userAgent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actionTimestamp;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User userId;

    public UserActivity() {
    }

    public UserActivity(Integer id) {
        this.id = id;
    }

    public UserActivity(Integer id, String actionType, String actionDescription, String remoteAddr, String userAgent, Date actionTimestamp) {
        this.id = id;
        this.actionType = actionType;
        this.actionDescription = actionDescription;
        this.remoteAddr = remoteAddr;
        this.userAgent = userAgent;
        this.actionTimestamp = actionTimestamp;
    }

    public UserActivity(String actionType, String actionDescription, String remoteAddr, String userAgent, Date actionTimestamp, User userId) {
        this.actionType = actionType;
        this.actionDescription = actionDescription;
        this.remoteAddr = remoteAddr;
        this.userAgent = userAgent;
        this.actionTimestamp = actionTimestamp;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getActionTimestamp() {
        return actionTimestamp;
    }

    public void setActionTimestamp(Date actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof UserActivity)) {
            return false;
        }
        UserActivity other = (UserActivity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserActivity[" + "actionType=" + actionType + ", actionDescription=" + actionDescription + ", remoteAddr=" + remoteAddr + ", actionTimestamp=" + actionTimestamp + ']';
    }

}
