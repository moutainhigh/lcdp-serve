package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.bpm.ICalendarService;
import com.redxun.bpm.core.entity.BpmCalCalendar;
import com.redxun.bpm.core.entity.BpmCalSetting;
import com.redxun.bpm.core.entity.BpmCalTimeBlock;
import com.redxun.bpm.core.service.BpmCalCalendarServiceImpl;
import com.redxun.bpm.core.service.BpmCalSettingServiceImpl;
import com.redxun.bpm.core.service.BpmCalTimeBlockServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmCalCalendar")
@ClassDefine(title = "工作日历安排",alias = "bpmCalCalendarController",path = "/bpm/core/bpmCalCalendar",packages = "core",packageName = "流程管理")
@Api(tags = "工作日历安排")
public class BpmCalCalendarController extends BaseController<BpmCalCalendar> {

    @Autowired
    BpmCalCalendarServiceImpl bpmCalCalendarService;

    @Autowired
    BpmCalTimeBlockServiceImpl bpmCalTimeBlockService;

    @Autowired
    BpmCalSettingServiceImpl bpmCalSettingService;

    @Autowired
    ICalendarService iCalendarService;

    @Override
    public BaseService getBaseService() {
        return bpmCalCalendarService;
    }

    @Override
    public String getComment() {
        return "工作日历安排";
    }

