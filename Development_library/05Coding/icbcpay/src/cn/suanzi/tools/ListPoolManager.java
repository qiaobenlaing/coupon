package cn.suanzi.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 链表缓冲池
 * 
 * @author 刘卫平
 * @version 1.0.0 2014-1-7
 */
public class ListPoolManager<T> {
	List<T>[] pool;
	boolean[] inUse;

	@SuppressWarnings("unchecked")
	public ListPoolManager(int initialPoolSize) {
		this.pool = new List[initialPoolSize]; // unchecked
		this.inUse = new boolean[initialPoolSize];
		for (int i = this.pool.length - 1; i >= 0; i--) {
			this.pool[i] = new ArrayList<T>();
			this.inUse[i] = false;
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized List<T> getList() {
		for (int i = this.inUse.length - 1; i >= 0; i--) {
			if (!this.inUse[i]) {
				this.inUse[i] = true;
				return this.pool[i];
			}
		}
		boolean[] old_inUse = this.inUse;
		this.inUse = new boolean[old_inUse.length + 10];
		System.arraycopy(old_inUse, 0, this.inUse, 0, old_inUse.length);

		List<T>[] old_pool = this.pool;
		this.pool = new List[old_pool.length + 10]; // unchecked
		System.arraycopy(old_pool, 0, this.pool, 0, old_pool.length);
		for (int i = old_pool.length; i < this.pool.length; i++) {
			this.pool[i] = new ArrayList<T>();
			this.inUse[i] = false;
		}
		this.inUse[(this.pool.length - 1)] = true;
		return this.pool[(this.pool.length - 1)];
	}

	public synchronized void returnList(List<T> v) {
		for (int i = this.inUse.length - 1; i >= 0; i--) {
			if (this.pool[i] == v) {
				this.inUse[i] = false;
				v.clear();
				return;
			}
		}
		throw new RuntimeException("Vector was not obtained from the pool: " + v);
	}
}