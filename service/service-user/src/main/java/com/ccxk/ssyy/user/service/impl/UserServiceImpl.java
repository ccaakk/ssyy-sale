package com.ccxk.ssyy.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccxk.ssyy.enums.user.Leader;
import com.ccxk.ssyy.enums.user.User;
import com.ccxk.ssyy.enums.user.UserDelivery;
import com.ccxk.ssyy.user.mapper.LeaderMapper;
import com.ccxk.ssyy.user.mapper.UserDeliveryMapper;
import com.ccxk.ssyy.user.mapper.UserMapper;
import com.ccxk.ssyy.user.service.UserService;
import com.ccxk.ssyy.vo.user.LeaderAddressVo;
import com.ccxk.ssyy.vo.user.UserLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private LeaderMapper leaderMapper;

    @Autowired
    private UserDeliveryMapper userDeliveryMapper;

    @Override
    public User getUserByOpenId(String openid) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openid));
        return user;
    }

    @Override
    public LeaderAddressVo getLeaderAddressByUserId(Long id) {
        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>()
                .eq(UserDelivery::getUserId, id)
                .eq(UserDelivery::getIsDefault, 1));
        if (userDelivery == null){
             return null;
        }
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());
        //封装数据
        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtils.copyProperties(leader, leaderAddressVo);
        leaderAddressVo.setUserId(id);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    @Override
    public UserLoginVo getUserLoginVo(Long id) {
        User user = baseMapper.selectById(id);
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(id);
        userLoginVo.setNickName(user.getNickName());
        userLoginVo.setPhotoUrl(user.getPhotoUrl());
        userLoginVo.setIsNew(user.getIsNew());
        userLoginVo.setOpenId(user.getOpenId());

        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>()
                .eq(UserDelivery::getUserId, id)
                .eq(UserDelivery::getIsDefault,1));
        if (userDelivery != null) {
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
            userLoginVo.setWareId(userDelivery.getWareId());
        }else {
            userLoginVo.setLeaderId(1L);
            userLoginVo.setWareId(1L);
        }
        return userLoginVo;
    }
}
