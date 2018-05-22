package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.Category;

import java.util.List;

/**
 * Created by chao on 5/21/18.
 */
public interface ICategoryService {
    ServerResponse addCategory (String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getParallelSubcategory(Integer categoryId);

    ServerResponse getSubcategory (Integer categoryId);
}
