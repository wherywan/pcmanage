--------------------------------
-- Create table: 'SYS_CODE'
--------------------------------
CREATE TABLE SYS_CODE (
   ID varchar2(36) NOT NULL
  ,CODE varchar2(40) NOT NULL
  ,CODE_TYPE varchar2(32) NOT NULL
  ,CODE_VALUE varchar2(100) NOT NULL
  ,ORDER_BY number DEFAULT 0 NOT NULL
  ,PAR_CODE_ID varchar2(36) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_CODE ADD CONSTRAINT PK_SYSCODE PRIMARY KEY (ID);
ALTER TABLE SYS_CODE ADD CONSTRAINT IK_SYSCODE_CODE_CODETYPE UNIQUE (CODE,CODE_TYPE);
CREATE INDEX IK_SYSCODE_CODETYPE ON SYS_CODE (CODE_TYPE);
COMMENT ON TABLE SYS_CODE IS '系统管理 - 标准代码表';
COMMENT ON COLUMN SYS_CODE.ID IS '代码ID (Label: 主键)';
COMMENT ON COLUMN SYS_CODE.CODE IS '代码';
COMMENT ON COLUMN SYS_CODE.CODE_TYPE IS '代码类别';
COMMENT ON COLUMN SYS_CODE.CODE_VALUE IS '代码值';
COMMENT ON COLUMN SYS_CODE.ORDER_BY IS '展现顺序号';
COMMENT ON COLUMN SYS_CODE.PAR_CODE_ID IS '父代码ID';
COMMENT ON COLUMN SYS_CODE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_CODE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_CODE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_CODE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_CODE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_CODE_TYPE'
--------------------------------
CREATE TABLE SYS_CODE_TYPE (
   ID varchar2(36) NOT NULL
  ,CODE_TYPE varchar2(32) NOT NULL
  ,CODE_TYPE_NAME varchar2(100) NOT NULL
  ,PAR_CODE_TYPE_ID varchar2(36) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_CODE_TYPE ADD CONSTRAINT PK_SYSCODETYPE PRIMARY KEY (ID);
ALTER TABLE SYS_CODE_TYPE ADD CONSTRAINT IK_SYSCODETYPE_CODETYPE UNIQUE (CODE_TYPE);
COMMENT ON TABLE SYS_CODE_TYPE IS '系统管理 - 标准代码类别表';
COMMENT ON COLUMN SYS_CODE_TYPE.ID IS '代码类别ID (Label: 主键)';
COMMENT ON COLUMN SYS_CODE_TYPE.CODE_TYPE IS '代码类别';
COMMENT ON COLUMN SYS_CODE_TYPE.CODE_TYPE_NAME IS '代码类别名称';
COMMENT ON COLUMN SYS_CODE_TYPE.PAR_CODE_TYPE_ID IS '父代码类别ID';
COMMENT ON COLUMN SYS_CODE_TYPE.CREATED_TS IS '创建时间';
COMMENT ON COLUMN SYS_CODE_TYPE.CREATED_BY IS '创建人用户ID';
COMMENT ON COLUMN SYS_CODE_TYPE.LAST_UPD_TS IS '修改时间';
COMMENT ON COLUMN SYS_CODE_TYPE.LAST_UPD_BY IS '修改人用户ID';
COMMENT ON COLUMN SYS_CODE_TYPE.UPDATE_COUNT IS '修改次数';

--------------------------------
-- Create table: 'SYS_HOLIDAY'
--------------------------------
CREATE TABLE SYS_HOLIDAY (
   DATE_ID varchar2(36) NOT NULL
  ,HOLIDAY_FLAG char(1) NOT NULL
  ,REMARK varchar2(256) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_HOLIDAY ADD CONSTRAINT PK_SYSHOLIDAY PRIMARY KEY (DATE_ID);
COMMENT ON TABLE SYS_HOLIDAY IS '系统管理 - 消息表';
COMMENT ON COLUMN SYS_HOLIDAY.DATE_ID IS '主键ID: yyyyMMdd 格式 (Label: 主键)';
COMMENT ON COLUMN SYS_HOLIDAY.HOLIDAY_FLAG IS '日期类型: 0 - 普通日期 1 - 节假日 2 - 节假日调休 (Label: 类型)';
COMMENT ON COLUMN SYS_HOLIDAY.REMARK IS '节假日说明 (Label: 说明)';
COMMENT ON COLUMN SYS_HOLIDAY.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_HOLIDAY.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_HOLIDAY.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_HOLIDAY.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_HOLIDAY.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_MESSAGE'
--------------------------------
CREATE TABLE SYS_MESSAGE (
   ID varchar2(36) NOT NULL
  ,SEND_TYPE varchar2(32) NOT NULL
  ,TITLE varchar2(128) NOT NULL
  ,CONTENT varchar2(500) NOT NULL
  ,VALID_DATE timestamp(6) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_MESSAGE ADD CONSTRAINT PK_SYSMESSAGE PRIMARY KEY (ID);
COMMENT ON TABLE SYS_MESSAGE IS '系统管理 - 用户消息表';
COMMENT ON COLUMN SYS_MESSAGE.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_MESSAGE.SEND_TYPE IS 'all-所有人发送 org-按部门发送 role-按角色发送 (Label: 消息类型)';
COMMENT ON COLUMN SYS_MESSAGE.TITLE IS '消息标题 (Label: 消息标题)';
COMMENT ON COLUMN SYS_MESSAGE.CONTENT IS '消息内容 (Label: 消息内容)';
COMMENT ON COLUMN SYS_MESSAGE.VALID_DATE IS '有效期内，符合条件的新用户也发送 (Label: 有效期)';
COMMENT ON COLUMN SYS_MESSAGE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_MESSAGE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_MESSAGE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_MESSAGE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_MESSAGE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_MESSAGE_PUBLISH'
--------------------------------
CREATE TABLE SYS_MESSAGE_PUBLISH (
   ID varchar2(36) NOT NULL
  ,MESSAGE_ID varchar2(36) NOT NULL
  ,SEND_TYPE varchar2(32) NOT NULL
  ,TARGET_ID varchar2(128) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_MESSAGE_PUBLISH ADD CONSTRAINT PK_SYSMESSAGEPUBLISH PRIMARY KEY (ID);
CREATE INDEX IK_SYSMESSAGEPUBLISH_MESSA0000 ON SYS_MESSAGE_PUBLISH (MESSAGE_ID,SEND_TYPE,TARGET_ID);
COMMENT ON TABLE SYS_MESSAGE_PUBLISH IS '系统管理 - 用户消息发布表';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.MESSAGE_ID IS '消息ID (Label: 消息ID)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.SEND_TYPE IS 'all-所有人发送 org-按部门发送 role-按角色发送 (Label: 消息类型)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.TARGET_ID IS 'org-填写机构号 role-填写角色id (Label: 接收组)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_MESSAGE_PUBLISH.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_NOTICE'
--------------------------------
CREATE TABLE SYS_NOTICE (
   ID varchar2(36) NOT NULL
  ,NOTICE_TYPE varchar2(32) NULL
  ,TITLE varchar2(128) NOT NULL
  ,CONTENT clob NOT NULL
  ,PUBLISH_TIME timestamp(6) NOT NULL
  ,IS_VISIBLE varchar2(1) DEFAULT 'Y' NOT NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_NOTICE ADD CONSTRAINT PK_SYSNOTICE PRIMARY KEY (ID);
COMMENT ON TABLE SYS_NOTICE IS '系统管理 - 系统公告表';
COMMENT ON COLUMN SYS_NOTICE.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_NOTICE.NOTICE_TYPE IS '公告类型 (Label: 公告类型)';
COMMENT ON COLUMN SYS_NOTICE.TITLE IS '标题 (Label: 标题)';
COMMENT ON COLUMN SYS_NOTICE.CONTENT IS '内容 (Label: 内容)';
COMMENT ON COLUMN SYS_NOTICE.PUBLISH_TIME IS '发布时间 (Label: 发布时间)';
COMMENT ON COLUMN SYS_NOTICE.IS_VISIBLE IS 'Y-可见 N-不可见 (Label: 是否可见)';
COMMENT ON COLUMN SYS_NOTICE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_NOTICE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_NOTICE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_NOTICE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_NOTICE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_NOTICE_PUBLISH'
--------------------------------
CREATE TABLE SYS_NOTICE_PUBLISH (
   ID varchar2(36) NOT NULL
  ,NOTICE_ID varchar2(36) NOT NULL
  ,SCOPE varchar2(32) DEFAULT 'all' NOT NULL
  ,TARGET_ID varchar2(36) NOT NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_NOTICE_PUBLISH ADD CONSTRAINT PK_SYSNOTICEPUBLISH PRIMARY KEY (ID);
ALTER TABLE SYS_NOTICE_PUBLISH ADD CONSTRAINT IK_SYSNOTICEPUBLISH_NOTICE0000 UNIQUE (NOTICE_ID,SCOPE,TARGET_ID);
COMMENT ON TABLE SYS_NOTICE_PUBLISH IS '系统管理 - 系统公告发布表';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.NOTICE_ID IS '公告ID (Label: 公告ID)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.SCOPE IS 'all-全部可见 org-指定机构可见 role-指定角色可见  user-指定用户id (Label: 公告范围)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.TARGET_ID IS 'all-all org-可见机构id role-可见角色id user-对应用户id (Label: 内容)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_NOTICE_PUBLISH.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_OPER_LOG'
--------------------------------
CREATE TABLE SYS_OPER_LOG (
   ID varchar2(36) NOT NULL
  ,CLIENT_IP varchar2(32) NULL
  ,USER_ID varchar2(36) NULL
  ,USER_NAME varchar2(40) NULL
  ,OPER_BGN_TS timestamp(6) NULL
  ,OPER_END_TS timestamp(6) NULL
  ,METHOD varchar2(32) NULL
  ,URL varchar2(300) NULL
  ,REQ_INFO clob NULL
  ,RESP_INFO clob NULL
  ,REQ_REST varchar2(32) NULL
  ,MODEL_NAME varchar2(200) NULL
  ,MODEL_DESC varchar2(500) NULL
  ,FUNC_NAME varchar2(200) NULL
  ,FUNC_DESC clob NULL
  ,REMARK varchar2(1000) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_OPER_LOG ADD CONSTRAINT PK_SYSOPERLOG PRIMARY KEY (ID);
CREATE INDEX IK_SYSOPERLOG_USERNAME ON SYS_OPER_LOG (USER_NAME);
COMMENT ON TABLE SYS_OPER_LOG IS '系统管理 - 操作日志表';
COMMENT ON COLUMN SYS_OPER_LOG.ID IS '日志ID-UUID (Label: 主键)';
COMMENT ON COLUMN SYS_OPER_LOG.CLIENT_IP IS '客户端IP (Label: 客户端IP)';
COMMENT ON COLUMN SYS_OPER_LOG.USER_ID IS '用户ID (Label: 用户ID)';
COMMENT ON COLUMN SYS_OPER_LOG.USER_NAME IS '用户名 (Label: 用户名)';
COMMENT ON COLUMN SYS_OPER_LOG.OPER_BGN_TS IS '操作开始时间 (Label: 操作开始时间)';
COMMENT ON COLUMN SYS_OPER_LOG.OPER_END_TS IS '操作结束时间 (Label: 操作结束时间)';
COMMENT ON COLUMN SYS_OPER_LOG.METHOD IS '请求类型: GET/POST/DELETE (Label: 请求类型)';
COMMENT ON COLUMN SYS_OPER_LOG.URL IS 'URL (Label: URL)';
COMMENT ON COLUMN SYS_OPER_LOG.REQ_INFO IS '请求信息 (Label: 请求信息)';
COMMENT ON COLUMN SYS_OPER_LOG.RESP_INFO IS '响应信息 (Label: 响应信息)';
COMMENT ON COLUMN SYS_OPER_LOG.REQ_REST IS '请求结果: 01-请求成功 02-请求失败 (Label: 请求结果)';
COMMENT ON COLUMN SYS_OPER_LOG.MODEL_NAME IS '模块名 (Label: 模块名)';
COMMENT ON COLUMN SYS_OPER_LOG.MODEL_DESC IS '模块描述 (Label: 模块描述)';
COMMENT ON COLUMN SYS_OPER_LOG.FUNC_NAME IS '功能名 (Label: 功能名)';
COMMENT ON COLUMN SYS_OPER_LOG.FUNC_DESC IS '功能描述 (Label: 功能描述)';
COMMENT ON COLUMN SYS_OPER_LOG.REMARK IS '备注 (Label: 备注)';
COMMENT ON COLUMN SYS_OPER_LOG.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_OPER_LOG.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_OPER_LOG.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_OPER_LOG.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_OPER_LOG.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_ORG'
--------------------------------
CREATE TABLE SYS_ORG (
   ID varchar2(36) NOT NULL
  ,ORG_CODE varchar2(36) NOT NULL
  ,ORG_NAME varchar2(100) NOT NULL
  ,ORG_NAME_SHORT varchar2(80) NULL
  ,ORG_TYPE varchar2(32) DEFAULT '01' NOT NULL
  ,PAR_ORG_ID varchar2(36) NULL
  ,B_ORG_ID varchar2(36) NOT NULL
  ,ORDER_BY number DEFAULT 0 NOT NULL
  ,ORG_STATUS varchar2(32) DEFAULT '02' NOT NULL
  ,UPD_STATUS varchar2(32) DEFAULT '01' NOT NULL
  ,ORG_CODE_HR varchar2(36) NULL
  ,ORG_CODE_CB varchar2(36) NULL
  ,MNG_POSTN_ID varchar2(36) NULL
  ,REMARK varchar2(256) NULL
  ,LAWYER varchar2(64) NULL
  ,REG_ADDR varchar2(256) NULL
  ,OFFICE_ADDR varchar2(256) NULL
  ,OFFICE_ZIP varchar2(12) NULL
  ,OFFICE_TEL varchar2(40) NULL
  ,OFFICE_FAX varchar2(20) NULL
  ,EMAIL varchar2(50) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_ORG ADD CONSTRAINT PK_SYSORG PRIMARY KEY (ID);
ALTER TABLE SYS_ORG ADD CONSTRAINT IK_SYSORG_ORGCODE UNIQUE (ORG_CODE);
COMMENT ON TABLE SYS_ORG IS '系统管理 - 机构';
COMMENT ON COLUMN SYS_ORG.ID IS '机构/部门ID (Label: 主键)';
COMMENT ON COLUMN SYS_ORG.ORG_CODE IS '机构编码';
COMMENT ON COLUMN SYS_ORG.ORG_NAME IS '中文名称';
COMMENT ON COLUMN SYS_ORG.ORG_NAME_SHORT IS '机构简称';
COMMENT ON COLUMN SYS_ORG.ORG_TYPE IS '01分行、02支行、03网点、04部门';
COMMENT ON COLUMN SYS_ORG.PAR_ORG_ID IS '父级节点';
COMMENT ON COLUMN SYS_ORG.B_ORG_ID IS '对于部门而言，部门归属机构ID，对于机构而言，是自身';
COMMENT ON COLUMN SYS_ORG.ORDER_BY IS '同父机构下显示时排序使用';
COMMENT ON COLUMN SYS_ORG.ORG_STATUS IS '01-筹建 02-营业 03-终止 04-试营业 05-已失效';
COMMENT ON COLUMN SYS_ORG.UPD_STATUS IS '01.HR数据 02.本地新增 03.本地编辑';
COMMENT ON COLUMN SYS_ORG.ORG_CODE_HR IS 'HR系统机构号';
COMMENT ON COLUMN SYS_ORG.ORG_CODE_CB IS '核心系统机构号';
COMMENT ON COLUMN SYS_ORG.MNG_POSTN_ID IS '机构或部门领导：本机构或本部门内员工的默认汇报对象
指向SYS_POSTN.POSTN_ID';
COMMENT ON COLUMN SYS_ORG.REMARK IS '备注';
COMMENT ON COLUMN SYS_ORG.LAWYER IS '法人代表';
COMMENT ON COLUMN SYS_ORG.REG_ADDR IS '注册地址';
COMMENT ON COLUMN SYS_ORG.OFFICE_ADDR IS '办公地址';
COMMENT ON COLUMN SYS_ORG.OFFICE_ZIP IS '邮编';
COMMENT ON COLUMN SYS_ORG.OFFICE_TEL IS '电话';
COMMENT ON COLUMN SYS_ORG.OFFICE_FAX IS '传真';
COMMENT ON COLUMN SYS_ORG.EMAIL IS '电子邮件';
COMMENT ON COLUMN SYS_ORG.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_ORG.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_ORG.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_ORG.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_ORG.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_PARAM'
--------------------------------
CREATE TABLE SYS_PARAM (
   ID varchar2(36) NOT NULL
  ,PARAM_KEY varchar2(32) NOT NULL
  ,PARAM_NAME varchar2(64) NULL
  ,PARAM_VALUE varchar2(256) NULL
  ,REMARK varchar2(256) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_PARAM ADD CONSTRAINT PK_SYSPARAM PRIMARY KEY (ID);
ALTER TABLE SYS_PARAM ADD CONSTRAINT IK_SYSPARAM_PARAMNAME UNIQUE (PARAM_NAME);
COMMENT ON TABLE SYS_PARAM IS '系统管理 - 系统参数表';
COMMENT ON COLUMN SYS_PARAM.ID IS 'ID (Label: ID)';
COMMENT ON COLUMN SYS_PARAM.PARAM_KEY IS '参数KEY (Label: 参数名称)';
COMMENT ON COLUMN SYS_PARAM.PARAM_NAME IS '参数名称 (Label: 参数名称)';
COMMENT ON COLUMN SYS_PARAM.PARAM_VALUE IS '参数值 (Label: 参数值)';
COMMENT ON COLUMN SYS_PARAM.REMARK IS '参数说明 (Label: 参数说明)';
COMMENT ON COLUMN SYS_PARAM.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_PARAM.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_PARAM.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_PARAM.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_PARAM.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_RESOURCE'
--------------------------------
CREATE TABLE SYS_RESOURCE (
   ID varchar2(36) NOT NULL
  ,RESOURCE_NAME varchar2(100) NOT NULL
  ,SHORT_NAME varchar2(64) NULL
  ,ENG_NAME varchar2(100) NULL
  ,ORDER_BY number DEFAULT 0 NOT NULL
  ,RESOURCE_URL varchar2(256) NULL
  ,RESOURCE_TYPE varchar2(32) DEFAULT 'MENU' NOT NULL
  ,SHORT_ICON varchar2(32) NULL
  ,PAR_RESOURCE_ID varchar2(36) NULL
  ,REMARK varchar2(256) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_RESOURCE ADD CONSTRAINT PK_SYSRESOURCE PRIMARY KEY (ID);
COMMENT ON TABLE SYS_RESOURCE IS '系统管理 - 系统资源';
COMMENT ON COLUMN SYS_RESOURCE.ID IS '菜单ID (Label: 主键)';
COMMENT ON COLUMN SYS_RESOURCE.RESOURCE_NAME IS '菜单名称 (Label: 菜单名称)';
COMMENT ON COLUMN SYS_RESOURCE.SHORT_NAME IS '菜单简称 (Label: 菜单简称)';
COMMENT ON COLUMN SYS_RESOURCE.ENG_NAME IS '英文名称 (Label: 英文名称)';
COMMENT ON COLUMN SYS_RESOURCE.ORDER_BY IS '展现顺序号 (Label: 展现顺序号)';
COMMENT ON COLUMN SYS_RESOURCE.RESOURCE_URL IS '功能链接 (Label: 功能链接)';
COMMENT ON COLUMN SYS_RESOURCE.RESOURCE_TYPE IS '默认为菜单 (Label: 资源类型)';
COMMENT ON COLUMN SYS_RESOURCE.SHORT_ICON IS '小图标 (Label: 小图标)';
COMMENT ON COLUMN SYS_RESOURCE.PAR_RESOURCE_ID IS '父级菜单ID (Label: 父级菜单ID)';
COMMENT ON COLUMN SYS_RESOURCE.REMARK IS '备注 (Label: 备注)';
COMMENT ON COLUMN SYS_RESOURCE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_RESOURCE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_RESOURCE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_RESOURCE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_RESOURCE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_ROLE'
--------------------------------
CREATE TABLE SYS_ROLE (
   ID varchar2(36) NOT NULL
  ,ROLE_NAME varchar2(50) NOT NULL
  ,REMARK varchar2(256) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_ROLE ADD CONSTRAINT PK_SYSROLE PRIMARY KEY (ID);
ALTER TABLE SYS_ROLE ADD CONSTRAINT IK_SYSROLE_ROLENAME UNIQUE (ROLE_NAME);
COMMENT ON TABLE SYS_ROLE IS '系统管理 - 系统角色表';
COMMENT ON COLUMN SYS_ROLE.ID IS '角色ID (Label: 主键)';
COMMENT ON COLUMN SYS_ROLE.ROLE_NAME IS '角色名称';
COMMENT ON COLUMN SYS_ROLE.REMARK IS '备注描述';
COMMENT ON COLUMN SYS_ROLE.CREATED_TS IS '创建时间';
COMMENT ON COLUMN SYS_ROLE.CREATED_BY IS '创建人用户ID';
COMMENT ON COLUMN SYS_ROLE.LAST_UPD_TS IS '修改时间';
COMMENT ON COLUMN SYS_ROLE.LAST_UPD_BY IS '修改人用户ID';
COMMENT ON COLUMN SYS_ROLE.UPDATE_COUNT IS '修改次数';

--------------------------------
-- Create table: 'SYS_ROLE_RESOURCE'
--------------------------------
CREATE TABLE SYS_ROLE_RESOURCE (
   ID varchar2(36) NOT NULL
  ,ROLE_ID varchar2(36) NOT NULL
  ,RESOURCE_ID varchar2(36) NOT NULL
  ,RESOURCE_TYPE varchar2(32) NOT NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_ROLE_RESOURCE ADD CONSTRAINT PK_SYSROLERESOURCE PRIMARY KEY (ID);
ALTER TABLE SYS_ROLE_RESOURCE ADD CONSTRAINT IK_SYSROLERESOURCE_ROLEID_0000 UNIQUE (ROLE_ID,RESOURCE_ID);
COMMENT ON TABLE SYS_ROLE_RESOURCE IS '系统管理 - 系统角色与资源关系表';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.ID IS '无业务含义 (Label: 主键)';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.ROLE_ID IS '角色ID';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.RESOURCE_ID IS '资源ID';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.RESOURCE_TYPE IS '资源类型';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.CREATED_TS IS '创建时间';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.CREATED_BY IS '创建人用户ID';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.LAST_UPD_TS IS '修改时间';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.LAST_UPD_BY IS '修改人用户ID';
COMMENT ON COLUMN SYS_ROLE_RESOURCE.UPDATE_COUNT IS '修改次数';

--------------------------------
-- Create table: 'SYS_SCHEDULE'
--------------------------------
CREATE TABLE SYS_SCHEDULE (
   ID varchar2(36) NOT NULL
  ,RUNNER varchar2(256) NOT NULL
  ,CRON varchar2(32) NULL
  ,STATUS varchar2(32) NOT NULL
  ,REMARK varchar2(256) NULL
  ,LAST_RUN timestamp(6) NULL
  ,VERSION varchar2(36) NOT NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_SCHEDULE ADD CONSTRAINT PK_SYSSCHEDULE PRIMARY KEY (ID);
COMMENT ON TABLE SYS_SCHEDULE IS '系统管理 - 系统计划任务表';
COMMENT ON COLUMN SYS_SCHEDULE.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_SCHEDULE.RUNNER IS '计划任务执行器';
COMMENT ON COLUMN SYS_SCHEDULE.CRON IS '计划任务CRON';
COMMENT ON COLUMN SYS_SCHEDULE.STATUS IS '状态';
COMMENT ON COLUMN SYS_SCHEDULE.REMARK IS '备注';
COMMENT ON COLUMN SYS_SCHEDULE.LAST_RUN IS '计划任务上次执行时间';
COMMENT ON COLUMN SYS_SCHEDULE.VERSION IS '配置版本号';
COMMENT ON COLUMN SYS_SCHEDULE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_SCHEDULE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_SCHEDULE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_SCHEDULE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_SCHEDULE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_SCHEDULE_HISTORY'
--------------------------------
CREATE TABLE SYS_SCHEDULE_HISTORY (
   ID varchar2(36) NOT NULL
  ,RUNNER varchar2(256) NOT NULL
  ,CRON varchar2(32) NULL
  ,START_TIME timestamp(6) NOT NULL
  ,END_TIME timestamp(6) NOT NULL
  ,RESULT varchar2(32) NOT NULL
  ,REMARK varchar2(256) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_SCHEDULE_HISTORY ADD CONSTRAINT PK_SYSSCHEDULEHISTORY PRIMARY KEY (ID);
CREATE INDEX IK_SYSSCHEDULEHISTORY_RUNNER ON SYS_SCHEDULE_HISTORY (RUNNER);
COMMENT ON TABLE SYS_SCHEDULE_HISTORY IS '系统管理 - 系统计划任务历史表';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.RUNNER IS '计划任务执行器';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.CRON IS '计划任务CRON';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.START_TIME IS '开始时间';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.END_TIME IS '结束时间';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.RESULT IS '执行结果';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.REMARK IS '备注';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_SCHEDULE_HISTORY.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_USER'
--------------------------------
CREATE TABLE SYS_USER (
   ID varchar2(36) NOT NULL
  ,USERNAME varchar2(40) NOT NULL
  ,REALNAME varchar2(20) NOT NULL
  ,ORG_ID varchar2(36) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_USER ADD CONSTRAINT PK_SYSUSER PRIMARY KEY (ID);
ALTER TABLE SYS_USER ADD CONSTRAINT IK_SYSUSER_USERNAME UNIQUE (USERNAME);
COMMENT ON TABLE SYS_USER IS '系统管理 - 用户';
COMMENT ON COLUMN SYS_USER.ID IS '用户ID (Label: 主键)';
COMMENT ON COLUMN SYS_USER.USERNAME IS '用户登陆名';
COMMENT ON COLUMN SYS_USER.REALNAME IS '用户姓名（昵称）';
COMMENT ON COLUMN SYS_USER.ORG_ID IS '机构ID';
COMMENT ON COLUMN SYS_USER.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_USER.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_USER.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_USER.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_USER.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_USER_MESSAGE'
--------------------------------
CREATE TABLE SYS_USER_MESSAGE (
   ID varchar2(36) NOT NULL
  ,MESSAGE_ID varchar2(36) NULL
  ,SENDER_ID varchar2(36) NOT NULL
  ,RECEIVER varchar2(36) NOT NULL
  ,MESSAGE_TYPE varchar2(32) NOT NULL
  ,TITLE varchar2(128) NULL
  ,CONTENT varchar2(500) NULL
  ,IS_READ char(1) DEFAULT 'N' NOT NULL
  ,REPLY_ID varchar2(36) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_USER_MESSAGE ADD CONSTRAINT PK_SYSUSERMESSAGE PRIMARY KEY (ID);
CREATE INDEX IK_SYSUSERMESSAGE_RECEIVER0000 ON SYS_USER_MESSAGE (RECEIVER,MESSAGE_TYPE,IS_READ);
COMMENT ON TABLE SYS_USER_MESSAGE IS '系统管理 - 用户消息表';
COMMENT ON COLUMN SYS_USER_MESSAGE.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_USER_MESSAGE.MESSAGE_ID IS '系统短消息ID，如用户消息，此字段不填 (Label: 系统短消息)';
COMMENT ON COLUMN SYS_USER_MESSAGE.SENDER_ID IS '发送人用户ID，系统消息填写SYSTEM (Label: 发送人)';
COMMENT ON COLUMN SYS_USER_MESSAGE.RECEIVER IS '接收人用户ID (Label: 接收人)';
COMMENT ON COLUMN SYS_USER_MESSAGE.MESSAGE_TYPE IS 'system-系统消息 user-用户消息 reply-回复消息 (Label: 消息类型)';
COMMENT ON COLUMN SYS_USER_MESSAGE.TITLE IS '系统消息可为空 (Label: 消息标题)';
COMMENT ON COLUMN SYS_USER_MESSAGE.CONTENT IS '系统消息可为空 (Label: 消息内容)';
COMMENT ON COLUMN SYS_USER_MESSAGE.IS_READ IS 'Y/N (Label: 是否已读)';
COMMENT ON COLUMN SYS_USER_MESSAGE.REPLY_ID IS '回复消息Id (Label: 回复消息)';
COMMENT ON COLUMN SYS_USER_MESSAGE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_USER_MESSAGE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_USER_MESSAGE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_USER_MESSAGE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_USER_MESSAGE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_USER_PARAM'
--------------------------------
CREATE TABLE SYS_USER_PARAM (
   ID varchar2(36) NOT NULL
  ,USER_ID varchar2(36) NOT NULL
  ,PARAM_KEY varchar2(32) NOT NULL
  ,PARAM_NAME varchar2(64) NULL
  ,PARAM_VALUE varchar2(256) NULL
  ,REMARK varchar2(256) NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_USER_PARAM ADD CONSTRAINT PK_SYSUSERPARAM PRIMARY KEY (ID);
ALTER TABLE SYS_USER_PARAM ADD CONSTRAINT IK_SYSUSERPARAM_USERID_PAR0000 UNIQUE (USER_ID,PARAM_KEY);
COMMENT ON TABLE SYS_USER_PARAM IS '系统管理 - 用户参数表';
COMMENT ON COLUMN SYS_USER_PARAM.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_USER_PARAM.USER_ID IS '用户ID';
COMMENT ON COLUMN SYS_USER_PARAM.PARAM_KEY IS '参数KEY (Label: 参数编码)';
COMMENT ON COLUMN SYS_USER_PARAM.PARAM_NAME IS '参数名称 (Label: 参数名称)';
COMMENT ON COLUMN SYS_USER_PARAM.PARAM_VALUE IS '参数值 (Label: 参数值)';
COMMENT ON COLUMN SYS_USER_PARAM.REMARK IS '备注说明 (Label: 参数说明)';
COMMENT ON COLUMN SYS_USER_PARAM.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_USER_PARAM.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_USER_PARAM.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_USER_PARAM.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_USER_PARAM.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

--------------------------------
-- Create table: 'SYS_USER_ROLE'
--------------------------------
CREATE TABLE SYS_USER_ROLE (
   ID varchar2(36) NOT NULL
  ,USER_ID varchar2(36) NOT NULL
  ,ROLE_ID varchar2(36) NOT NULL
  ,CREATED_TS timestamp(6) NULL
  ,CREATED_BY varchar2(36) NULL
  ,LAST_UPD_TS timestamp(6) NULL
  ,LAST_UPD_BY varchar2(36) NULL
  ,UPDATE_COUNT number DEFAULT 0 NULL
);
ALTER TABLE SYS_USER_ROLE ADD CONSTRAINT PK_SYSUSERROLE PRIMARY KEY (ID);
ALTER TABLE SYS_USER_ROLE ADD CONSTRAINT IK_SYSUSERROLE_USERID_ROLEID UNIQUE (USER_ID,ROLE_ID);
COMMENT ON TABLE SYS_USER_ROLE IS '系统管理 - 用户角色表';
COMMENT ON COLUMN SYS_USER_ROLE.ID IS 'ID (Label: 主键)';
COMMENT ON COLUMN SYS_USER_ROLE.USER_ID IS '用户ID';
COMMENT ON COLUMN SYS_USER_ROLE.ROLE_ID IS '角色ID';
COMMENT ON COLUMN SYS_USER_ROLE.CREATED_TS IS '创建时间 (Label: 创建时间)';
COMMENT ON COLUMN SYS_USER_ROLE.CREATED_BY IS '创建人用户ID (Label: 创建人)';
COMMENT ON COLUMN SYS_USER_ROLE.LAST_UPD_TS IS '修改时间 (Label: 修改时间)';
COMMENT ON COLUMN SYS_USER_ROLE.LAST_UPD_BY IS '修改人用户ID (Label: 修改人)';
COMMENT ON COLUMN SYS_USER_ROLE.UPDATE_COUNT IS '修改次数 (Label: 修改次数)';

