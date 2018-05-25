package com.eCommerce.service.impl;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.dao.CategoryMapper;
import com.eCommerce.pojo.Category;
import com.eCommerce.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by chao on 5/21/18.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory (String categoryName, Integer parentId) {
        if (parentId == null || org.apache.commons.lang3.StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("Invalid parameter");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true); // true -> this category is current available

        int count = categoryMapper.insert(category);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("Category is added!");
        }
        return ServerResponse.createByErrorMessage("Failed");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName) {
        if (categoryId == null || org.apache.commons.lang3.StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("Invalid parameter");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("Category name updated!");
        }
        return ServerResponse.createByErrorMessage("Failed");
    }

    public ServerResponse<List<Category>> getParallelSubcategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("Subcategory is not found");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse<List<Integer>> getSubcategory (Integer categoryId) {
        Set<Category> set = Sets.newHashSet();
        findChildCategory(set, categoryId);

        List<Integer> list = Lists.newArrayList();
        for (Category c : set) {
            list.add(c.getId());
        }
        return ServerResponse.createBySuccess(list);
    }

    // find children recursively
    private Set<Category>  findChildCategory(Set<Category> set, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            set.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category c : categoryList) {
            findChildCategory(set, c.getId());
        }
        return set;
    }




}
