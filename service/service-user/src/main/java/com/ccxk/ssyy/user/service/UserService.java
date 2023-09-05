package com.ccxk.ssyy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.enums.user.User;
import com.ccxk.ssyy.vo.user.LeaderAddressVo;
import com.ccxk.ssyy.vo.user.UserLoginVo;

public interface UserService extends IService<User> {
    User getUserByOpenId(String openid);

    LeaderAddressVo getLeaderAddressByUserId(Long id);

    UserLoginVo getUserLoginVo(Long id);
}
