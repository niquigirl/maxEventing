ALTER TABLE [dbo].[AssociateTask] DROP CONSTRAINT [DF_AssociateTask_ignored]
GO

/****** Object:  Table [dbo].[Rating]    Script Date: 4/11/2014 12:02:10 PM ******/
DROP TABLE [dbo].[Rating]
GO
/****** Object:  Table [dbo].[AutoTaskFlow]    Script Date: 4/11/2014 12:03:58 PM ******/
ALTER TABLE [dbo].[AutoTaskFlow] DROP CONSTRAINT [DF_AutoTaskFlow_canRepeat]
GO
DROP TABLE [dbo].[AutoTaskFlow]
GO
/****** Object:  Table [dbo].[AssociateTask]    Script Date: 4/11/2014 11:01:05 AM ******/
DROP TABLE [dbo].[AssociateTask]
GO
/****** Object:  Table [dbo].[TaskTemplate]    Script Date: 4/11/2014 11:59:47 AM ******/
DROP TABLE [dbo].[TaskTemplate]
GO
/****** Object:  Table [dbo].[NotificationTemplate]    Script Date: 4/11/2014 12:03:05 PM ******/
DROP TABLE [dbo].[NotificationTemplate]
GO
/****** Object:  Table [dbo].[ProspectContact]    Script Date: 4/11/2014 12:09:20 PM ******/
DROP TABLE [dbo].[ProspectContact]
GO
/****** Object:  Table [dbo].[ProspectProperty]    Script Date: 4/11/2014 12:28:47 PM ******/
DROP TABLE [dbo].[ProspectProperty]
GO
/****** Object:  Table [dbo].[Prospect]    Script Date: 4/11/2014 12:05:20 PM ******/
DROP TABLE [dbo].[Prospect]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO

/********** Rating ************************/
CREATE TABLE [dbo].[Rating](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [ratingSetId] [bigint] NOT NULL,
  [ratingValue] [int] NOT NULL,
  [displayOrder] [int] NULL,
  [descriptionKey] [varchar](200) NULL,
  [image] [varchar](2084) NULL
) ON [PRIMARY]

/********** Prospect ************************/
CREATE TABLE [dbo].[Prospect](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [name] [varchar](200) NULL,
  [cellPhone] [varchar](50) NULL,
  [homePhone] [varchar](50) NULL,
  [workPhone] [varchar](50) NULL,
  [secondaryPhone] [varchar](50) NULL,
  [addressLine1] [varchar](200) NULL,
  [addressLine2] [varchar](200) NULL,
  [city] [varchar](50) NULL,
  [state] [varchar](50) NULL,
  [zip] [varchar](12) NULL,
  [ownerId] [bigint] NULL,
  [createdDate] [date] NOT NULL
) ON [PRIMARY]

