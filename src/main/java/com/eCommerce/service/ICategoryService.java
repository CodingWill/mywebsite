package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.Category;

import java.util.List;


public interface ICategoryService {
    ServerResponse addCategory (String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getParallelSubcategory(Integer categoryId);

    ServerResponse<List<Integer>> getSubcategory (Integer categoryId);
}
