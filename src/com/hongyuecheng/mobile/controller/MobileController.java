package com.hongyuecheng.mobile.controller;

import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.checkplan.entity.CheckResult;
import com.hongyuecheng.checkplan.service.CheckOptionService;
import com.hongyuecheng.checkplan.service.CheckPlanService;
import com.hongyuecheng.checkplan.service.CheckResultService;
import com.hongyuecheng.common.Constants;
import com.hongyuecheng.report.entity.CheckRecordDetail;
import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.report.service.CheckRecordDetailService;
import com.hongyuecheng.report.service.CheckRecordsService;
import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.user.service.UserService;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;
import com.hongyuecheng.utils.PropertiesMapper;
import com.hongyuecheng.utils.ReturnValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/mobile")
//@MultipartConfig(fileSizeThreshold = 0, maxFileSize = 52428800l, maxRequestSize = 52428800l)
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
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request) {
        request.setAttribute("msg", "");
        return "mobile/list";
    }

    @RequestMapping("/resetPwd")
    public String resetPwd() {
        return "mobile/resetPwd";
    }

    @RequestMapping("/saveUserInfo")
    @ResponseBody
    public ReturnValue saveUserInfo(String oldPassword, String newPassword, HttpSession session) {
        ReturnValue returnValue = new ReturnValue();
        try {
            User user = (User) session.getAttribute("user");
            if (!StringUtils.equals(user.getPassword(), oldPassword)) {
                returnValue.setFlag(false);
                returnValue.setMsg("旧密码输入错误!");
            } else {
                user.setPassword(newPassword);
                userService.updateUser(user);
                returnValue.setFlag(true);
            }

        } catch (Exception e) {
            returnValue.setFlag(false);
            returnValue.setMsg("系统错误!");
        }

        return returnValue;
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
                // 检查是否有抽查计划
                CheckPlan plan = checkPlanService.getPlanByDate(DateUtil.parse(DateUtil.format(new Date(), DateUtil.FORMAT_TYPE_1), DateUtil.FORMAT_TYPE_1));
                if (null != plan) {
                    List<ShopInfo> shopInfos = shopInfoService.getShopInfos(plan.getShopIds());
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(shopInfos)) {
                        return "mobile/checkResult";
                    }
                }
                request.setAttribute("msg", "今日无抽查计划,请先添加抽查计划再填写抽查结果!");
                return "mobile/list";
            } else {
                String checkDate = DateUtil.format(new Date(), DateUtil.FORMAT_TYPE_1);
                Integer shopId = user.getShopId();
                CheckRecords ret = checkRecordsService.getCheckRecords(checkDate, shopId, checkType);
                if (null == ret) {
                    ret = new CheckRecords();
                    ret.setShopId(shopId);
                    ret.setShopInfo(shopInfoService.getShopInfo(shopId));
                    ret.setRecordType(checkType);
                    ret.setUserId(user.getId());
                    checkRecordsService.initCheckRecords(ret);
                }
                if (null != ret.getShopInfo()) {
                    Integer optionType = Constants.CHECK_OPTION_TYPE_NORMAL;
                    ;
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
                ret.setCheckDate(new Date());
                ret.setCheckDateStr(DateUtil.format(ret.getCheckDate(), DateUtil.FORMAT_TYPE_1));
                request.setAttribute("checkRecords", ret);
                request.setAttribute("submitFlag", checkSubmit(checkType));
                return "mobile/checkDetail";
            }
        }
    }

    /**
     * 判断当前时间是否可以编辑
     *
     * @param checkType
     * @return
     */
    private boolean checkSubmit(Integer checkType) {
        boolean flag = false;
        Date current = new Date();
        String start = DateUtil.format(current, DateUtil.FORMAT_TYPE_1) + " ";
        String end = DateUtil.format(current, DateUtil.FORMAT_TYPE_1) + " ";
        if (checkType.intValue() == Constants.CHECK_RECORD_TYPE_OPEN.intValue()) {
            start += PropertiesMapper.getValue("opencheck_start");
            end += PropertiesMapper.getValue("opencheck_end");
        } else if (checkType.intValue() == Constants.CHECK_RECORD_TYPE_CLOSE.intValue()) {
            start += PropertiesMapper.getValue("closecheck_start");
            end += PropertiesMapper.getValue("closecheck_end");
        } else {
            return flag;
        }
        Date startDate = DateUtil.parse(start);
        Date endDate = DateUtil.parse(end);
        // 结束时间必须大于开始时间
        if (endDate.getTime() <= startDate.getTime()) {
            // 如果还没过中午12点，则endDate加一天
            if (current.getTime() >= startDate.getTime() && current.getTime() >= endDate.getTime()) {
                // 结束时间按加一天处理
                endDate = DateUtil.addDays(endDate, 1);
            } else if (current.getTime() <= startDate.getTime() && current.getTime() <= endDate.getTime()) {
                // 开始时间减一天处理
                startDate = DateUtil.addDays(startDate, -1);
            }
        }
        if (current.getTime() >= startDate.getTime() && current.getTime() <= endDate.getTime()) {
            flag = true;
        }
        return flag;
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "mobile/mobileLogin";
    }

    @RequestMapping(value = "/mobileMain", method = RequestMethod.GET)
    public String mobileMain(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null != user) {
            return "mobile/list";
        } else {
            return "mobile/mobileLogin";
        }
    }

    @RequestMapping(value = "/saveRecordDetails", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue saveRecordDetails(@RequestBody CheckRecords checkRecords, HttpSession session) {
        ReturnValue returnValue = new ReturnValue();
        if (null == checkRecords) {
            returnValue.setFlag(false);
            returnValue.setMsg("参数错误保存失败");
            return returnValue;
        }
        try {
            checkRecords.setCheckDate(new Date());
            if (null == checkRecords.getRecordId()) {// 新增
                Integer recordId = checkRecordsService.addCheckRecords(checkRecords);
                checkRecords.setRecordId(recordId);
                List<CheckRecordDetail> details = checkRecords.getDetails();
                for (CheckRecordDetail checkRecordDetail : details) {
                    checkRecordDetail.setRecordId(checkRecords.getRecordId());
                    checkRecordDetailService.addCheckRecordDetail(checkRecordDetail);
                }
            } else {// 更新
                checkRecordsService.updateCheckRecords(checkRecords);
                List<CheckRecordDetail> details = checkRecords.getDetails();
                for (CheckRecordDetail checkRecordDetail : details) {
                    checkRecordDetail.setRecordId(checkRecords.getRecordId());
                    checkRecordDetailService.updateCheckRecordDetail(checkRecordDetail);
                }
            }
            returnValue.setFlag(true);
            returnValue.setData(checkRecords);

            if (checkRecords.getRecordType().intValue() == Constants.CHECK_RECORD_TYPE_CLOSE.intValue()) {
                CheckPlan plan = checkPlanService.getPlanByDateAndShop(DateUtil.parse(DateUtil.format(new Date(), DateUtil.FORMAT_TYPE_1), DateUtil.FORMAT_TYPE_1), checkRecords.getShopId());
                if (null != plan) {
                    returnValue.setMsg("保存成功,但本店今日有抽查,请在抽查人员抽查完成之后再离店!");
                }
            }
        } catch (Exception e) {
            returnValue.setFlag(false);
            returnValue.setMsg("保存数据错误");
        }
        return returnValue;
    }

    @RequestMapping(value = "/saveCheckRecordDetail", method = RequestMethod.POST)
    @ResponseBody
    public String saveCheckRecordDetail(Integer shopId, Integer checkType, Integer recordId, String optionCode, Integer optionResult, HttpSession session) {
        try {
            String checkDate = getCheckDate(checkType);
            CheckRecords ret = checkRecordsService.getCheckRecords(checkDate, shopId, checkType);
            if (null == ret) {
                ret = new CheckRecords();
                ret.setShopId(shopId);
                ret.setCheckDate(new Date());
                ret.setRecordType(checkType);
                User user = (User) session.getAttribute("user");
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

            return "保存成功";
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
                                DefaultMultipartHttpServletRequest request, HttpServletResponse response) {
        boolean isNew = false;
        CheckResult checkResult = checkResultService.getCheckResult(planId, shopId);
        if (null == checkResult) {
            isNew = true;
            checkResult = new CheckResult();
        }
        checkResult.setPlanId(planId);
        checkResult.setShopId(shopId);
        checkResult.setStatus(status);
        checkResult.setDescription(description);
        StringBuffer picture = new StringBuffer();
        try {
            //说明输入的请求信息采用UTF-8编码方式
            request.setCharacterEncoding("utf-8");
            int num = 0;
            boolean flag = true;
            //Servlet3.0中新引入的方法，用来处理multipart/form-data类型编码的表单
            MultiValueMap<String, MultipartFile> fileMap = request.getMultiFileMap();
            Set<String> fileNameKeys = fileMap.keySet();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(fileNameKeys)) {
                for (String sourceFileName : fileNameKeys) {
                    List<MultipartFile> files = fileMap.get(sourceFileName);
                    if (org.apache.commons.collections.CollectionUtils.isEmpty(files)) {
                        continue;
                    }
                    MultipartFile multipartFile = files.get(0);
                    String fileType = multipartFile.getOriginalFilename();
                    fileType = fileType.substring(fileType.lastIndexOf(".") + 1, fileType.length());
                    String saveFileName = shopId + "_" + planId + "_" + num + "." + fileType;
                    //获得存储上传文件的文件夹路径
                    String fileSavingFolder = request.getSession().getServletContext().getRealPath("") + File.separator + "check_result_images";
                    //获得存储上传文件的完整路径（文件夹路径+文件名）
                    //文件夹位置固定，文件夹采用与上传文件的原始名字相同
                    String fileSavingPath = fileSavingFolder + File.separator + saveFileName;
                    //如果存储上传文件的文件夹不存在，则创建文件夹
                    File f = new File(fileSavingFolder + File.separator);
                    if (!f.exists()) {
                        f.mkdirs();
                    }

                    File image = new File(fileSavingPath);
                    multipartFile.transferTo(image);

                    //将上传的文件内容写入服务器文件中
                    picture.append(saveFileName).append(";");

                    num++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (picture.length() > 0) {
            checkResult.setImageNames(picture.substring(0, picture.length() - 1));
        } else {
            if (StringUtils.isNotEmpty(checkResult.getImageNames()) && !isNew) {

            } else {
                checkResult.setImageNames("");
            }
        }
        if (isNew) {
            checkResultService.addCheckResult(checkResult);
        } else {
            checkResultService.updateCheckResult(checkResult);
        }

        try {
            setRespone(response, "保存成功,刷新以查看提交图片");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setRespone(HttpServletResponse response, String info) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(info);
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
        CheckResult res = checkResultService.getCheckResult(planId, shopId);
        if (null == res) {
            return new CheckResult();
        } else {
            return res;
        }
    }

    @RequestMapping(value = "/deleteImage", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteImage(Integer shopId, Integer planId, String imageName, HttpServletRequest request) {
        CheckResult res = checkResultService.getCheckResult(planId, shopId);
        if (null == res || StringUtils.isBlank(imageName)) {
        } else {
            String images = res.getImageNames();
            if (images.indexOf(";") != -1) {
                StringBuffer newImages = new StringBuffer();
                String[] imageArr = images.split(";");
                if (null == imageArr || imageArr.length <= 1) {
                } else {
                    for (String iName : imageArr) {
                        if (imageName.equalsIgnoreCase(iName)) {
                            continue;
                        } else {
                            newImages.append(iName).append(";");
                        }
                    }

                    if (newImages.length() > 0) {
                        res.setImageNames(newImages.substring(0, newImages.length() - 1));
                        checkResultService.updateCheckResult(res);
                    }
                }

            } else {
                res.setImageNames("");
                checkResultService.updateCheckResult(res);
            }
        }
        // 物理删除实际图片
        //获得存储上传文件的文件夹路径
        String fileSavingFolder = request.getSession().getServletContext().getRealPath("") + File.separator + "check_result_images";
        String fileName = fileSavingFolder + File.separator + imageName;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
        return true;
    }

    private String getCheckDate(int checkType) {
        Date current = new Date();
        String result = DateUtil.format(current, DateUtil.FORMAT_TYPE_1);
        if (checkType == Constants.CHECK_RECORD_TYPE_CLOSE) {
            String start = DateUtil.format(current, DateUtil.FORMAT_TYPE_1) + " " + PropertiesMapper.getValue("closecheck_start");
            String end = DateUtil.format(current, DateUtil.FORMAT_TYPE_1) + " " + PropertiesMapper.getValue("closecheck_end");
            Date startDate = DateUtil.parse(start);
            Date endDate = DateUtil.parse(end);
            // 结束时间必须大于开始时间
            if (endDate.getTime() <= startDate.getTime()) {
                // 如果还没过中午12点，则endDate加一天
                if (current.getTime() >= startDate.getTime() && current.getTime() >= endDate.getTime()) {
                    // 结束时间按加一天处理
                    endDate = DateUtil.addDays(endDate, 1);
                } else if (current.getTime() <= startDate.getTime() && current.getTime() <= endDate.getTime()) {
                    // 开始时间减一天处理
                    startDate = DateUtil.addDays(startDate, -1);
                }
                // 获取开始时间的年月日
                result = DateUtil.format(startDate, DateUtil.FORMAT_TYPE_1);
            }
        }
        return result;
    }
}
