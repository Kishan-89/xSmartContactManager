package com.smart.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.Contact;
import com.smart.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer>  {

	//pageable will contain two thing current page and contact(items) per page
	@Query("from Contact as c where c.user.u_Id = :id")
	public Page<Contact> findContactByUser(@Param("id") int id, Pageable pageable);
	
	//this method is for the search of contacts which matches with a particular words for a particular user
	public List<Contact> findByNameContainingAndUser(String word,User user);
}
