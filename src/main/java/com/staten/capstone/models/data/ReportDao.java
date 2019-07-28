package com.staten.capstone.models.data;

import com.staten.capstone.models.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ReportDao extends CrudRepository<Report, Integer> {

    public Report findByTitle(String title);

    public Report findById(int id);

    @Override
    void delete(Report report);
}
