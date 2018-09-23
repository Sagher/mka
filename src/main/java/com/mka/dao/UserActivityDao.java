package com.mka.dao;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.model.User;
import com.mka.model.UserActivity;
import java.util.List;

public interface UserActivityDao {

    public boolean addUserActivity(UserActivity userActivity);

    public List<UserActivity> getUserActivity(User user, int startIndex, int fetchSize, String orderBy);

    public int getUserActivityCount(User user);
}
