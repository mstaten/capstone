package com.staten.capstone.models.data;

import com.staten.capstone.models.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface LocationDao extends CrudRepository<Location, Integer> {

    public Location findById(int id);

    public Location findByLatLng(String latLng);

    @Override
    void delete(Location location);

}
