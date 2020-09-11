package com.h3bpm.web.controller;

import OThinker.Common.Organization.Models.User;
import OThinker.H3.Controller.ControllerBase;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.Announcement;
import com.h3bpm.web.service.AnnouncementService;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.*;
import com.h3bpm.web.vo.query.QueryAnnouncementList;
import com.h3bpm.web.vo.query.QueryKnowledgeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/Portal/announcement")
public class AnnouncementController extends ControllerBase {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

    @Autowired
    private AnnouncementService announcementService;

    @Override
    public String getFunctionCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @RequestMapping(value = "/findShowAll", method = RequestMethod.GET, produces = "application/json;charset=utf8")
    @ResponseBody
    public ResponseVo findShowAll() throws IOException {

        List<RespFindShowAllVo> list = new ArrayList<>();

        List<Announcement> announcementList = announcementService.findAnnouncementByTime(new Date());
        if (announcementList != null){
            for (Announcement announcement : announcementList) {
                list.add(new RespFindShowAllVo(announcement));
            }
        }

        return new ResponseVo(list);
    }

    /*@RequestMapping(value = "/listAnnouncementByPage", produces = "application/json;charset=utf8")
    @ResponseBody
    public RespPageVo listAnnouncementByPage(@ModelAttribute ReqListAnnouncementPageVo requestBean) {
        Map<String, Object> userMap = null;
        try {
            userMap = this._getCurrentUser();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        User user = (User) userMap.get("User");

        QueryAnnouncementList queryAnnouncementList = new QueryAnnouncementList(requestBean);
        queryAnnouncementList.setQueryUserId(user.getObjectId());

        UserSessionInfo userSessionInfo = UserSessionUtils.get(Constants.SESSION_USER, UserSessionInfo.class);
        List<String> parentIds = userSessionInfo.getParentIds();
        if (parentIds != null) {
            queryAnnouncementList.setUserAllParentIds(parentIds);
        }

        PageInfo<RespFindShowAllVo> pageInfo = announcementService.findKnowledgeByPage(queryAnnouncementList);

        return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
    }*/


}