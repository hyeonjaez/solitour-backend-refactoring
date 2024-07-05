package solitour_backend.solitour.info_tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.info_tag.entity.InfoTag;

public interface InfoTagRepository extends JpaRepository<InfoTag, Long> {
}
