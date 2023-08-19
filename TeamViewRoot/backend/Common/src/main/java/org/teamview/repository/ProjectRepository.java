//package org.teamview.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.teamview.model.Project;
//
//import java.util.Optional;
//
//public interface ProjectRepository extends JpaRepository<Project, Long> {
//
//    @Override
//    @Query("select p from Project p where p.id=?1 and p.deleted=false")
//    Optional<Project> findById(Long id);
//
//    @Override
//    @Query("select p from Project p where p.deleted=false")
//    Page<Project> findAll(Pageable pageable);
//
//}
