For the sake of testing, the following have been added to the DB:

####################################################################################################################
## Set up the Tasks
####################################################################################################################
insert into TaskTemplate(descriptionKey, url, detailUrl, formUrl) values
('WATCH_VIDEO', 'www.videos.max.com', 'www.videos.max.com', 'www.maxcoach.com/CreateWatchVideoTask.html'),
('LOG_IN', 'www.yourvo.max.com/login', 'www.maxcoach.com/LoginTaskDetails.html', 'www.maxcoach.com/CreateLoginTask.html'),
('SIGN_UP', 'FOR TRACKING PURPOSES ONLY', 'www.yourvo.max.com/SignUpDetails.html', 'FOR TRACKING PURPOSES ONLY'),
('START_COACH', 'www.yourvo.max.com/enableCoach', 'www.maxcoach.com/WelcomeToMaxCoaching.html', 'www.maxcoach.com/CreateStartYourCoachTask.html'),
('ENTER_YOUR_WHY', 'www.yourvo.max.com/WhatsYourWhy.html', 'www.maxcoach.com/WhyYouShouldEnterYourWhy.html', 'www.maxcoach.com/CreateEnterYourWhyTask.html'),
('ADD_PROSPECT', 'www.yourvo.max.com/NewProspect.html', 'www.maxcoach.com/WhyAndHowYouShouldAddProspects.html', 'www.maxcoach.com/CreateAddProspectTask.html'),
('CONTACT_PROSPECT', 'www.yourvo.max.com/LogProspectContact.html', 'www.maxcoach.com/8PhasesOfProspectContacts.html', 'www.maxcoach.com/CreateContactProspectTask.html'),
('ADD_5_PROSPECTS', 'www.yourvo.max.com/NewProspect.html', 'www.maxcoach.com/WhyYouShouldAdd5Prospects.html', 'www.maxcoach.com/CreateAdd5ProspectsTask.html'),
('WATCH_VIDEO', 'www.videos.max.com', 'www.videos.max.com', 'www.maxcoach.com/CreateWatchVideoTask.html'),
('SPONSOR_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=sponsor', 'www.WhyYouShouldContactNewAssociates.html?uplineType=sponsor', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=sponsor'),
('ENROLLER_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=enroller', 'www.WhyYouShouldContactNewAssociates.html?uplineType=enroller', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=enroller'),
('UPLINE_BRONZE_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=bronze', 'www.WhyYouShouldContactNewAssociates.html?uplineType=bronze', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=bronze'),
('UPLINE_SILVER_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=silver', 'www.WhyYouShouldContactNewAssociates.html?uplineType=silver', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=silver'),
('UPLINE_GOLD_CONTACT_NEW_ASSOCIATE', 'www.LogNewAssociateContact.html?uplineType=gold', 'www.WhyYouShouldContactNewAssociates.html?uplineType=gold', 'www.maxcoach.com/CreateContactNewAssociateTask.html?uplineType=gold')


####################################################################################################################
## Set up the Task Flows
####################################################################################################################
insert into AutoTaskFlow
(description, resultTaskTemplateId, triggerTaskTemplateId, dependentTaskTemplateId, numberToComplete, autoDueDateNumDays, assigneeType, minRepeatDelayNumDays, canRepeat, subjectType)
values
('Create ''Enter your why'' task on ''Start Your Coach''', 39, 48, null, null, null, 'ASSOCIATE', null, null, null),
('Create ''Add 5 Prospects'' task when why has been entered', 42, 39, 40, 5, null, 'ASSOCIATE', 1, 1, null),
('Create ''Contact Prospect'' task when any Prospects are added', 41, 40, null, 8, 3, 'ASSOCIATE', null, 1, 'PROSPECT'),
('Create ''Contact New Associate'' task for the Sponsor when new Associates sign', 43, 49, null, null, 3, 'SPONSOR', null, 0, 'ASSOCIATE'),
('Create ''Contact New Associate'' task for the Upline Gold when new Associates sign', 47, 49, null, null, 3, 'UPLINE_GOLD', null, 0, 'ASSOCIATE')
