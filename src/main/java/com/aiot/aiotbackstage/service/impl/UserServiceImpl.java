package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.config.WeChatConfig;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.util.JWTUtil;
import com.aiot.aiotbackstage.common.util.JsonUtils;
import com.aiot.aiotbackstage.common.util.MD5Utils;
import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.UserLoginParam;
import com.aiot.aiotbackstage.model.param.UserParam;
import com.aiot.aiotbackstage.model.vo.Code2SessionVo;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.TokenVo;
import com.aiot.aiotbackstage.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @ClassName UserManageServiceImpl
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper roleUserMapper;

    @Autowired
    private SysDustRecMapper dustRecMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 微信的 code2session 接口 获取微信用户信息
     */
    private String code2Session(String jsCode) {
        log.info("-------jsCode--------",jsCode);
        String urlString = "?appid={app_id}&secret={app_secret}&js_code={jsCode}&grant_type={grant_type}";
        return  restTemplate.getForObject(
                weChatConfig.getSession_host() + urlString, String.class,
                weChatConfig.getApp_id(),
                weChatConfig.getApp_secret(),
                jsCode,
                weChatConfig.getGrant_type());
    }

    /**
     * 1 . 我们的微信小程序端传入code。
     * 2 . 调用微信code2session接口获取openid和session_key
     * 3 . 根据openid和session_key自定义登陆态(Token)
     * 4 . 返回自定义登陆态(Token)给小程序端。
     * 5 . 我们的小程序端调用其他需要认证的api，请在header的Authorization里面携带 token信息
     * @return Token 返回后端 自定义登陆态  token  基于JWT实现
     */
    @Override
    public TokenVo userLogin(UserLoginParam userLoginParam) {

        //如果code为空则PC端登陆
        if(userLoginParam.getLoginType() == 2){
            SysUserEntity sysUserEntity = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                    .eq(SysUserEntity::getUserName, userLoginParam.getUserName()));
            if(ObjectUtils.isEmpty(sysUserEntity)){
                throw new MyException(ResultStatusCode.USER_NAME_NO_EXIT);
            }
            if(!sysUserEntity.getPassword().equals(userLoginParam.getPassword())){
                throw new MyException(ResultStatusCode.USER_PASSWORD_NO_EXIT);
            }
            //5 . JWT 返回自定义登陆态 Token
            String token = jwtUtil.createToken(sysUserEntity);
            return new TokenVo(token);
        }else{
            //1 . code2session返回JSON数据
            String resultJson = code2Session(userLoginParam.getCode());
            //2 . 解析数据
            Code2SessionVo response = JsonUtils.jsonToPojo(resultJson, Code2SessionVo.class);
            if (!response.getErrcode().equals("0")) {
                log.info("code2session失败:", response.getErrmsg());
                throw new AuthenticationException("code2session失败 : " + response.getErrmsg());
            }else {
                //3 . 先从本地数据库中查找用户是否存在
                SysUserEntity sysUserEntity = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                        .eq(SysUserEntity::getWxOpenid, response.getOpenid()));
                if (ObjectUtils.isEmpty(sysUserEntity)) {
                    sysUserEntity=new SysUserEntity();
                    sysUserEntity.setWxOpenid(response.getOpenid());  //不存在就新建用户
                    sysUserEntity.setSessionKey(response.getSession_key());
                    sysUserEntity.setLoginType(1);
                    sysUserEntity.setCreateTime(new Date());
                    sysUserEntity.setUpdateTime(new Date());
                    userMapper.insert(sysUserEntity);
                }
                log.info("sysUserEntity----[{}]", JsonUtils.objectToJson(sysUserEntity));
                //4 . 更新sessionKey和登陆时间
                sysUserEntity.setSessionKey(response.getSession_key());
                sysUserEntity.setUpdateTime(new Date());
                userMapper.updateById(sysUserEntity);
                //5 . JWT 返回自定义登陆态 Token
                String token = jwtUtil.createToken(sysUserEntity);
                return new TokenVo(token);
            }
        }

    }

    @Override
    public void loginOut(String token) {
        //用户退出逻辑
        if (StringUtils.isEmpty(token)) {
            throw new MyException(ResultStatusCode.LOGIN_OUT_ERR);
        }
        String username = jwtUtil.getUserNameByToken(token);
        String jwtId = jwtUtil.getJwtIdByToken(token);
        String wxOpenId = jwtUtil.getWxOpenIdByToken(token);
        String sessionKey = jwtUtil.getSessionKeyByToken(token);
        SysUserEntity sysUser;
        if(StringUtils.isNotBlank(username)){
            sysUser = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                    .eq(SysUserEntity::getUserName,username));
        }else{
            sysUser = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                    .eq(SysUserEntity::getWxOpenid,wxOpenId)
                    .eq(SysUserEntity::getSessionKey,sessionKey));
        }
        if (sysUser != null) {
            //清空用户Token缓存
            redisUtils.del("JWT-SESSION-" + jwtId);
        } else {
            throw new MyException(ResultStatusCode.TOKEN_NO_EXIT);
        }
    }

    @Autowired
    private SysInsectRecMapper insectRecMapper;

    @Autowired
    private SysDeviceErrorRecMapper deviceErrorRecMapper;
