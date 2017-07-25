package com.example.fpl_racewaiking.db;

import java.util.List;

import com.example.fpl_racewaiking.app.MyApplication;

import android.content.Context;
import de.greenrobot.dao.query.QueryBuilder;
import ww.greendao.dao.DaoSession;
import ww.greendao.dao.dl_referee;
import ww.greendao.dao.dl_refereeDao;
import ww.greendao.dao.pf_athlete;
import ww.greendao.dao.pf_athleteDao;
import ww.greendao.dao.pf_foul;
import ww.greendao.dao.pf_foulDao;
import ww.greendao.dao.pf_group;
import ww.greendao.dao.pf_groupDao;

public class DbService {
	private static DbService instance;
	private static Context appContext;
	public static DaoSession mDaoSession;
	public static dl_refereeDao refereeDao;
	public static pf_athleteDao athleteDao;
	public static pf_foulDao foulDao;
	public static pf_groupDao groupDao;

	public static DbService getInstance(Context context) {
		if (instance == null) {
			instance = new DbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			DbService.mDaoSession = MyApplication.getDaoSession(context);
			DbService.athleteDao = DbService.mDaoSession.getPf_athleteDao();
			DbService.foulDao = DbService.mDaoSession.getPf_foulDao();
			DbService.groupDao = DbService.mDaoSession.getPf_groupDao();
			DbService.refereeDao = DbService.mDaoSession.getDl_refereeDao();
		}

		return instance;
	}

	// *********************************************存储方法****************************************

	/**
	 * 存储当前登录的裁判员基本信息对象---单个存储
	 * 
	 * @param referee
	 * @return
	 */
	public long saveReferee(dl_referee referee) {
		return refereeDao.insertOrReplace(referee);
	}

