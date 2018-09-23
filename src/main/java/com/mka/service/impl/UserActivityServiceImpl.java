package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.UserActivityDao;
import com.mka.model.User;
import com.mka.model.UserActivity;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityService")
public class UserActivityServiceImpl implements UserActivityService {

    private static final Logger log = Logger.getLogger(UserActivityServiceImpl.class);

    @Autowired
    UserActivityDao activityDao;

    @Override
    public boolean addUserActivity(UserActivity userActivity) {
        return activityDao.addUserActivity(userActivity);
    }

    @Override
    public List<UserActivity> getUserActivity(User user, int startIndex, int fetchSize, String orderBy) {
        return activityDao.getUserActivity(user, startIndex, fetchSize, orderBy);
    }

    @Override
    public int getUserActivityCount(User user) {
        return activityDao.getUserActivityCount(user);
    }

}