//    private SysSensorRecMapper sensorRecMapper;
    @Override
    public void test() {

        batchSave ();

    }
    /**
     * 检查用户信息是否存在
     * @param userParam
     */
    private void checkUserInfo(UserParam userParam){
        //查询该用户是否存在
        SysUserEntity userEntity = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                .eq(SysUserEntity::getUserName, userParam.getUserName()));
        if (ObjectUtils.isNotEmpty(userEntity)) {
            throw new MyException(ResultStatusCode.USER_HAS_EXISTED);
        }
    }
    @Override
    public void saveUser(UserParam userParam) {
        checkUserInfo(userParam);
        SysUserEntity userEntity=new SysUserEntity();
        userEntity.setUserName(userParam.getUserName());
        userEntity.setPassword(userParam.getPassword());
        userEntity.setLoginType(2);
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        userMapper.insert(userEntity);
        SysUserRoleEntity userRoleEntity=new SysUserRoleEntity();
        userRoleEntity.setUserId(userEntity.getUserId());
        userRoleEntity.setRoleId(userParam.getRoleId());
        userRoleMapper.insert(userRoleEntity);
    }

    @Override
    public void updateUser(UserParam userParam) {
        SysUserEntity phoneEntity = SysUserEntity.builder().userId(userParam.getUserId()).build();
        if (ObjectUtils.isEmpty(phoneEntity)) {
            throw new MyException(ResultStatusCode.USER_HAS_NO_EXISTED);
        }
        SysUserEntity userEntity=new SysUserEntity();
        userEntity.setUserId(phoneEntity.getUserId());
        userEntity.setUserName(userParam.getUserName());
        String encryptedPassword = MD5Utils.encrypt(userParam.getPassword());
        userEntity.setPassword(encryptedPassword);
        userEntity.setUpdateTime(new Date());
        //修改用户信息
        userMapper.updateById(userEntity);
        //修改用户角色
        SysUserRoleEntity userRoleEntity=new SysUserRoleEntity();
        userRoleEntity.setRoleId(userParam.getRoleId());
        userRoleMapper.update(userRoleEntity,Wrappers.<SysUserRoleEntity>lambdaQuery()
                .eq(SysUserRoleEntity::getUserId,userEntity.getUserId()));
    }

    @Override
    public PageResult<SysUserEntity> userPage(PageParam param) {
        PageParam pageQuery=new PageParam();
        pageQuery.setPageSize(param.getPageSize());
        pageQuery.setPageNumber(param.getPageNumber());
        //查询后台用户
        List<SysUserEntity> sysUserEntities = userMapper.userPageInfo(pageQuery);
        sysUserEntities.stream().forEach(userEntity -> {
            List<SysUserRoleEntity> sysUserRoleEntityList = userRoleMapper.selectList(Wrappers.<SysUserRoleEntity>lambdaQuery()
                    .eq(SysUserRoleEntity::getUserId, userEntity.getUserId()));
            sysUserRoleEntityList.stream().forEach(sysUserRoleEntity -> {
                SysRoleEntity sysRoleEntities = roleMapper.selectById(sysUserRoleEntity.getRoleId());
                if(ObjectUtils.isNotEmpty(sysRoleEntities)){
                    userEntity.setRoleName(sysRoleEntities.getRoleName());
                    userEntity.setRoleId(sysRoleEntities.getRoleId());
                }
            });
        });
        Integer total = userMapper.selectCount(null);
        return PageResult.<SysUserEntity>builder().total(total).pageData(sysUserEntities).build();
    }

    @Override
    public void delUser(Long id) {
        SysUserEntity phoneEntity = SysUserEntity.builder().userId(id).build();
        if (ObjectUtils.isEmpty(phoneEntity)) {
            throw new MyException(ResultStatusCode.USER_HAS_NO_EXISTED);
        }
        //删除该用户
        userMapper.deleteById(id);
        //删除该用户对应的角色数据
        userRoleMapper.delete(Wrappers.<SysUserRoleEntity>lambdaQuery()
                .eq(SysUserRoleEntity::getUserId,id));
    }

    @Override
    public void isToken(String token) {
        String jwtId = jwtUtil.getJwtIdByToken(token);
        String b = (String) redisUtils.get("JWT-SESSION-" + jwtId);
        if(b == null || b.equals("")){
            throw new MyException(ResultStatusCode.TOKEN_NO_EXIT);
        }
    }

    @Override
    public List<Map<String,Object>> permissionInfo(String token){

        Long userId = jwtUtil.getUserIdByToken(token);
        List<SysUserRoleEntity> sysUserRoleEntities = roleUserMapper
                .selectList(Wrappers.<SysUserRoleEntity>lambdaQuery()
                        .eq(SysUserRoleEntity::getUserId, userId));
        List<Long> rootIds = sysUserRoleEntities.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toList());
        List<SysRoleEntity> roles = roleMapper.selectBatchIds(rootIds);
        List<Map<String,Object>> permissions=new ArrayList<>();
        if(roles.size()>0) {
            for(SysRoleEntity role : roles) {
                Map<String,Object> map=new HashMap<>();
                map.put("roleId",role.getRoleId());
                List<SysRoleMenuEntity> sysRoleMenuEntities = sysRoleMenuMapper
                        .selectList(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                                .eq(SysRoleMenuEntity::getRoleId, role.getRoleId()));
                List<Long> menuIds = sysRoleMenuEntities.stream()
                        .map(SysRoleMenuEntity::getMenuId).collect(Collectors.toList());
                List<SysMenuEntity> sysMenuEntities = sysMenuMapper.selectList(Wrappers.<SysMenuEntity>lambdaQuery()
                        .eq(SysMenuEntity::getType, "menu")
                        .in(SysMenuEntity::getId, menuIds));
                //此处构建树
                map.put("perms",getTree(sysMenuEntities));
                permissions.add(map);
            }
        }
        return permissions;

    }

    private List<SysMenuEntity> getTree(List<SysMenuEntity> list) {

        List<SysMenuEntity> baseLists = new ArrayList<>();

        // 总菜单，出一级菜单，一级菜单没有父id
        for (SysMenuEntity e: list) {
            if( e.getParentId().equals(0L) ){
                baseLists.add( e );
            }
        }
        // 遍历一级菜单
        for (SysMenuEntity e: baseLists) {
            // 将子元素 set进一级菜单里面
            e.setChildren( getChild(e.getId(),list) );
        }
        return baseLists;
    }

    /**
     * 获取子节点
     * @param pid
     * @param elements
     * @return
     */
    private List<SysMenuEntity> getChild(Long  pid , List<SysMenuEntity> elements){
        List<SysMenuEntity> childs = new ArrayList<>();
        for (SysMenuEntity e: elements) {
            if(!e.getParentId().equals(0L)){
                if(e.getParentId().equals( pid )){
                    // 子菜单的下级菜单
                    childs.add( e );
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (SysMenuEntity e: childs) {
            // 继续添加子元素
            e.setChildren( getChild( e.getId() , elements ) );
        }
        //停下来的条件，如果 没有子元素了，则停下来
        if( childs.size()==0 ){
            return null;
        }
        return childs;
    }



    public Date parseDate (String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = format.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public void  batchSave () {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Task(this.parseDate("2017-01-01 00:00:00"), this.parseDate("2017-03-01 00:00:00")));
        cachedThreadPool.execute(new Task(this.parseDate("2017-03-01 00:00:00"), this.parseDate("2017-06-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2017-06-01 00:00:00"), this.parseDate("2017-09-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2017-09-01 00:00:00"), this.parseDate("2018-01-01 00:00:00")));
//
//        cachedThreadPool.execute(new Task(this.parseDate("2018-01-01 00:00:00"), this.parseDate("2018-03-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2018-03-01 00:00:00"), this.parseDate("2018-06-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2018-06-01 00:00:00"), this.parseDate("2018-09-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2018-09-01 00:00:00"), this.parseDate("2019-01-01 00:00:00")));
//
//        cachedThreadPool.execute(new Task(this.parseDate("2019-01-01 00:00:00"), this.parseDate("2019-03-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2019-03-01 00:00:00"), this.parseDate("2019-06-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2019-06-01 00:00:00"), this.parseDate("2019-09-01 00:00:00")));
//        cachedThreadPool.execute(new Task(this.parseDate("2019-09-01 00:00:00"), this.parseDate("2020-01-01 00:00:00")));
//
//        cachedThreadPool.execute(new Task(this.parseDate("2020-01-01 00:00:00"), this.parseDate("2020-03-01 00:00:00")));

    }
    class Task implements Runnable {
        private Date sdate;
        private Date edate;

        public Task(Date sdate,Date edate) {
            this.sdate = sdate;
            this.edate = edate;
        }

        @Override
        public void run() {
            System.out.println("测试数据生成中..."+sdate);
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            log.info("1---------{}",formatter.format(sdate));
            while (sdate.before(edate)) {
                DecimalFormat df = new DecimalFormat( "0.00" );
                // sdate=每加五分钟的值
                // sdate = +5m
                // 执行插入语句，建议批量提交事物
//                SysInsectRecEntity insectRecEntity=new SysInsectRecEntity();
//                insectRecEntity.setDeviceId((int)(Math.random()*14)+1);
//                insectRecEntity.setImage("https://aiot-obs.obs.cn-north-4.myhuaweicloud.com/菜青虫.jpg");
//                insectRecEntity.setResultImage("https://aiot-obs.obs.cn-north-4.myhuaweicloud.com/菜青虫.jpg");
//                insectRecEntity.setResult(((int)(Math.random()*262))+","+((int)(Math.random()*100))
//                        +"#"+((int)(Math.random()*262))+","+((int)(Math.random()*100)));
//
//                insectRecMapper.insert(insectRecEntity);
//                SysDeviceErrorRecEntity deviceErrorRecEntity=new SysDeviceErrorRecEntity();
//                deviceErrorRecEntity.setSiteId((int)(Math.random()*8)+1);
//                deviceErrorRecEntity.setDeviceType("InsectDevice");
//                deviceErrorRecEntity.setSubType("zhan-wei");
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(sdate);
//                cal.add(Calendar.DATE, 1);
//                Date time = cal.getTime();
//                sdate=time;
//                deviceErrorRecEntity.setEndTime(sdate);
//                cal.setTime(sdate);
//                cal.add(Calendar.MINUTE,(int)(Math.random()*60));
//                Date time1 = cal.getTime();
//                deviceErrorRecEntity.setStartTime(time1);
//                deviceErrorRecEntity.setCreateTime(new Date());
//                deviceErrorRecMapper.insert(deviceErrorRecEntity);


//                SysDustRecEntity dustRecEntity=new SysDustRecEntity();
//                dustRecEntity.setDepth(10);
//                dustRecEntity.setEc(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity.setEpsilon(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity.setSalinity(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity.setSiteId((int)(Math.random()*8)+1);
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(sdate);
//                cal.add(Calendar.MINUTE, 5);
//                Date time = cal.getTime();
//                sdate=time;
//                dustRecEntity.setTime(sdate);
//                dustRecEntity.setTds(Double.parseDouble(df.format( Math.random()*40))+20);
//                dustRecEntity.setTemperature(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity.setWc(Double.parseDouble(df.format( Math.random()*40))+20);
//                dustRecMapper.insert(dustRecEntity);
//////
//                SysDustRecEntity dustRecEntity1=new SysDustRecEntity();
//                dustRecEntity1.setDepth(20);
//                dustRecEntity1.setEc(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity1.setEpsilon(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity1.setSalinity(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity1.setSiteId((int)(Math.random()*8)+1);
//                dustRecEntity1.setTime(sdate);
//                dustRecEntity1.setTds(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity1.setTemperature(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity1.setWc(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecMapper.insert(dustRecEntity1);
////
//                SysDustRecEntity dustRecEntity2=new SysDustRecEntity();
//                dustRecEntity2.setDepth(40);
//                dustRecEntity2.setEc(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity2.setEpsilon(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity2.setSalinity(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity2.setSiteId((int)(Math.random()*8)+1);
//                dustRecEntity2.setTime(sdate);
//                dustRecEntity2.setTds(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity2.setTemperature(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecEntity2.setWc(Double.parseDouble(df.format( Math.random()*40)+20));
//                dustRecMapper.insert(dustRecEntity2);

//                SysSensorRecEntity dustRec=new SysSensorRecEntity();
//                dustRec.setSiteId((int)(Math.random()*8)+1);
//                dustRec.setSensor("wind_speed");
//                dustRec.setValue(df.format( Math.random()*20));
//
//                sensorRecMapper.insert(dustRec);
//
//                SysSensorRecEntity dustRec1=new SysSensorRecEntity();
//                dustRec1.setSiteId((int)(Math.random()*8)+1);
//                dustRec1.setSensor("wind_direction");
//
//                dustRec1.setValue(df.format( Math.random()*20));
//                dustRec1.setTime(sdate);
//                sensorRecMapper.insert(dustRec1);
//
//                SysSensorRecEntity dustRec2=new SysSensorRecEntity();
//                dustRec2.setSiteId((int)(Math.random()*8)+1);
//                dustRec2.setSensor("humidity");
//                dustRec2.setValue(df.format( Math.random()*40));
//                dustRec2.setTime(sdate);
//                sensorRecMapper.insert(dustRec2);
//
//                SysSensorRecEntity dustRec3=new SysSensorRecEntity();
//                dustRec3.setSiteId((int)(Math.random()*8)+1);
//                dustRec3.setSensor("temperature");
//                dustRec3.setValue(df.format( Math.random()*50));
//                dustRec3.setTime(sdate);
//                sensorRecMapper.insert(dustRec3);
//
//                SysSensorRecEntity dustRec4=new SysSensorRecEntity();
//                dustRec4.setSiteId((int)(Math.random()*8)+1);
//                dustRec4.setSensor("noise");
//                dustRec4.setValue(df.format( Math.random()*50));
//                dustRec4.setTime(sdate);
//                sensorRecMapper.insert(dustRec4);
//
//                SysSensorRecEntity dustRec5=new SysSensorRecEntity();
//                dustRec5.setSiteId((int)(Math.random()*8)+1);
//                dustRec5.setSensor("PM25");
//                dustRec5.setValue(df.format( Math.random()*100));
//                dustRec5.setTime(sdate);
//                sensorRecMapper.insert(dustRec5);
//
//                SysSensorRecEntity dustRec6=new SysSensorRecEntity();
//                dustRec6.setSiteId((int)(Math.random()*8)+1);
//                dustRec6.setSensor("PM10");
//                dustRec6.setValue(df.format( Math.random()*100));
//                dustRec6.setTime(sdate);
//                sensorRecMapper.insert(dustRec6);
//
//                SysSensorRecEntity dustRec7=new SysSensorRecEntity();
//                dustRec7.setSiteId((int)(Math.random()*8)+1);
//                dustRec7.setSensor("atmos");
//                dustRec7.setValue(df.format( Math.random()*200));
//                dustRec7.setTime(sdate);
//                sensorRecMapper.insert(dustRec7);
//                log.info("2---------{}",formatter.format(sdate));
            }
        }
    }
}