	/**
	 * 存储当前登录的裁判员基本信息对象集合---批量存储
	 * 
	 * @param dl_referees
	 */
	public void saveRefereeList(final List<dl_referee> dl_referees) {
		if (dl_referees == null || dl_referees.isEmpty()) {
			return;
		}
		refereeDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < dl_referees.size(); i++) {
					dl_referee dl_referee = dl_referees.get(i);
					refereeDao.insertOrReplace(dl_referee);
				}
			}
		});
	}

	/**
	 * 存储运动员基本信息对象---单个存储
	 * 
	 * @param referee
	 * @return
	 */
	public long saveAthlete(pf_athlete athlete) {
		return athleteDao.insertOrReplace(athlete);
	}

	/**
	 * 存储运动员基本信息对象集合---批量存储
	 * 
	 * @param pf_athletes
	 */
	public void saveAthleteList(final List<pf_athlete> pf_athletes) {
		if (pf_athletes == null || pf_athletes.isEmpty()) {
			return;
		}
		athleteDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < pf_athletes.size(); i++) {
					pf_athlete pf_athlete = pf_athletes.get(i);
					athleteDao.insertOrReplace(pf_athlete);
				}
			}
		});
	}

	/**
	 * 存储判罚信息对象---单个存储
	 * 
	 * @param referee
	 * @return
	 */
	public long saveFoul(pf_foul foul) {
		return foulDao.insertOrReplace(foul);
	}

	/**
	 * 存储判罚信息对象集合---批量存储
	 * 
	 * @param pf_athletes
	 */
	public void saveFoulList(final List<pf_foul> pf_fouls) {
		if (pf_fouls == null || pf_fouls.isEmpty()) {
			return;
		}
		foulDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < pf_fouls.size(); i++) {
					pf_foul pf_foul = pf_fouls.get(i);
					foulDao.insertOrReplace(pf_foul);
				}
			}
		});
	}

	/**
	 * 存储分组信息对象---单个存储
	 * 
	 * @param referee
	 * @return
	 */
	public long saveGroup(pf_group group) {
		return groupDao.insertOrReplace(group);
	}

	/**
	 * 存储分组信息对象集合---批量存储
	 * 
	 * @param pf_athletes
	 */
	public void saveGroupList(final List<pf_group> pf_groups) {
		if (pf_groups == null || pf_groups.isEmpty()) {
			return;
		}
		groupDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < pf_groups.size(); i++) {
					pf_group pf_group = pf_groups.get(i);
					groupDao.insertOrReplace(pf_group);
				}
			}
		});
	}

	// ********************************************删除方法******************************************

	// ********************************************查找方法******************************************

	/**
	 * 查询全部裁判员信息
	 * 
	 * @return
	 */
	public List<dl_referee> loadAllReferee() {
		return refereeDao.loadAll();
	}

	public dl_referee queryRefereeByName(String name) {
		QueryBuilder<dl_referee> qb = refereeDao.queryBuilder();
		dl_referee referee = qb.where(dl_refereeDao.Properties.Referee_CHN_name.eq(name)).unique();
		return referee;
	}

	/**
	 * 查询全部运动员信息
	 * 
	 * @return
	 */
	public List<pf_athlete> loadAllAthlete() {
		return athleteDao.loadAll();
	}

	/**
	 * 根据运动员编号查询
	 * 
	 * @param id
	 * @return
	 */
	public pf_athlete queryAthleteByID(String id) {
		QueryBuilder<pf_athlete> qb = athleteDao.queryBuilder();
		pf_athlete athlete = qb.where(pf_athleteDao.Properties.Athlete_ID.eq(id)).unique();
		return athlete;
	}

	/**
	 * 根据运动员序号查询
	 * 
	 * @param order
	 * @return
	 */
	public pf_athlete queryAthleteByOrder(int order) {
		QueryBuilder<pf_athlete> qb = athleteDao.queryBuilder();
		pf_athlete athlete = qb.where(pf_athleteDao.Properties.Athlete_order.eq(order)).unique();
		return athlete;
	}

	/**
	 * 查询全部判罚信息
	 * 
	 * @return
	 */
	public List<pf_foul> loadAllFoul() {
		return foulDao.loadAll();
	}

	public List<pf_foul> queryFoulByAthleteOrderAndJudge(String name, int order) {
		QueryBuilder<pf_athlete> qb1 = athleteDao.queryBuilder();
		pf_athlete athlete = qb1.where(pf_athleteDao.Properties.Athlete_order.eq(order)).unique();
		QueryBuilder<pf_foul> qb = foulDao.queryBuilder();
		List<pf_foul> fouls = qb.where(qb.and(pf_foulDao.Properties.Foul_referee_name.eq(name),
				pf_foulDao.Properties.Foul_athlete_ID.eq(athlete.getAthlete_ID()),
				pf_foulDao.Properties.Foul_card.eq(1))).list();
		return fouls;
	}

	public List<pf_foul> queryFoulByAthleteIdAndJudge(String name, String id) {
		QueryBuilder<pf_foul> qb = foulDao.queryBuilder();
		List<pf_foul> fouls = qb.where(qb.and(pf_foulDao.Properties.Foul_referee_name.eq(name),
				pf_foulDao.Properties.Foul_athlete_ID.eq(id), pf_foulDao.Properties.Foul_card.eq(1))).list();
		return fouls;
	}

	public List<pf_foul> queryFoulByjudgeAndCard(String name, int card) {
		QueryBuilder<pf_foul> qb = foulDao.queryBuilder();
		List<pf_foul> fouls = qb.where(
				qb.and(pf_foulDao.Properties.Foul_referee_name.eq(name), pf_foulDao.Properties.Foul_card.eq(card)))
				.list();
		return fouls;
	}

	public List<pf_foul> queryFoulByjudgeAndCardAndItem(String name, int card, String item) {
		QueryBuilder<pf_group> qb1 = groupDao.queryBuilder();
		pf_group group = qb1.where(pf_groupDao.Properties.Group_CHN_content.eq(item)).unique();
		QueryBuilder<pf_foul> qb = foulDao.queryBuilder();
		List<pf_foul> fouls = qb
				.where(qb.and(pf_foulDao.Properties.Group_ID.eq(group.getGroup_ID()),
						pf_foulDao.Properties.Foul_referee_name.eq(name), pf_foulDao.Properties.Foul_card.eq(card)))
				.list();
		return fouls;
	}

	public List<pf_foul> queryFoulByjudgeName(String name) {
		QueryBuilder<pf_foul> qb = foulDao.queryBuilder();
		List<pf_foul> fouls = qb.where(pf_foulDao.Properties.Foul_referee_name.eq(name)).list();
		return fouls;
	}

	/**
	 * 查询全部分组信息
	 * 
	 * @return
	 */
	public List<pf_group> loadAllGroup() {
		return groupDao.loadAll();
	}

	public pf_group queryGroupByGroupID(Long id) {
		QueryBuilder<pf_group> qb = groupDao.queryBuilder();
		pf_group group = qb.where(pf_groupDao.Properties.Group_ID.eq(id)).unique();
		return group;
	}

}
