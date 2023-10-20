package com.tracker.services.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.LinkedSocialMedia;
import com.tracker.commons.models.UserMedia;

@Repository
@Transactional
public interface LinkedSocialMediaRepository extends JpaRepository<LinkedSocialMedia, String> {

	//VIEW - create view linked_social_media as select UUID() as id, userid, social_type, social_id from user_media where social_id is not null order by userid
	
	public List<LinkedSocialMedia> findByUseridIn(List<Long> userids);
	//public List<UserMedia> findByUseridIn1(List<Long> userids);


}
