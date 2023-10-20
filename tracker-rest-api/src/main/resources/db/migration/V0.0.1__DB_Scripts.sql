 

SET FOREIGN_KEY_CHECKS=0;

DROP PROCEDURE IF EXISTS `pRaiseError`;
DELIMITER ;;

CREATE PROCEDURE `pRaiseError`(sErrorMsg VARCHAR(255),sErrorCode VARCHAR(255))
BEGIN 
  CALL pExecuteImmediate(fFormat('%s', sErrorMsg),sErrorCode);
END;

;;
DELIMITER ;


DROP PROCEDURE IF EXISTS `pExecuteImmediate`;
DELIMITER ;;

CREATE PROCEDURE `pExecuteImmediate`(IN sErrorMessage TEXT,sErrorCode VARCHAR(255))
BEGIN 
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = sErrorMessage, MYSQL_ERRNO=sErrorCode; 
END;

;;
DELIMITER ;


-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `UserId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UserType` enum('ADMIN','CLIENT_ADMIN', 'USER') NOT NULL DEFAULT 'USER',
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


DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `ClientId` bigint(20) unsigned NOT NULL AUTO_INCREMENT, 
  `Name` varchar(150) NOT NULL, 
  `Active` bit(1) NOT NULL DEFAULT b'1',
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ClientId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

 
DROP TABLE IF EXISTS `email_notifications`;
CREATE TABLE `email_notifications` (
  `EmailId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `Status` int(11) NOT NULL DEFAULT '0' COMMENT '0 = failed, 1 = sent',
  `RetryCount` int(11) NOT NULL DEFAULT '0',
  `Sender` varchar(500) NOT NULL,
  `Recipients` varchar(500) NOT NULL,
  `CC` varchar(500) DEFAULT NULL,
  `BCC` varchar(500) DEFAULT NULL,
  `Body` mediumtext,
  `Subject` varchar(250) NOT NULL,
  `ReplyTo` varchar(500) DEFAULT NULL,
  `Attachment` longblob,
  `CreatedBy` bigint(20) unsigned NOT NULL,
  `ModifiedBy` bigint(20) unsigned NOT NULL,
  `DateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`EmailId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
 
 
 
DROP TABLE IF EXISTS `config_properties`;
CREATE TABLE `config_properties` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `Property` varchar(255) NOT NULL,
  `PropertyValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

 
SET FOREIGN_KEY_CHECKS=1;