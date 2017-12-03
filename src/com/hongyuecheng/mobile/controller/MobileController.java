package com.hongyuecheng.mobile.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.hongyuecheng.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.checkplan.entity.CheckResult;
import com.hongyuecheng.checkplan.service.CheckOptionService;
import com.hongyuecheng.checkplan.service.CheckPlanService;
import com.hongyuecheng.checkplan.service.CheckResultService;
import com.hongyuecheng.report.entity.CheckRecordDetail;
import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.report.service.CheckRecordDetailService;
import com.hongyuecheng.report.service.CheckRecordsService;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;

@Controller
@RequestMapping("/mobile")
@MultipartConfig
public class MobileController {

    @Autowired
    private CheckRecordsService checkRecordsService;
    @Autowired
    private CheckRecordDetailService checkRecordDetailService;
    @Autowired
    private CheckPlanService checkPlanService;
    @Autowired
    private CheckOptionService checkOptionService;
    @Autowired
    private CheckResultService checkResultService;
    @Autowired
    private ShopInfoService shopInfoService;

    @RequestMapping("/list")
    public String list() {
        return "mobile/list";
    }

    @RequestMapping("/detail")
    public String detail(HttpSession session, Integer checkType, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return "mobile/mobileLogin";
        } else {
            request.setAttribute("checkType", checkType);
            if (-1 == checkType) {
                //抽查页面
                return "mobile/checkResult";
            } else {
                return "mobile/checkDetail";
            }
        }
    }

    @RequestMapping("/login")
    public String login() {
        return "mobile/mobileLogin";
    }

    @RequestMapping(value = "/getCheckRecords", method = RequestMethod.POST)
    @ResponseBody
    public CheckRecords getCheckRecords(Integer shopId, Integer checkType, HttpSession session) {
        String checkDate = DateUtil.format(new Date(), DateUtil.FORMAT_TYPE_1);
        CheckRecords ret = checkRecordsService.getCheckRecords(checkDate, shopId, checkType);
        if (null == ret) {
            ret = new CheckRecords();
            ret.setShopId(shopId);
            ret.setShopInfo(shopInfoService.getShopInfo(shopId));
            ret.setCheckDate(new Date());
            ret.setRecordType(checkType);
            User user = (User) session.getAttribute("user");
            ret.setUserId(user.getId());
            checkRecordsService.initCheckRecords(ret);
        }
        if (null != ret.getShopInfo()) {
            Integer optionType = Constants.CHECK_OPTION_TYPE_NORMAL;;
            if (Constants.SHOP_TYPE_FOOD.intValue() == ret.getShopInfo().getShopType()) {
                optionType = null;
            }
            //需要检查的所有检查项
            List<CheckRecordDetail> detailAll = checkOptionService.getCheckRecordDetailByType(checkType, optionType);

            List<CheckRecordDetail> resDetailList = null;

            //现在已经检查的检查项
            List<CheckRecordDetail> detailList = ret.getDetails();
            if (null != detailList && detailList.size() > 0) {
                resDetailList = new ArrayList<CheckRecordDetail>();
                for (CheckRecordDetail dAll : detailAll) {
                    String optcode = dAll.getOptionCode();
                    for (CheckRecordDetail checkRecordDetail : detailList) {
                        if (optcode.equalsIgnoreCase(checkRecordDetail.getOptionCode())) {
                            dAll.setOptionResult(checkRecordDetail.getOptionResult());
                        }
                    }
                    resDetailList.add(dAll);
                }
                ret.setDetails(resDetailList);
            } else {
                ret.setDetails(detailAll);
            }
        }
        return ret;
    }

    @RequestMapping(value = "/mobileMain", method = RequestMethod.POST)
    public String mobileMain(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null != user) {
            return "mobile/list";
        } else {
            return "mobile/mobileLogin";
        }
    }

    @RequestMapping(value = "/saveCheckRecordDetail", method = RequestMethod.POST)
    @ResponseBody
    public String saveCheckRecordDetail(Integer shopId, Integer checkType, Integer recordId, String optionCode, Integer optionResult, HttpSession session) {
        try {
            String checkDate = DateUtil.format(new Date(), DateUtil.FORMAT_TYPE_1);
            CheckRecords ret = checkRecordsService.getCheckRecords(checkDate, shopId, checkType);
            if (null == ret) {
                ret = new CheckRecords();
                ret.setShopId(shopId);
                ret.setCheckDate(new Date());
                ret.setRecordType(checkType);
                User user = (User) session.getAttribute("user");
                ;
                ret.setUserId(user.getId());
                checkRecordsService.initCheckRecords(ret);
                recordId = checkRecordsService.addCheckRecords(ret);

                CheckRecordDetail detail = new CheckRecordDetail();
                detail.setRecordId(recordId);
                detail.setOptionCode(optionCode);
                detail.setOptionResult(optionResult);
                checkRecordDetailService.addCheckRecordDetail(detail);
            } else {
                List<CheckRecordDetail> detailList = ret.getDetails();
                recordId = ret.getRecordId();
                boolean hasRecord = false;
                //如果已经存在该参数的记录，则更新
                if (null != detailList && detailList.size() > 0) {
                    for (CheckRecordDetail checkRecordDetail : detailList) {
                        if (optionCode.equalsIgnoreCase(checkRecordDetail.getOptionCode())) {
                            checkRecordDetail.setOptionResult(optionResult);
                            checkRecordDetailService.updateCheckRecordDetail(checkRecordDetail);
                            hasRecord = true;
                        }
                    }
                }

                //数据库中没有该参数的记录，就新增
                if (!hasRecord) {
                    CheckRecordDetail detail = new CheckRecordDetail();
                    detail.setRecordId(recordId);
                    detail.setOptionCode(optionCode);
                    detail.setOptionResult(optionResult);
                    checkRecordDetailService.addCheckRecordDetail(detail);
                }
            }

            return "ok";
        } catch (Exception e) {
            return "网络错误，保存数据错误：\n" + e.getMessage();
        }

    }

    /**
     * 获取当天需要抽查的商家信息
     *
     * @return
     */
    @RequestMapping(value = "/queryCheckShopListPlan", method = RequestMethod.POST)
    @ResponseBody
    public Page queryPlan(Page page) {
        if (null == page) {
            page = new Page();
        }
        String date = DateUtil.format(new Date(), DateUtil.FORMAT_TYPE_1);
        Date today = DateUtil.parse(date, DateUtil.FORMAT_TYPE_1);
        checkPlanService.queryPlans(null, today, today, page);
        List<CheckPlan> plans = page.getResult();
        if (null != plans && !plans.isEmpty()) {
            for (CheckPlan checkPlan : plans) {
                checkPlan.setShopList(shopInfoService.getShopInfos(checkPlan.getShopIds()));
            }
        }
        return page;
    }

    /**
     * 保存抽查结果
     *
     * @param shopId
     * @param status
     * @param description
     * @param planId
     * @param request
     * @param response
     */
    @RequestMapping(value = "/saveCheckResult", method = RequestMethod.POST)
    @ResponseBody
    public void saveCheckResult(Integer shopId, Integer status, String description, Integer planId,
                                HttpServletRequest request, HttpServletResponse response) {
        CheckResult checkResult = checkResultService.getCheckResult(planId, shopId);
        if (null == checkResult) {
            checkResult = new CheckResult();
        }
        checkResult.setPlanId(planId);
        checkResult.setShopId(shopId);
        checkResult.setStatus(status);
        checkResult.setDescription(description);
        String picture = null;
        try {
            //说明输入的请求信息采用UTF-8编码方式
            request.setCharacterEncoding("utf-8");
            //文件类型
            String contentType = request.getContentType();
            System.out.println(contentType);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            //Servlet3.0中新引入的方法，用来处理multipart/form-data类型编码的表单
            Part part = request.getPart("imageNames");
            //获取HTTP头信息headerInfo=（form-data; name="file" filename="文件名"）
            String headerInfo = part.getHeader("content-disposition");
            //从HTTP头信息中获取文件名fileName=（文件名）
            String fileName = headerInfo.substring(headerInfo.lastIndexOf("=") + 2, headerInfo.length() - 1);
            //获得存储上传文件的文件夹路径
            String fileSavingFolder = request.getSession().getServletContext().getRealPath("") + File.separator + "check_result_images";
            //获得存储上传文件的完整路径（文件夹路径+文件名）
            //文件夹位置固定，文件夹采用与上传文件的原始名字相同
            String fileSavingPath = fileSavingFolder + File.separator + fileName;
            //如果存储上传文件的文件夹不存在，则创建文件夹
            File f = new File(fileSavingFolder + File.separator);
            if (!f.exists()) {
                f.mkdirs();
            }
            //将上传的文件内容写入服务器文件中
            part.write(fileSavingPath);
            //输出上传成功信息
            out.println("保存成功~！");
            picture = fileSavingPath;
            System.out.println(picture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkResult.setImageNames(picture);
        checkResultService.updateCheckResult(checkResult);
        // return "数据保存成功~！";
    }

    /**
     * 查询指定商家的抽查结果
     *
     * @param shopId
     * @param planId
     * @param session
     * @return
     */
    @RequestMapping(value = "/queryCheckResult", method = RequestMethod.POST)
    @ResponseBody
    public CheckResult queryCheckResult(Integer shopId, Integer planId, HttpSession session) {
        return checkResultService.getCheckResult(planId, shopId);
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, HttpServletResponse response) {
        try {
            //说明输入的请求信息采用UTF-8编码方式  
            request.setCharacterEncoding("utf-8");
            //文件类型
            String contentType = request.getContentType();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            //Servlet3.0中新引入的方法，用来处理multipart/form-data类型编码的表单  
            Part part = request.getPart("file");
            //获取HTTP头信息headerInfo=（form-data; name="file" filename="文件名"）  
            String headerInfo = part.getHeader("content-disposition");
            //从HTTP头信息中获取文件名fileName=（文件名）  
            String fileName = headerInfo.substring(headerInfo.lastIndexOf("=") + 2, headerInfo.length() - 1);
            //获得存储上传文件的文件夹路径  
            String fileSavingFolder = request.getSession().getServletContext().getRealPath("") + File.separator + "check_result_images";
            //获得存储上传文件的完整路径（文件夹路径+文件名）  
            //文件夹位置固定，文件夹采用与上传文件的原始名字相同  
            String fileSavingPath = fileSavingFolder + File.separator + fileName;
            //如果存储上传文件的文件夹不存在，则创建文件夹  
            File f = new File(fileSavingFolder + File.separator);
            if (!f.exists()) {
                f.mkdirs();
            }
            //将上传的文件内容写入服务器文件中  
            part.write(fileSavingPath);
            //输出上传成功信息  
            out.println("文件上传成功~！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
    @ResponseBody
    public void downloadFile(Integer shopId, Integer planId, HttpServletResponse response) {
        try {
            //服务器绝对路径
            String fullFilePath = "";
            System.out.println(fullFilePath);
              /*打开文件，创建File类型的文件对象*/
            File file = new File(fullFilePath);
              /*如果文件存在*/
            if (file.exists()) {
                System.out.println("文件存在");
                  /*获得文件名，并采用UTF-8编码方式进行编码，以解决中文问题*/
                String filename = URLEncoder.encode(file.getName(), "UTF-8");
                System.out.println(filename);
                  /*重置response对象*/
                response.reset();
                //设置文件的类型，xml文件采用text/xml类型，详见MIME类型的说明
                response.setContentType("text/xml");
                //设置HTTP头信息中内容
                response.addHeader("Content-Disposition", "attachment:filename=\"" + filename + "\"");
                //设置文件的长度
                int fileLength = (int) file.length();
                System.out.println(fileLength);
                response.setContentLength(fileLength);
                  /*如果文件长度大于0*/
                if (fileLength != 0) {
                    //创建输入流
                    InputStream inStream = new FileInputStream(file);
                    byte[] buf = new byte[4096];
                    //创建输出流
                    ServletOutputStream servletOS = response.getOutputStream();
                    int readLength;
                    //读取文件内容并写到response的输出流当中
                    while (((readLength = inStream.read(buf)) != -1)) {
                        servletOS.write(buf, 0, readLength);
                    }
                    //关闭输入流
                    inStream.close();
                    //刷新输出缓冲
                    servletOS.flush();
                    //关闭输出流
                    servletOS.close();
                }
            } else {
                System.out.println("文件不存在~！");
                PrintWriter out = response.getWriter();
                out.println("文件 \"" + fullFilePath + "\" 不存在");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
