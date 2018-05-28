package com.eCommerce.service.impl;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.dao.CategoryMapper;
import com.eCommerce.dao.ProductMapper;
import com.eCommerce.pojo.Category;
import com.eCommerce.pojo.Product;
import com.eCommerce.service.ICategoryService;
import com.eCommerce.service.IProductService;
import com.eCommerce.util.DateTimeUtil;
import com.eCommerce.util.PropertiesUtil;
import com.eCommerce.vo.ProductDetailVo;
import com.eCommerce.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.sun.corba.se.spi.activation.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ICategoryService iCategoryService;

    // backend part

    public ServerResponse saveOrUpdateProduct (Product product) {
        if (product != null) {
            if (StringUtils.isBlank(product.getMainImage())) {
                // get sub-image, split by ,
                String[] subImages = product.getSubImages().split(",");
                // set first image as main image
                if (subImages.length > 0) {
                    product.setMainImage(subImages[0]);
                }
            }

            if (product.getId() != null) {
                // product exists
                int count = productMapper.updateByPrimaryKey(product);
                if (count > 0) {
                    return ServerResponse.createBySuccess("Update successfully!");
                }
                return ServerResponse.createByErrorMessage("failed");
            }
            else {
                // add new product
                int count = productMapper.insert(product);
                if (count > 0) {
                    return ServerResponse.createBySuccess("new product added!");
                }
                return ServerResponse.createByErrorMessage("failed");
            }
        }
        return ServerResponse.createByErrorMessage("update or add product failed");
    }

    public ServerResponse<String> setProductStatus(Integer productId, Integer status) {
        if(productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int count =  productMapper.updateByPrimaryKeySelective(product);
        if (count > 0) {
            return ServerResponse.createBySuccess("product status updated");
        }
        return ServerResponse.createByErrorMessage("set product status failed");

    }


    // manage* -> backend part
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("product not found");
        }
        // value object
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        // img host
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.mywebsite.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
           productDetailVo.setParentCategoryId(0);
        }
        else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        // set create time
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public ServerResponse<PageInfo> getProductList(int pageNumber, int pageSize) {

        //start page
        PageHelper.startPage(pageNumber, pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product p : productList) {
            ProductListVo productListVo = assembleProductListVo(p);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.mywebsite.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    public ServerResponse<PageInfo> searchProduct(String productName,
                                                  Integer productId,
                                                  int pageNumber,
                                                  int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        if(StringUtils.isNotBlank(productName)){
            // rebuild product name string
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product p : productList) {
            ProductListVo productListVo = assembleProductListVo(p);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    // portal part
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("product not found");
        }
        if (product.getStatus() != Const.ProductStatusEnum.AVAILABLE_NOW.getCode()) {
            return ServerResponse.createByErrorMessage("Product is not available now");
        }
        // value object
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,
                                                                Integer categoryId,
                                                                int pageNum,
                                                                int pageSize,
                                                                String orderBy){
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        // check categoryId
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getSubcategory(category.getId()).getData();
        }
        // check keyword
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //dynamic sort
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
