package com.tracker.services.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.CompanyTeamUser;

@Repository
@Transactional
public interface CompanyTeamUserRepository extends JpaRepository<CompanyTeamUser,String>  {

	public List<CompanyTeamUser> findByUserIdAndCompanyId(Long userId, Long companyId);
	
	public List<CompanyTeamUser> findByCompanyId( Long companyId);
}
