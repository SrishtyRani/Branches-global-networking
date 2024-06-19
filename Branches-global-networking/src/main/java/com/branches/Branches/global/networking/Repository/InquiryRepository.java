package com.branches.Branches.global.networking.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.branches.Branches.global.networking.model.InquiryModel;

public interface InquiryRepository  extends JpaRepository<InquiryModel, Long>{

	   Page<InquiryModel> findAll(Pageable pageable);



}
