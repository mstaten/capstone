package com.staten.capstone.models.data;

import com.staten.capstone.models.Report;
import com.staten.capstone.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ReportDao extends PagingAndSortingRepository<Report, Integer> {

    Report findById(int id);

    Slice<Report> findAll(Sort sort);

    Slice<Report> findAllByUser(User user, Pageable pageable);

    @Override
    void delete(Report report);
}
