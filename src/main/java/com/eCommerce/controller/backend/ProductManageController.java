package com.eCommerce.controller.backend;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.Product;
import com.eCommerce.pojo.User;
import com.eCommerce.service.IFileService;
import com.eCommerce.service.IProductService;
import com.eCommerce.service.IUserService;
import com.eCommerce.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by chao on 5/22/18.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    // 1. save product
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave (HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("no authority");
        }
        else {
            // service implement:
            return iProductService.saveOrUpdateProduct(product);
        }
    }

    // 2. update product status
    @RequestMapping("set_product_status.do")
    @ResponseBody
    public ServerResponse setProductStatus (HttpSession session,
                                            Integer productId,
                                            Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("no authority");
        }
        else {
            // service implement:
            return iProductService.setProductStatus(productId, status);
        }
    }


    // 3. get product detail
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail (HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("no authority");
        }
        else {
            // service implement:
            return iProductService.manageProductDetail(productId);
        }
    }

    // 4. get product list
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNum",defaultValue = "1")
                                  int pageNumber,
                                  @RequestParam(value = "pageSize",defaultValue = "10")
                                  int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("no authority");
        }
        else {
            // service implement:
            return iProductService.getProductList(pageNumber,pageSize);
        }
    }

    // 5. product search(return with soem order)
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,
                                        String productName,
                                        Integer productId,
                                        @RequestParam(value = "pageNum",defaultValue = "1")
                                        int pageNumber,
                                        @RequestParam(value = "pageSize",defaultValue = "10")
                                        int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("no authority");
        }
        else {
            // service implement:
            return iProductService.searchProduct(productName, productId, pageNumber, pageSize);
        }
    }

    // 6. image upload
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false)
                                 MultipartFile file,
                                 HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("no authority");
        } else {
            // service implement:
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);
        }
    }

        /*
                Simditor json
                {
                    "success": true/false,
                    "msg": "error message", # optional
                    "file_path": "[real file path]"
                 }
         */
        // 7. richtext image upload
        @RequestMapping("richtext_img_upload.do")
        @ResponseBody
        public Map richtextImgUpload(HttpSession session,
                                     @RequestParam(value = "upload_file", required = false)
                                                MultipartFile file,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {

            Map resultMap = Maps.newHashMap();
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            if (user == null) {
                resultMap.put("success", false);
                resultMap.put("msg", "please login and try again");
                return resultMap;
            }


            if (!iUserService.checkAdminRole(user).isSuccess()) {
                resultMap.put("success", false);
                resultMap.put("msg", "admin authority is needed");
                return resultMap;
            }
            else {
                // service implement:
                String path = request.getSession().getServletContext().getRealPath("upload");
                String targetFileName = iFileService.upload(file,path);
                if (StringUtils.isBlank(targetFileName)) {
                    resultMap.put("success", false);
                    resultMap.put("msg", "Upload failed");
                    return resultMap;
                }
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
                resultMap.put("success", true);
                resultMap.put("msg", "success");
                resultMap.put("file_path", url);
                response.addHeader("Access-Control-Allow-Headers","X-File-Name");
                return resultMap;
            }
        }









}
