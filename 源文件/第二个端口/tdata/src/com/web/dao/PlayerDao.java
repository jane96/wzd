package com.web.dao;

import org.springframework.stereotype.Repository;

import com.web.entity.Player;
import com.web.entity.User;
/**
 * 玩家dao类
 * @author hz
 */
@Repository
public class PlayerDao extends BaseDao<Player> {
	/**
	 * 通过id获取player实体
	 * @param id 玩家id
	 * @return Player 玩家实体对象
	 */
	public Player findByPlayerId(String id){
		System.out.println("执行查询name");
		return getByHql("from Player where id = '" + id + "'");
	}
}
