package com.scfsoft.system.dao.mybatis;
import com.scfsoft.system.dto.MultiSystemNotice;
import com.scfsoft.system.dto.SystemNotice;
import com.scfsoft.system.dto.SystemNoticeSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author guojingyu
 */
@Mapper
public interface SystemNoticeMapper {

    /**
     * 查询系统公告
     * @param param
     * @return
     */
//    List<SystemNotice> findSystemNoticeByPage(SystemNoticeSearchParam param);

//    /**
//     * 查询系统公告
//     * @param noticeId
//     * @return
//     */
//    SystemNotice findSystemNoticeById(@Param("noticeId") String noticeId);


    /**
     * 查询系统公告
     * @param param
     * @return
     */
    List<SystemNotice> findSystemNoticeByPage(SystemNoticeSearchParam param);

    /**
     * 查询系统公告
     * @param noticeId
     * @return
     */
    MultiSystemNotice findMultiSystemNoticeById(@Param("noticeId") String noticeId);

    /**
     * 查询用户系统公告
     * @param userId
     * @return
     */
    List<SystemNotice> findSubscriberSystemNotice(@Param("userId") String userId);

}
