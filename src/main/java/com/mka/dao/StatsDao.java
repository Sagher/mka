/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao;

import com.mka.model.response.StatItem;
import com.mka.model.response.Stats;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface StatsDao {

    public List<StatItem> getStats();
}
