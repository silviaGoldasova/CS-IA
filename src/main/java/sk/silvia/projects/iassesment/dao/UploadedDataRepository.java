package sk.silvia.projects.iassesment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.iassesment.dto.UploadedDTO;

public interface UploadedDataRepository extends JpaRepository<UploadedDTO, Long> {
}
