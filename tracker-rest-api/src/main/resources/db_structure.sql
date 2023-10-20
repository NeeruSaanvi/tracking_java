

DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `ClientId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(150) NOT NULL,
  `Keywords` text,
  `Active` bit(1) NOT NULL DEFAULT b'1',
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ClientId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `UserId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UserType` enum('ADMIN','CLIENT_ADMIN','USER') NOT NULL DEFAULT 'USER',
  `Email` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `PasswordSalt` varchar(255) NOT NULL,
  `FirstName` varchar(255) DEFAULT NULL,
  `MiddleName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `FacebookLogin` bit(1) NOT NULL DEFAULT b'0',
  `TwitterLogin` bit(1) NOT NULL DEFAULT b'0',
  `TempDisabled` bit(1) NOT NULL DEFAULT b'0',
  `PermDisabled` bit(1) NOT NULL DEFAULT b'0',
  `LastLogin` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `LastFailedLogin` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `EmailValidated` bit(1) NOT NULL DEFAULT b'0',
  `PasswordExpired` bit(1) NOT NULL DEFAULT b'0',
  `PasswordResetToken` varchar(255) DEFAULT NULL,
  `PasswordResetTokenExpireTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `NumberOfFailedLogins` int(11) NOT NULL DEFAULT '0',
  `Country` varchar(255) NOT NULL DEFAULT 'USA',
  `CurrencyId` bigint(20) unsigned NOT NULL DEFAULT '1',
  `Gender` enum('Female','Male') DEFAULT NULL,
  `dob` timestamp NULL DEFAULT NULL,
  `TimeZone` int(11) NOT NULL DEFAULT '1',
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`UserId`),
  KEY `UX_Users_Email` (`Email`),
  KEY `FX_Users_Currency_Id` (`CurrencyId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `TeamId` bigint(20) unsigned NOT NULL AUTO_INCREMENT, 
  `Name` varchar(150) NOT NULL, 
  `Active` bit(1) NOT NULL DEFAULT b'1',
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`TeamId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;


DROP TABLE IF EXISTS `team_user_map`;
CREATE TABLE `team_user_map` (
  `UserId` bigint(20) unsigned NOT NULL, 
  `TeamId` bigint(20) unsigned NOT NULL,   
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;


DROP TABLE IF EXISTS `user_client_map`;
CREATE TABLE `user_client_map` (
  `ClientId` bigint(20) unsigned NOT NULL,
  `UserId` bigint(20) unsigned NOT NULL,  
  `data_download_flag_fb` int(11) DEFAULT '0' COMMENT '0=Yes,1=No,2=TokenError',
  `data_download_flag_in` int(11) DEFAULT '0' COMMENT '0=Yes,1=No,2=TokenError',
  `data_download_flag_yt` int(11) DEFAULT '0' COMMENT '0=Yes,1=No,2=TokenError',
  `data_download_date_fb` date DEFAULT NULL,
  `data_download_date_in` date DEFAULT NULL,
  `data_download_date_yt` date DEFAULT NULL,
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;


DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `EventId` bigint(20) NOT NULL AUTO_INCREMENT,
  `EventTitle` varchar(150) NOT NULL,
  `Description` varchar(5000) NOT NULL,
  `VenueAddress` varchar(100) DEFAULT NULL,
  `ContactPerson` varchar(200) NOT NULL,
  `ContactPhone` varchar(100) NOT NULL,
  `EventStartDate` date NOT NULL,
  `EventStartTime` varchar(100) NOT NULL,
  `EventEndDate` date NOT NULL,
  `UserId` int(9) NOT NULL,
  `AttendanceCount` int(11) DEFAULT '0',
  `AttendanceHours` int(11) DEFAULT '0',  
  `Status` varchar(20) NOT NULL,
  `EventState` text,
  `SharedTO` varchar(1000) DEFAULT 'all',  
  `EventAttachment` varchar(1000) DEFAULT NULL,
  `EventAttachment1` text CHARACTER SET utf8,
  `EventAttachment2` text CHARACTER SET utf8,
  `EventAttachment3` text CHARACTER SET utf8,
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`EventId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `user_media`;
CREATE TABLE `user_media` (
  `UserMediaId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) NOT NULL,
  `UserName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `FullName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `Email` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `Gender` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `SocialType` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `Image` varchar(255) CHARACTER SET utf8 DEFAULT NULL,  
  `SocialType` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `Status` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `AccessToken` varchar(7000) CHARACTER SET utf8 DEFAULT NULL,  
  `DownloadDated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,    
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`UserMediaId`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `facebook_feed`;
CREATE TABLE `facebook_feed` (
  `FacebookFeedId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserMediaId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'FK user_media',
  `Id` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `Story` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `Link` varchar(5000) DEFAULT NULL,
  `LikesCount` int(11) DEFAULT NULL,
  `CommentCount` int(11) DEFAULT '0',
  `ShareCount` int(11) DEFAULT '0',
  `ViewCount` int(11) DEFAULT '0',  
  `FeedType` varchar(1000) DEFAULT NULL,
  `PhotoUrl` varchar(5000) DEFAULT NULL,
  `PostType` varchar(100) DEFAULT 'profile',
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`FacebookFeedId`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `instagram_feed`;
CREATE TABLE `instagram_feed` (
  `InstagramFeedId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserMediaId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'FK with user_media',
  `Id` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT 'Tracking admin',
  `Text` text CHARACTER SET utf8,
  `Link` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `Likes` int(11) DEFAULT '0',
  `CommentsCount` int(11) DEFAULT NULL,
  `LikesCount` int(11) DEFAULT NULL,
  `ThumbnailImage` text CHARACTER SET utf8,
  `StandardImage` text CHARACTER SET utf8,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`InstagramFeedId`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `tournaments`;
CREATE TABLE `tournaments` (
  `TournamentId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) unsigned NOT NULL,  
  `AnglerName` varchar(200) DEFAULT NULL,
  `TournamentTitle` varchar(500) NOT NULL,
  `FinishedPlace` varchar(350) NOT NULL,
  `ReferenceName` varchar(100) DEFAULT NULL,
  `ContactInfo` varchar(200) NOT NULL,
  `TournamentPlace` varchar(200) DEFAULT NULL,
  `TournamentDate` date NOT NULL,
  `SharedTO` varchar(1000) DEFAULT 'all',  
  `Status` varchar(20) NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`TournamentId`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `lead`;
CREATE TABLE `lead` (
  `LeadId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) unsigned NOT NULL,  
  `LeadDealName` varchar(1000) DEFAULT NULL,
  `LeadConsuFirstName` varchar(500) DEFAULT NULL,
  `LeadConsuLastName` varchar(500) DEFAULT NULL,
  `LeadDelAdd` varchar(2000) DEFAULT NULL,
  `LeadConsuCity` varchar(200) DEFAULT NULL,
  `LeadConsuState` varchar(200) DEFAULT NULL,
  `LeadConsuZip` varchar(200) NOT NULL,
  `LeadDelPhone` varchar(1000) DEFAULT NULL,
  `LeadBuyName` varchar(1000) DEFAULT NULL,
  `LeadBuyPhone` varchar(1000) DEFAULT NULL,
  `LeadBuyEmail` varchar(1000) DEFAULT NULL,
  `LeadRel` varchar(5000) DEFAULT NULL,
  `SharedTO` varchar(1000) DEFAULT 'all',  
  `LeadType` varchar(500) DEFAULT 'Dealer',
  `Status` varchar(20) NOT NULL,
  `PostedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LeadId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `print`;
CREATE TABLE `print` (
  `PrintId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) unsigned NOT NULL,  
  `PrintPublishName` varchar(2000) DEFAULT NULL,
  `PrintIssueDate` date DEFAULT NULL,
  `PrintArtName` varchar(2000) DEFAULT NULL,
  `PrintCovType` varchar(1000) DEFAULT NULL,
  `PrintImgPath` varchar(1000) DEFAULT NULL,
  `SharedTO` varchar(1000) DEFAULT 'all',  
  `Status` varchar(20) NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PrintId`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `web`;
CREATE TABLE `web` (
  `WebId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) unsigned NOT NULL,  
  `Name` varchar(1000) DEFAULT NULL,  
  `URL` varchar(1000) DEFAULT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
  PRIMARY KEY (`WebId`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



