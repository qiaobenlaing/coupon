package com.whhft.sysmanage.service.rmi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.base.ShiroHelper;
import com.whhft.sysmanage.common.entity.SysUser;
import com.whhft.sysmanage.common.entity.SysUserExample;
import com.whhft.sysmanage.common.entity.UserRoleRs;
import com.whhft.sysmanage.common.entity.UserRoleRsExample;
import com.whhft.sysmanage.common.model.SysUserModel;
import com.whhft.sysmanage.common.page.SysUserPage;
import com.whhft.sysmanage.common.rmi.SysUserService;
import com.whhft.sysmanage.service.mapper.SysAuthMapper;
import com.whhft.sysmanage.service.mapper.SysUserMapper;
import com.whhft.sysmanage.service.mapper.UserRoleRsMapper;
import com.whhft.sysmanage.service.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("sysUserServiceImpl")
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, Integer> implements SysUserService{
	
	@Value("${WhHft.DEFAULT_PASSWORD}") 
	private String defaultPassword = null;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysAuthMapper sysAuthMapper;
	
	@Autowired
	private UserRoleRsMapper userRoleRsMapper;
	
	public BaseMapper<SysUser, Integer> mapper(){
		return sysUserMapper;
	}
	
	@Override
	public PageInfo<SysUser> page(SysUserPage page){
		SysUserExample example = new SysUserExample();
		SysUserExample.Criteria cr = example.createCriteria();
		if(StringUtils.isNotEmpty(page.getLoginName())){
			cr.andLoginNameLike("%"+page.getLoginName()+"%");
		}
		if(StringUtils.isNotEmpty(page.getUserName())){
			cr.andUserNameLike("%"+page.getUserName()+"%");
		}
		if(StringUtils.isNotEmpty(page.getEnabled())){
			cr.andEnabledEqualTo(page.getEnabled());
		}
		/*
		 * 默认不查询匿名用户帐号和admin帐号，其中匿名用户帐号是一个虚拟帐号，用于做日志查询时方便显示之用，
		 * admin这个帐号是超级管理员，不允许操作员变更他的资料
		 */
		cr.andUserIdGreaterThan(1);
		//用户是逻辑删除，这里不要查询被删帐号
		cr.andDelFlagEqualTo("1");
		example.setOrderByClause(page.getOrderByClause());
		PageHelper.startPage(page.getPage(), page.getRows()); 
		List<SysUser> userList = sysUserMapper.selectByExample(example);
		//分页查询要屏蔽密码，整个用户管理功能中只有登录和修改密码的时候需要对密码进行操作
		for(SysUser user: userList){
			user.setUserPwd(null);
		}
		return new PageInfo<SysUser>(userList);
	}
	/*
	 * 用户插入操作比较特殊，在同一个事务中，插入完成后要获取ID，继续插入关联数据
	 * 在一个事务中想要获取流水号是个比较特殊的情况，一般来说MYBATIS只p常就是数字1
	 * 为解决这个问题必须在SysUserMapper.xml的INSERT配置中增加一下配置
	 * keyProperty="userId" useGeneratedKeys="true"
	 * 这段配置对自增主键方式可用，但在ORACLE或者其他数据库下必须做对应处理
	 */
	@Override
	public SysUserModel insert(SysUserModel model) {
		model.setUserPwd(ShiroHelper.generatPwdWithSalt(defaultPassword));
		//未被删除的数据，标志位为“1”，被删除的，默认为“2”或为NULL
		model.setDelFlag("1");
		model.setUpdateTime(DateUtils.getCurrentTime());
		super.insert(model);
		saveUserRoleIds(model.getUserId(), model.getRoleIds());
		return model;
	};
	
	@Override
	public void update(SysUserModel model) {
		//这段代码效率不高，先查密码和删除标记位再写回去，更高效的办法是UPDATE的时候不修改密码即可，但是不在意这一点效率，开发省事
		SysUser oldUser = sysUserMapper.selectByPrimaryKey(model.getUserId());
		model.setUserPwd(oldUser.getUserPwd());
		model.setDelFlag(oldUser.getDelFlag());
		model.setUpdateTime(DateUtils.getCurrentTime());
		super.update(model);
		saveUserRoleIds(model.getUserId(), model.getRoleIds());
	};
	
	//逻辑删除用户时，首先物理删除用户-角色关系，然后把用户标志位设置为“2”
	@Override
	public void delete(Integer id) {
		UserRoleRsExample example = new UserRoleRsExample();
		example.createCriteria().andUserIdEqualTo(id);
		userRoleRsMapper.deleteByExample(example);
		SysUser user = new SysUser();
		user.setUserId(id);
		user.setDelFlag("2");
		sysUserMapper.updateByPrimaryKeySelective(user);
	};

	@Override
	public boolean loginNameValid(Integer userId, String loginName){
		SysUserExample example = new SysUserExample();
		SysUserExample.Criteria cr = example.createCriteria();
		cr.andDelFlagEqualTo("1");
		if(userId != null){
			cr.andUserIdNotEqualTo(userId);
		}
		cr.andLoginNameEqualTo(loginName);
		if(sysUserMapper.selectByExample(example).size() > 0 ){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public List<Integer> getUserRoleIds(Integer userId) {
		UserRoleRsExample example = new UserRoleRsExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Integer> resList = new ArrayList<Integer>();
		for(UserRoleRs rs: userRoleRsMapper.selectByExample(example)){
			resList.add(rs.getRoleId());
		}
		return resList;
	}
	@Override
	public SysUserModel load(Integer userId){
		SysUser user = super.findOne(userId);
		SysUserModel model = new SysUserModel();
		BeanUtils.copyProperties(user, model);
		model.setUserPwd(null);
		UserRoleRsExample example = new UserRoleRsExample();
		example.createCriteria().andUserIdEqualTo(userId);
		String roleIds = "";
		for(UserRoleRs rs: userRoleRsMapper.selectByExample(example)){
			if( roleIds.length() > 0){
				roleIds += ",";
			}
			roleIds += rs.getRoleId();
		}
		model.setRoleIds(roleIds);
		return model;
	}
	@Override
	public void removeAll(String userIds){
		for(String userIdStr: userIds.split(",")){
			if(StringUtils.isNoneEmpty(userIdStr)){
				delete(Integer.parseInt(userIdStr));
			}
		}
	}
	
	/*
	 * 根据用户名读取用户信息，只读未被逻辑删除的用户
	 */
	@Override
	public SysUser getUserByName(String userName){
		return sysUserMapper.getUserByName(userName);
	}
	
	@Override
	public boolean changePassword(Integer userId, String userPwd, String newPwd){
		SysUserExample example = new SysUserExample();
		example.createCriteria().andUserIdEqualTo(userId).andUserPwdEqualTo(ShiroHelper.generatPwdWithSalt(userPwd));
		List<SysUser> userList = sysUserMapper.selectByExample(example);
		//已更改为当前用户修改密码，不需要判断获取的用户数量userList.size() == 1
		if(userId != null){
			SysUser user = new SysUser();
			user.setUserId(userId);
			user.setUserPwd(ShiroHelper.generatPwdWithSalt(newPwd));
			sysUserMapper.updateByPrimaryKeySelective(user);
			return true;
		}else{
			return false;
		}
	}

//	 初始化密码，传入user_id对象，将数据库对应账户密码设为defaultPassward
	@Override
	public boolean initPassword(Integer userId){
		SysUserExample example = new SysUserExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<SysUser> userList = sysUserMapper.selectByExample(example);
		if( userList.size() == 1){
			SysUser user = new SysUser();
			user.setUserId(userId);
			user.setUserPwd(ShiroHelper.generatPwdWithSalt(defaultPassword));
			sysUserMapper.updateByPrimaryKeySelective(user);
			return true;
		}else{
			return false;
		}
	}

	private void saveUserRoleIds(Integer userId, String roleIds) {
		UserRoleRsExample example = new UserRoleRsExample();
		example.createCriteria().andUserIdEqualTo(userId);
		userRoleRsMapper.deleteByExample(example);
		//必须判断一下用户编辑页面上，该用户有没有关联任何角色，如果没有，则无需后续操作
		if(StringUtils.isNoneEmpty(roleIds)){
			for(String roleId: roleIds.split(",")){
				if(StringUtils.isNoneEmpty(roleId)){
					UserRoleRs rs = new UserRoleRs();
					rs.setUserId(userId);
					rs.setRoleId(Integer.parseInt(roleId));
					userRoleRsMapper.insert(rs);
				}
			}
		}
	}
}
