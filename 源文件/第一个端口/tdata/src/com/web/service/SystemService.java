package com.web.service;

import java.io.IOException;
import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.dao.PermissionDao;
import com.web.dao.PlayerDao;
import com.web.dao.ProxyDao;
import com.web.dao.RoleDao;
import com.web.dao.SaleRecordDao;
import com.web.dao.UserDao;
import com.web.entity.User;
import com.web.persistence.Page;
import com.web.security.DES;
import com.web.entity.Permission;
import com.web.entity.Player;
import com.web.entity.Proxy;
import com.web.entity.Role;
import com.web.entity.SaleRecord;

/**
 * 系统service类
 * @author hz
 * */
@Service("systemService")
@Transactional(readOnly = false)
public class SystemService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final String HASH_MD5 = "MD5";
	public static final String HASH_DES = "DES";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	UserDao userDao;
	@Autowired
	SaleRecordDao srDao;
	@Autowired
	PlayerDao playerDao;
	@Autowired
	PermissionDao permissionDao;
	@Autowired
	RoleDao roleDao;
	@Autowired
	ProxyDao proxyDao;
	//user
	public void save(User user){
		userDao.save(user);
	}
	public void update(User user){
		userDao.updateOnly(user);
	}
	public List<User> getUserByRole(Role role){
		return (List<User>) userDao.getByHqlList("from User where role.id = '" + role.getId() + "'");
		
	}
	public User getUserByLoginId(String loginName) {
		return userDao.findByLoginId(loginName);
	}
	public User getUserByName(String name){
		return userDao.findByName(name);
	}
	//saleRecord
	public void save(SaleRecord sr){
		srDao.save(sr);
	}
	/**
	 * 用于查找销售记录
	 * @param SaleRecord
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidKeyException 
	 * @throws ParseException 
	 */
	public Page<SaleRecord> find(Page<SaleRecord> page,String playerId,String searchDate) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ParseException{
		DetachedCriteria dc = srDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(searchDate)){
			System.out.println(searchDate);
			String[] arr = searchDate.split(" ");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			System.out.println(arr[0] +" 00:00:00");
			System.out.println(sdf.format(sdf.parse(arr[0] +" 00:00:00"))+"-------------");
			System.out.println(sdf.format(sdf.parse(arr[2] +" 00:00:00"))+"-------------");
			dc.add(Restrictions.ge("playerRechargeDate",sdf.parse(arr[0] +" 00:00:00") ));//小的 时间段
			dc.add(Restrictions.le("playerRechargeDate",sdf.parse(arr[2] +" 23:59:59") ));//大的时间段
		}
		if(StringUtils.isNotEmpty(playerId)){
			System.out.println("playerId:" + playerId);
			dc.add(Restrictions.eq("playerId", playerId));
		}
		return srDao.find(page, dc);
	}
	
	/**
	 * 存储玩家*/
	public void save(Player player){
		playerDao.saveOnly(player);
	}
	/**
	 * 更新玩家
	 * */
	public void update(Player player){
		playerDao.save(player);
	}
	/**
	 * 删除玩家*/
	public void delete(Player player){
		DES des = new DES();
		playerDao.deleteForId(player.getpId());
	}
	/**
	 * 根据ID查找玩家*/
	public Player getPlayerNameById(String id) {
		return playerDao.findByPlayerId(id);
	}
	/**
	 * 更新玩家房卡数量*/
	public void updatePlayerNumber(Player player,String add) throws IOException, Exception{
		DES des = new DES();
		String old = new String(des.decrypt(des.base64decoder.decodeBuffer(player.getpNumber()),des.PASSWORD));
		player.setpNumber(des.base64encoder.encode(des.encrypt((String.valueOf(Integer.parseInt(old) + Integer.parseInt(add))).getBytes(),des.PASSWORD)));
		playerDao.save(player);
	}
	/**
	 * 增加权限信息*/
	public void save(Permission permission){
		permissionDao.save(permission);
	}
	/**
	 * 查找所有权限*/
	public List<Permission> findAllList(){
		List<Permission> list = permissionDao.createQuery("from Permission").list();
		return list;
	}
	
	/**
	 * 增加角色信息
	 * */
	public void save(Role role){
		roleDao.save(role);
	}
	
	/**
	 * 查找roleID通过name
	 * */
	public Role findRoleByName(String s){
		return roleDao.getByHql(s);
	}
	//player start
	public Page<Player> find(Page<Player> page,Player player) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ParseException{
		DES des = new DES();
		DetachedCriteria dc = playerDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(player.getpId())){
			System.out.println(player.getpId() + "----------------");
			dc.add(Restrictions.eq("pId",des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD))));
		}
		if(StringUtils.isNotEmpty(player.getpWechat())){
			System.out.println(player.getpWechat() + "----------------");
			dc.add(Restrictions.eq("pWechat", des.base64encoder.encode(des.encrypt(player.getpWechat().getBytes(), des.PASSWORD))));
		}
		return playerDao.find(page, dc);
	}
	//end player
	//proxy start
	/**
	 * 存储代理角色信息
	 * @param proxy
	 */
	public void save(Proxy proxy){
		proxyDao.saveOnly(proxy);
	}
	/**
	 * 代理信息分页
	 * @param page 分页
	 * @param phone 手机号码
	 * @param loginId 登录账号ID
	 * @return List 分页列表
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws ParseException
	 */
	public Page<Proxy> findProxy(Page<Proxy> page,String phone,String loginId) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ParseException{
		DetachedCriteria dc = proxyDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(phone)){//
			System.out.println("phone:-------------" + phone);
			dc.add(Restrictions.like("proxyPhone",phone,MatchMode.ANYWHERE));
		}else if(StringUtils.isNotBlank(loginId)){
			System.out.println("loginId:-------------" + loginId);
			dc.add(Restrictions.like("user.loginId",loginId,MatchMode.ANYWHERE));
		}
		return proxyDao.find(page, dc);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 查找所有代理用户
	 * @return
	 */
	public List<Proxy> findAllProxy(){
		return (List<Proxy>)proxyDao.getByHqlList("from Proxy");
	}
	/**
	 * 更新代理商信息
	 * @param proxy
	 */
	public void update(Proxy proxy){
		proxyDao.updateOnly(proxy);
	}
	
	public Proxy getProxy(String id){
		return proxyDao.get(id);
	}
	//end proxy
}