/********** ProspectContact ************************/
CREATE TABLE [dbo].[ProspectContact](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [prospectId] [bigint] NOT NULL,
  [date] [date] NULL,
  [contactTypeDescriptionKey] [varchar](200) NULL,
  [artifactDescriptionKey] [varchar](200) NULL,
  [artifactUrl] [varchar](2084) NULL,
  [notes] [varchar](max) NULL,
  [createdDate] [date] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

/********** ProspectProperty ************************/
CREATE TABLE [dbo].[ProspectProperty](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [prospectId] [bigint] NOT NULL,
  [propertyNameKey] [varchar](200) NOT NULL,
  [ratingResponseId] [bigint] NULL,
  [textResponse] [varchar](max) NULL,
  [notes] [varchar](max) NULL,
  [dateResponse] [date] NULL,
  [reminder] [date] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

/********** NotificationTemplate ************************/
CREATE TABLE [dbo].[NotificationTemplate](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [eventName] [varchar](200) NOT NULL,
  [recipientType] [varchar](80) NULL,
  [emailTemplateUrl] [varchar](2084) NULL,
  [smsTemplateUrl] [varchar](2084) NULL,
  [pushTemplateUrl] [varchar](2084) NULL
) ON [PRIMARY]

/********** TaskTemplate ************************/
CREATE TABLE [dbo].[TaskTemplate](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [taskClass] [varchar](200) NOT NULL,
  [descriptionKey] [varchar](200) NULL,
  [url] [varchar](2084) NULL,
  [detailUrl] [varchar](2084) NULL,
  [formUrl] [varchar](2084) NULL
) ON [PRIMARY]

/********** AssociateTask ************************/
CREATE TABLE [dbo].[AssociateTask](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [associateId] [bigint] NOT NULL,
  [taskTemplateId] [bigint] NOT NULL,
  [createdDate] [date] NOT NULL,
  [dueDate] [date] NULL,
  [ignored] [tinyint] NULL,
  [subjectId] [bigint] NULL,
  [completedDate] [date] NULL,
  [subjectObjectType] [varchar](80) NULL
) ON [PRIMARY]
GO

/********** AutoTaskFlow ************************/
CREATE TABLE [dbo].[AutoTaskFlow](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [resultTaskTemplateId] [bigint] NOT NULL,
  [triggerTaskTemplateId] [bigint] NULL,
  [dependentTaskTemplateId] [bigint] NULL,
  [numberToComplete] [int] NULL,
  [autoDueDateNumDays] [int] NULL,
  [assigneeType] [varchar](80) NOT NULL,
  [minRepeatDelayNumDays] [int] NULL,
  [canRepeat] [bit] NOT NULL,
  [subjectType] [varchar](80) NULL,
  [description] [varchar](max) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
ALTER TABLE [dbo].[AutoTaskFlow] ADD  CONSTRAINT [DF_AutoTaskFlow_canRepeat]  DEFAULT ((0)) FOR [canRepeat]
GO



SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[AssociateTask] ADD  CONSTRAINT [DF_AssociateTask_ignored]  DEFAULT ((0)) FOR [ignored]
GO


insert into [dbo].[TaskTemplate](taskClass, descriptionKey, url, detailUrl, formUrl) values
  ('WATCH_VIDEO', 'WATCH_VIDEO', 'www.videos.max.com', 'www.videos.max.com', 'www.maxcoach.com/CreateWatchVideoTask.html'),
  ('LOG_IN', 'LOG_IN', 'www.yourvo.max.com/login', 'www.maxcoach.com/LoginTaskDetails.html', 'www.maxcoach.com/CreateLoginTask.html'),
  ('SIGN_UP', 'SIGN_UP', 'FOR TRACKING PURPOSES ONLY', 'www.yourvo.max.com/SignUpDetails.html', 'FOR TRACKING PURPOSES ONLY'),
  ('START_COACH', 'START_COACH', 'www.yourvo.max.com/enableCoach', 'www.maxcoach.com/WelcomeToMaxCoaching.html', 'www.maxcoach.com/CreateStartYourCoachTask.html'),
  ('ENTER_YOUR_WHY', 'ENTER_YOUR_WHY', 'www.yourvo.max.com/WhatsYourWhy.html', 'www.maxcoach.com/WhyYouShouldEnterYourWhy.html', 'www.maxcoach.com/CreateEnterYourWhyTask.html'),
  ('ADD_PROSPECT', 'ADD_PROSPECT', 'www.yourvo.max.com/NewProspect.html', 'www.maxcoach.com/WhyAndHowYouShouldAddProspects.html', 'www.maxcoach.com/CreateAddProspectTask.html'),
  ('CONTACT_PROSPECT', 'CONTACT_PROSPECT', 'www.yourvo.max.com/LogProspectContact.html', 'www.maxcoach.com/8PhasesOfProspectContacts.html', 'www.maxcoach.com/CreateContactProspectTask.html'),
  ('CONTACT_ASSOCIATE', 'CONTACT_ASSOCIATE', 'www.yourvo.max.com/LogAssociateContact.html', 'www.maxcoach.com/BeAGoodMentorToYourDownlineAssociates.html', 'www.maxcoach.com/CreateContactAssociateTask.html'),
  ('ADD_5_PROSPECTS', 'ADD_5_PROSPECTS', 'www.yourvo.max.com/NewProspect.html', 'www.maxcoach.com/WhyYouShouldAdd5Prospects.html', 'www.maxcoach.com/CreateAdd5ProspectsTask.html'),
  ('CONTACT_ASSOCIATE', 'SPONSOR_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=sponsor', 'www.WhyYouShouldContactNewAssociates.html?uplineType=sponsor', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=sponsor'),
  ('CONTACT_ASSOCIATE', 'ENROLLER_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=enroller', 'www.WhyYouShouldContactNewAssociates.html?uplineType=enroller', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=enroller'),
  ('CONTACT_ASSOCIATE', 'UPLINE_BRONZE_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=bronze', 'www.WhyYouShouldContactNewAssociates.html?uplineType=bronze', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=bronze'),
  ('CONTACT_ASSOCIATE', 'UPLINE_SILVER_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=silver', 'www.WhyYouShouldContactNewAssociates.html?uplineType=silver', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=silver'),
  ('CONTACT_ASSOCIATE', 'UPLINE_GOLD_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=gold', 'www.WhyYouShouldContactNewAssociates.html?uplineType=gold', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=gold')

insert into [dbo].[AutoTaskFlow]
(description, resultTaskTemplateId, triggerTaskTemplateId, dependentTaskTemplateId, numberToComplete, autoDueDateNumDays, assigneeType, minRepeatDelayNumDays, canRepeat, subjectType)
values
  ('Create ''Enter your why'' task on ''Start Your Coach''',
   (select id from TaskTemplate where descriptionKey = 'ENTER_YOUR_WHY'), (select id from TaskTemplate where descriptionKey = 'START_COACH'),
   null, null, null, 'ASSOCIATE', null, 0, null),
  ('Create ''Add 5 Prospects'' task when why has been entered if coach has been started',
   (select id from TaskTemplate where descriptionKey = 'ADD_5_PROSPECTS'), (select id from TaskTemplate where descriptionKey = 'ENTER_YOUR_WHY'), (select id from TaskTemplate where descriptionKey = 'START_COACH'),
   5, null, 'ASSOCIATE', 1, 1, null),
  ('Create ''Contact Prospect'' task when any Prospects are added',
   (select id from TaskTemplate where descriptionKey = 'CONTACT_PROSPECT'), (select id from TaskTemplate where descriptionKey = 'ADD_PROSPECT'),
   null, 8, 3, 'ASSOCIATE', null, 1, 'PROSPECT'),
  ('Create ''Contact New Associate'' task for the Sponsor when new Associates sign',
   (select id from TaskTemplate where descriptionKey = 'SPONSOR_CONTACT_NEW_ASSOCIATE'), (select id from TaskTemplate where descriptionKey = 'SIGN_UP'),
   null, null, 3, 'SPONSOR', null, 0, 'ASSOCIATE'),
  ('Create ''Contact New Associate'' task for the Upline Gold when new Associates sign',
   (select id from TaskTemplate where descriptionKey = 'UPLINE_GOLD_CONTACT_NEW_ASSOCIATE'), (select id from TaskTemplate where descriptionKey = 'SIGN_UP'),
   null, null, 3, 'UPLINE_GOLD', null, 0, 'ASSOCIATE')