    @MethodDefine(title = "日历时间计算测试--时长", path = "/getMinuteBetweenTimeBlock", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "data")})
    @ApiOperation(value = "日历时间计算测试--时长")
    @PostMapping("/getMinuteBetweenTimeBlock")
    @ResponseBody
    public JsonResult getMinuteBetweenTimeBlock(@RequestBody JSONObject data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String settingId =data.getString("settingId");
        String beginDateStr = data.getString("beginDate");
        String endDateStr=data.getString("endDate");
        Date beginDate  =null;
        Date endDate  =null;
        try {
            beginDate  = simpleDateFormat.parse(beginDateStr);
            endDate  = simpleDateFormat.parse(endDateStr);
        }catch (Exception e){
            logger.error("---BpmCalCalendarController.getMinuteBetweenTimeBlock is error--- :"+e.getMessage());
        }
        int result=iCalendarService.getMinuteBetweenTimeBlock(settingId,beginDate,endDate);
        return JsonResult.Success().setData(result);
    }

    @MethodDefine(title = "日历时间计算测试--时间点", path = "/getEndTimeByCalendar", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "data")})
    @ApiOperation(value = "日历时间计算测试--时间点")
    @PostMapping("/getEndTimeByCalendar")
    @ResponseBody
    public JsonResult getEndTimeByCalendar(@RequestBody JSONObject data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String settingId =data.getString("settingId");
        String startTimeStr = data.getString("beginDate");
        Date startTime =null;
        try {
            startTime  = simpleDateFormat.parse(startTimeStr);
        }catch (Exception e){
            logger.error("---BpmCalCalendarController.getEndTimeByCalendar is error--- :"+e.getMessage());
        }
        int minute=data.getInteger("minuteNum");

        Date result=iCalendarService.getEndTimeByCalendar(settingId,startTime, minute);
        return JsonResult.Success().setData(result);
    }


    @MethodDefine(title = "根据connectId删除日期设置", path = "/delByConnectId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "日历ID", varName = "connectId")})
    @ApiOperation("根据connectId删除日期设置")
    @AuditLog(operation = "根据connectId删除日期设置")
    @GetMapping("/delByConnectId")
    public JsonResult delByConnectId(@ApiParam @RequestParam(value = "connectId") String connectId){
        bpmCalCalendarService.delByConnectId(connectId);
        return JsonResult.Success();
    }

    @MethodDefine(title = "根据日历ID查询所有班次设置列表", path = "/getWorkCalendarBySettingId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "日历ID", varName = "settingId")})
    @ApiOperation("根据日历ID查询所有班次设置列表")
    @GetMapping("/getWorkCalendarBySettingId")
    public JSONArray getWorkCalendarBySettingId(@ApiParam @RequestParam(value = "settingId") String settingId){
        JSONArray array = new JSONArray();
        List<BpmCalCalendar> bpmCalCalendars = bpmCalCalendarService.getWorkCalendarBySettingId(settingId);
        for (BpmCalCalendar calCalendar:bpmCalCalendars) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",calCalendar.getConnectId());
            jsonObject.put("title",calCalendar.getInfo());
            jsonObject.put("start",calCalendar.getStartDay().toString());
            jsonObject.put("end",calCalendar.getEndDay().toString());
            jsonObject.put("allDay",true);
            array.add(jsonObject);
        }
        return array;
    }

    @MethodDefine(title = "逐日创建工作时间", path = "/drawCalendar", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "data")})
    @ApiOperation(value = "逐日创建工作时间")
    @AuditLog(operation = "逐日创建工作时间")
    @PostMapping("/drawCalendar")
    @ResponseBody
    public JSONObject drawCalendar(@RequestBody JSONObject data) {
        String start=data.getString("start");//fullcalendar传来的开始时间错
        String end=data.getString("end");//结束时间戳
        String timeBlockValue=data.getString("timeBlockValue");//日历使用的时间区间设定
        String calSettingId=data.getString("calSettingId");//日历设定
        return returnObject(start,end,timeBlockValue,calSettingId, "", "", "");
    }

    public JSONObject returnObject(String start, String end, String timeBlockValue,
                                   String calSettingId, String connectId, String timeIntervals, String name){
        JSONObject jsonObjectReuslt=new JSONObject();//返回的jsonObject
        boolean removeOrNot=true;
        Long longStart=Long.parseLong(start);//开始时间戳Long
        Long longEnd=Long.parseLong(end);//结束时间戳Long
        BpmCalTimeBlock bpmCalTimeBlock;
        if (BeanUtil.isEmpty(timeIntervals) && BeanUtil.isEmpty(name)) {
            bpmCalTimeBlock = bpmCalTimeBlockService.get(timeBlockValue);
            timeIntervals = bpmCalTimeBlock.getTimeIntervals();//时间区间json
            name = bpmCalTimeBlock.getSettingName();
        }
        BpmCalSetting bpmCalSetting =new BpmCalSetting();
        String whereCross="";
        if(StringUtils.isNotBlank(calSettingId)){
            bpmCalSetting=bpmCalSettingService.get(calSettingId);
        }else{
            bpmCalSetting=bpmCalSettingService.getByName(name);
        }
        JSONObject jsonObject=returnObject(timeIntervals);
        Set<String> jsonArray=jsonObject.keySet();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Map<String, Long> stamp=new HashMap<String, Long>();
        for (String key : jsonArray) {//获取第一天的所有该设置的时间戳

            String value=(String) jsonObject.get(key);
            Date date = null;
            try {
                date = simpleDateFormat.parse(value);
            }catch (Exception e){

            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            String[] time=value.split(":");
            Long millisecond=calculateTimeStamp(Long.valueOf(hour + ""), Long.valueOf(min + ""));//毫秒数
            stamp.put(key, millisecond+longStart);
        }
        for (Map.Entry<String, Long> entry : stamp.entrySet()) {//计算出哪个位置跨天
            String key=entry.getKey();
            String num=key.split("_")[1];
            long value=stamp.get(key);
            if(key.indexOf("endTime_"+num) != -1){//与前一个做对比
                if(value<stamp.get("startTime_"+num)){
                    whereCross+="endTime_"+num;
                }
            }else if(key.contains("plusStart")){
                String whereN=key.substring(9);
                String where="plusStart"+whereN;//这个键
                String whereprefix="";//下一个键
                long whereNum=Long.parseLong(whereN);
                if(whereNum>0){
                    whereprefix="plusEnd"+(whereNum-1);
                    if(stamp.get(whereprefix)>value){//如果小于的话则记录跨天
                        whereCross+=where;
                    }
                }else{
                    if(value<stamp.get("endTime")){
                        whereCross+="plusStart0";
                    }
                }
            }else if(key.contains("plusEnd")){
                String whereN=key.substring(7);
                String where="plusEnd"+whereN;//这个键
                String whereprefix="";//下一个键
                long whereNum=Long.parseLong(whereN);
                if(whereNum>0){
                    whereprefix="plusStart"+whereN;
                    if(stamp.get(whereprefix)>value){//如果小于的话则记录跨天
                        whereCross+=where;
                    }
                }else {
                    whereprefix="plusStart0";
                    if(stamp.get(whereprefix)>value){//如果小于的话则记录跨天
                        whereCross+=where;
                    }
                }
            }
        }

        boolean flagAddDay = false;
        for (Map.Entry<String, Long> entry : stamp.entrySet()) {//调整map的时间
            String key=entry.getKey();
            String num=key.split("_")[1];
            if(whereCross.contains("endTime_"+num)){//假如包含原始数据,即第一条费plus的数据
                if(!("startTime_"+num).equals(key)){
                    flagAddDay = true;
                    stamp.put(key, stamp.get(key)+86400000);//每天的时间戳往后推一天
                }
            }else if(whereCross.contains("plusStart")){
                if(key.contains("plus")){
                    long whereNum=Long.parseLong(key.substring(key.length()-1));
                    if(Long.parseLong(key.substring(key.length()-1))>=whereNum){
                        flagAddDay = true;
                        stamp.put(key, stamp.get(key)+86400000);//每天的时间戳往后推一天
                    }
                }
            }else if(whereCross.contains("plusEnd")){
                if(key.contains("plus")){
                    long whereNum=Long.parseLong(key.substring(key.length()-1));
                    if(Long.parseLong(key.substring(key.length()-1))>whereNum){
                        flagAddDay = true;
                        stamp.put(key, stamp.get(key)+86400000);//每天的时间戳往后推一天
                    }else if(whereCross.equals(key)){
                        flagAddDay = true;
                        stamp.put(key, stamp.get(key)+86400000);
                    }
                }
            }
        }

        Long result=longEnd-longStart;//所选中的区间的总毫秒数量
        Long dayNum=result/86400000;//选中多少天
        int size = jsonArray.size() / 2;
		/*Long beforeSaveStartTime=stamp.get("startTime");
		Long beforeSaveEndTime=stamp.get("endTime");*/
        List<BpmCalCalendar> saveWorkCalendars = new ArrayList<>();
        if (BeanUtil.isEmpty(connectId)) {
            connectId = IdGenerator.getIdStr();
        }
        for (long i = 0; i < dayNum; i++) {
            //start+i*86400000
            int stampSize=stamp.size();
            for (int k = 0; k < size; k++) {
                BpmCalCalendar bpmCalCalendar=new BpmCalCalendar();
                bpmCalCalendar.setCalenderId(IdGenerator.getIdStr());
                bpmCalCalendar.setStartTime(new Timestamp(stamp.get("startTime_" + k)+i*86400000));
                bpmCalCalendar.setEndTime(new Timestamp(stamp.get("endTime_" + k)+i*86400000));
                bpmCalCalendar.setStartDay(new Timestamp(longStart));
                if (flagAddDay) {
                    longEnd += 86400000;
                    flagAddDay = false;
                }
                bpmCalCalendar.setEndDay(new Timestamp(longEnd));
                bpmCalCalendar.setConnectId(connectId);
                bpmCalCalendar.setSettingId(bpmCalSetting.getSettingId());
                bpmCalCalendar.setInfo(name);
                bpmCalCalendar.setTimeIntervals(timeIntervals);
                List<BpmCalCalendar> bpmCalCalendars=
                        bpmCalCalendarService.getCalendarInDayBySettingIdConnectId(bpmCalCalendar.getStartTime(),
                                bpmCalSetting.getSettingId(), connectId);
                for (BpmCalCalendar bpmCalCalendar2 : bpmCalCalendars) {//如果有重合的地方则不允许移动
                    if ((bpmCalCalendar2.getStartTime().getTime() <= bpmCalCalendar.getStartTime().getTime() &&
                            bpmCalCalendar2.getEndTime().getTime() >= bpmCalCalendar.getStartTime().getTime())||
                            (bpmCalCalendar2.getStartTime().getTime() <= bpmCalCalendar.getEndTime().getTime() &&
                                    bpmCalCalendar2.getEndTime().getTime() >= bpmCalCalendar.getEndTime().getTime()))
                    {
                        removeOrNot=false;
                    }
                }
                if(removeOrNot){
                    jsonObjectReuslt.put("success", true);
                    jsonObjectReuslt.put("id", connectId);
                    jsonObjectReuslt.put("text", name);
                    jsonObjectReuslt.put("end", new Timestamp(longEnd).toString());
                }else{
                    jsonObjectReuslt.put("success", false);
                }

                if(removeOrNot){
                    saveWorkCalendars.add(bpmCalCalendar);
                }

                for (long j = 0; j < stampSize/2-1; j++) {
                    String plusStartName="plusStart"+j;
                    String plusEndName="plusEnd"+j;
                    BpmCalCalendar plusWorkCalendar=new BpmCalCalendar();
                    plusWorkCalendar.setCalenderId(IdGenerator.getIdStr());
                    if (BeanUtil.isNotEmpty(stamp.get(plusStartName))) {
                        plusWorkCalendar.setStartTime(new Timestamp(stamp.get(plusStartName)+i*86400000));
                        plusWorkCalendar.setEndTime(new Timestamp(stamp.get(plusEndName)+i*86400000));
                        plusWorkCalendar.setSettingId(bpmCalSetting.getSettingId());
                        bpmCalCalendarService.insert(plusWorkCalendar);
                    }
                }
            }
        }
        if(removeOrNot){
            for (BpmCalCalendar saveWorkCalendar : saveWorkCalendars) {
                bpmCalCalendarService.insert(saveWorkCalendar);
            }
            //calSettingManager.saveOrUpdate(bpmCalSetting);
        }
        return jsonObjectReuslt;
    }

    /**
     * 计算8点40分有多少毫秒
     * @param hour
     * @param minute
     * @return
     */
    public Long calculateTimeStamp(Long hour,Long minute){
        if(hour!=null&&minute!=null){
            Long result=(hour*60+minute)*60000;
            return result;
        }else{
            return null;
        }

    }

    public JSONObject returnObject(String timeIntervals) {
        JSONArray jsonArray = JSONArray.parseArray(timeIntervals);
        JSONObject returnObject = new JSONObject();
        Map<Integer , Integer> map = new HashMap<>();
        int i = 0;
        for (Object o : jsonArray) {
            JSONObject jsonObject1 = (JSONObject) o;
            String startTime = "1970-01-01T"+(String) jsonObject1.get("startTime")+":00";
            String endTime = "1970-01-01T"+(String) jsonObject1.get("endTime")+":00";
            returnObject.put("startTime_" + i, startTime);
            returnObject.put("endTime_" + i, endTime);
            i++;
        }
        return returnObject;
    }
}
