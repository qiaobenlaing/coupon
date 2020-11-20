package com.whhft.sysmanage.service.rmi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.NewsCatalog;
import com.whhft.sysmanage.common.entity.NewsCatalogExample;
import com.whhft.sysmanage.common.entity.NewsExample;
import com.whhft.sysmanage.common.model.NewsCatalogExt;
import com.whhft.sysmanage.common.rmi.NewsCatalogService;
import com.whhft.sysmanage.service.mapper.NewsCatalogMapper;
import com.whhft.sysmanage.service.mapper.NewsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("catalogServiceImpl")
@Transactional
public class NewsCatalogServiceImpl extends
		BaseServiceImpl<NewsCatalog, Integer> implements NewsCatalogService {

	@Autowired
	private NewsCatalogMapper catalogMapper;

	@Autowired
	private NewsMapper newsMapper;

	@Override
	public BaseMapper<NewsCatalog, Integer> mapper() {
		return catalogMapper;
	}

	@Override
	public List<NewsCatalogExt> loadCatalogTree() {

		List<NewsCatalogExt> list = new ArrayList<NewsCatalogExt>();

		// 全部栏目Map
		Map<Integer, NewsCatalogExt> map = new LinkedHashMap<Integer, NewsCatalogExt>();

		NewsCatalogExample catalogExample = new NewsCatalogExample();
		catalogExample.setOrderByClause("ORDER_NO DESC");
		// 查询所有栏目
		for (NewsCatalog catalog : catalogMapper
				.selectByExample(catalogExample)) {
			map.put(catalog.getCatalogId(), new NewsCatalogExt(catalog));
		}

		// 遍历所有栏目
		for (Integer catalogId : map.keySet()) {

			// 栏目子节点
			NewsCatalogExt child = map.get(catalogId);

			// 父级栏目ID
			Integer parentCatalogId = map.get(catalogId).getParentCatalogId();

			// //根节点固定ID为1，父节点ID为0,如果不是根节点就可以组装树了
			if (parentCatalogId != 0) {
				// 父节点
				NewsCatalogExt parent = map.get(parentCatalogId);
				// 给父节点添加子节点集合
				if (parent != null) {
					parent.getChildren().add(child);
				}
			}
		}

		// 只返回根节点下的子节点
		for (NewsCatalogExt catalogExt : map.values()) {
			if (catalogExt.getParentCatalogId() == 0) {
				list.add(catalogExt);
			}
		}
		return list;

	}


	@Override
	public boolean catalogNameExist(String catalogName) {
		NewsCatalogExample example = new NewsCatalogExample();
		example.createCriteria().andCatalogNameEqualTo(catalogName);

		List<NewsCatalog> list = catalogMapper.selectByExample(example);

		if (list.get(0).getCatalogName().equals(catalogName)) {
			return true;
		}

		return false;
	}

}