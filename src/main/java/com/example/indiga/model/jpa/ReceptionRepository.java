package com.example.indiga.model.jpa;

import com.example.indiga.enums.ReceptionExportStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceptionRepository extends CrudRepository<Reception, Long> {

    @Override
    List<Reception> findAll();

    List<Reception> findByReceptionExportStatus(ReceptionExportStatus receptionExportStatus);
}
