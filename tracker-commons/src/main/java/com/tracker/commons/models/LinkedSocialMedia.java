package com.tracker.commons.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "linked_social_media")
@EqualsAndHashCode
@ToString(includeFieldNames = true, exclude= {"id"})
@Data
public class LinkedSocialMedia  implements Serializable {

	@Id
	@GeneratedValue
	private String id;
	private long userid;
	
	@Column(name="social_type")
	private String socialType;
	
	@Column(name="social_id")
	private String socialId;
}
