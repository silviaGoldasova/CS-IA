package sk.silvia.projects.IAssesment1.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.IAssesment1.dto.UploadedDTO;

public interface UploadedDataRepository extends JpaRepository<UploadedDTO, Long> {
}
