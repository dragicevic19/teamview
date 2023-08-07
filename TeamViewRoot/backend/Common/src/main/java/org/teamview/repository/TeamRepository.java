package org.teamview.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.teamview.model.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
}
