package solitour_backend.solitour.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.admin.entity.Banner;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {


    List<Banner> findAllByOrderById();
}
