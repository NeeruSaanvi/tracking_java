package com.tracker.commons.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Immutable
@Entity
@Table(name = "company_team_user")
@EqualsAndHashCode
@Data
public class CompanyTeamUser implements Serializable {

	@Id
	private String id;
	private long userId;
	private long teamId;
	private String teamName;
	private long companyId;
	
}
