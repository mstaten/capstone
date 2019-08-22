package com.staten.capstone.models.data;

import com.staten.capstone.models.Report;
import com.staten.capstone.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
@Transactional
public interface ReportDao extends PagingAndSortingRepository<Report, Integer> {

    Report findByTitle(String title);

    Report findById(int id);

    Slice<Report> findAllByUrgency(int urgency, Pageable pageable);

    Slice<Report> findAllByUser(User user, Pageable pageable);

    @Override
    void delete(Report report);
}
