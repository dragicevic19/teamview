package org.teamview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.TeamDTO;
import org.teamview.model.Team;
import org.teamview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public PaginationDTO<TeamDTO> findAll(PageRequest pageable) {

        Page<Team> pagedTeams = this.teamRepository.findAll(pageable);
        List<TeamDTO> dtos = new ArrayList<>();
        for (Team team : pagedTeams) {
            dtos.add(new TeamDTO(team));
        }

        return new PaginationDTO<>(dtos, pagedTeams.getTotalElements());
    }
}
