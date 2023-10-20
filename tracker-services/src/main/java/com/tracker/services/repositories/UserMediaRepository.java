package com.tracker.services.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.User;
import com.tracker.commons.models.UserMedia;

@Repository
@Transactional
public interface UserMediaRepository extends JpaRepository<UserMedia, Long>,UserMediaRepositoryCustom {
	public List<UserMedia> findBySocialType(String type);
	public List<UserMedia> findByUsernameInAndSocialType(List<String> usernames, String type);
	
	public List<UserMedia> findByUseridIn(List<Long> userids);
	public UserMedia findByUserid(Long userId);
	//public String getAccessToken(Long loggedInUserId, String string);
  
}
