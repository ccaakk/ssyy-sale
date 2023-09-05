package com.ccxk.ssyy.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.ccxk.ssyy.auth.AuthContextHolder;
import com.ccxk.ssyy.common.utils.JwtHelper;
import com.ccxk.ssyy.constant.RedisConst;
import com.ccxk.ssyy.enums.UserType;
import com.ccxk.ssyy.enums.user.Leader;
import com.ccxk.ssyy.enums.user.User;
import com.ccxk.ssyy.enums.user.UserLoginLog;
import com.ccxk.ssyy.exception.SsyyException;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.result.ResultCodeEnum;
import com.ccxk.ssyy.user.service.UserService;
import com.ccxk.ssyy.user.utils.ConstantPropertiesUtil;
import com.ccxk.ssyy.user.utils.HttpClientUtils;
import com.ccxk.ssyy.vo.user.LeaderAddressVo;
import com.ccxk.ssyy.vo.user.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "微信登录获取openid(小程序)")
    @GetMapping("/wxLogin/{code}")
    public Result loginWx(@PathVariable String code){
        //1.得到微信返回code临时票据值
        //2.拿着code + 小程序id + 小程序密钥 请求微信接口服务
        //// 使用HttpClient工具请求
        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;
        StringBuilder url = new StringBuilder()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        String tokenUrl = String.format(url.toString(), wxOpenAppId, wxOpenAppSecret, code);
        //HttpClient发送get请求
        String result = null;
        try {
             result = HttpClientUtils.get(tokenUrl);
        } catch (Exception e) {
            throw new SsyyException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //3.请求微信接口服务,返回两个值session_key,openid
        JSONObject jsonObject = JSONObject.parseObject(result);
        String session_key = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");
        //4.添加为微信用户数据到数据库里面
        User user = userService.getUserByOpenId(openid);
        if (user == null){
            user = new User();
            user.setOpenId(openid);
            user.setNickName(openid);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }
        //5.根据userId查询提货点和团长信息
        ////团长:管理提货点和仓库
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressByUserId(user.getId());
        //6.使用JWT工具根据userId和名称生成token字符串
        String token = JwtHelper.createToken(user.getId(), user.getNickName());
        //7.获取当前登录用户信息,设置有效时间
        UserLoginVo userLoginVo = userService.getUserLoginVo(user.getId());
        redisTemplate.opsForValue()
                .set(RedisConst.USER_LOGIN_KEY_PREFIX+user.getId(),
                        userLoginVo,
                        RedisConst.USERKEY_TIMEOUT,
                         TimeUnit.DAYS);
        //8.封装到map返回
        Map<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("token",token);
        map.put("leaderAddressVo",leaderAddressVo);
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }
}
